package com.lms.coursemanager;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LmsIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final HttpClient client = HttpClient.newHttpClient();

    private static String savedLessonId;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    @Order(1)
    public void testCreateCourse_Success() throws Exception {
        String json = "{"
                + "\"courseCode\":\"CRS101\","
                + "\"startDate\":\"15/10/2023\","
                + "\"courseName\":\"Java Web Development\","
                + "\"category\":\"Technology\","
                + "\"instructor\":\"Dr. Nguyen Van A\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/api/courses"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        String body = response.body();
        assertNotNull(body);
        assertTrue(body.contains("\"success\":true"));
        assertTrue(body.contains("\"courseCode\":\"CRS101\""));
        assertTrue(body.contains("\"startDate\":\"15/10/2023\""));
    }

    @Test
    @Order(2)
    public void testCreateCourse_Duplicate_ReturnsBadRequest() throws Exception {
        String json = "{"
                + "\"courseCode\":\"CRS101\","
                + "\"startDate\":\"15/10/2023\","
                + "\"courseName\":\"Another Course\","
                + "\"category\":\"Technology\","
                + "\"instructor\":\"Instructor B\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/api/courses"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        String body = response.body();
        assertNotNull(body);
        assertTrue(body.contains("\"success\":false"));
        assertTrue(body.contains("already exists"));
    }

    @Test
    @Order(3)
    public void testCreateLesson_Success() throws Exception {
        String json = "{"
                + "\"lessonName\":\"Intro to Java\","
                + "\"duration\":60,"
                + "\"contentType\":\"Video\","
                + "\"status\":\"Đang mở\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/api/lessons?courseCode=CRS101&startDate=15/10/2023"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        String body = response.body();
        assertNotNull(body);
        assertTrue(body.contains("\"success\":true"));
        assertTrue(body.contains("\"lessonName\":\"Intro to Java\""));
        assertTrue(body.contains("\"id\":"));

        // Extract saved lesson ID
        int idIndex = body.indexOf("\"id\":");
        if (idIndex != -1) {
            String sub = body.substring(idIndex + 5);
            int commaIndex = sub.indexOf(",");
            int braceIndex = sub.indexOf("}");
            int end = (commaIndex != -1) ? commaIndex : braceIndex;
            savedLessonId = sub.substring(0, end).trim();
        }
        assertNotNull(savedLessonId);
    }

    @Test
    @Order(4)
    public void testGetLessons_Success() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/api/lessons?courseCode=CRS101&startDate=15/10/2023"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String body = response.body();
        assertNotNull(body);
        assertTrue(body.contains("\"success\":true"));
        assertTrue(body.contains("\"lessonName\":\"Intro to Java\""));
    }

    @Test
    @Order(5)
    public void testUpdateLesson_Success() throws Exception {
        String json = "{"
                + "\"lessonName\":\"Intro to Java v2\","
                + "\"duration\":75,"
                + "\"contentType\":\"Video\","
                + "\"status\":\"Đang mở\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/api/lessons/" + savedLessonId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String body = response.body();
        assertNotNull(body);
        assertTrue(body.contains("\"success\":true"));
        assertTrue(body.contains("\"lessonName\":\"Intro to Java v2\""));
        assertTrue(body.contains("\"duration\":75"));
    }

    @Test
    @Order(6)
    public void testDeleteLesson_Success() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/api/lessons/" + savedLessonId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"success\":true"));

        // Verify lesson is deleted
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/api/lessons?courseCode=CRS101&startDate=15/10/2023"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode());
        assertTrue(getResponse.body().contains("\"data\":[]"));
    }
}
