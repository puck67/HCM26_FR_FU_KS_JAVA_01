package com.lms.menumanager.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class MenuControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testDashboardLoading() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("totalMenus"))
                .andExpect(model().attributeExists("totalParentMenus"))
                .andExpect(model().attributeExists("totalSubMenus"))
                .andExpect(model().attributeExists("allMenus"))
                .andExpect(model().attribute("currentRole", "ADMIN"))
                .andExpect(model().attributeExists("sidebarMenus"));
    }

    @Test
    public void testSwitchRole() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(get("/switch-role").param("role", "TEACHER").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        
        // Assert role is changed in session
        mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("currentRole", "TEACHER"));
    }

    @Test
    public void testMenuListLoading() throws Exception {
        mockMvc.perform(get("/menus"))
                .andExpect(status().isOk())
                .andExpect(view().name("menus/menu-list"))
                .andExpect(model().attributeExists("menus"));
    }

    @Test
    public void testMockPageLoading() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("mock-page"))
                .andExpect(model().attribute("pageTitle", "Students"))
                .andExpect(model().attribute("pageUri", "/students"));
    }
}
