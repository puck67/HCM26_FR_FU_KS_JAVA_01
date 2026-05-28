package org.fsa_2026.services;

import org.fsa_2026.dao.CousrseDao;
import org.fsa_2026.dao.LearnerDao;
import org.fsa_2026.dao.TrainnerDao;
import org.fsa_2026.enities.Cousrse;
import org.fsa_2026.enities.Learner;
import org.fsa_2026.enities.Trainner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchingRecordServiceTest {

    @Mock
    private CousrseDao cousrseDao;

    @Mock
    private LearnerDao learnerDao;

    @Mock
    private TrainnerDao trainnerDao;

    @InjectMocks
    private SearchingRecordService searchingService;

    @BeforeEach
    void setUp() throws Exception {
        // Khởi tạo các mock object trước
        MockitoAnnotations.openMocks(this);

        // Sử dụng Reflection để ép buộc cài đặt các trường private trong môi trường Java 8
        // Điều này đảm bảo các DAO bên trong Service sẽ không bao giờ bị null nữa!
        Field courseField = SearchingRecordService.class.getDeclaredField("cousrseDao");
        courseField.setAccessible(true);
        courseField.set(searchingService, cousrseDao);

        Field learnerField = SearchingRecordService.class.getDeclaredField("learnerDao");
        learnerField.setAccessible(true);
        learnerField.set(searchingService, learnerDao);

        Field trainerField = SearchingRecordService.class.getDeclaredField("trainnerDao");
        trainerField.setAccessible(true);
        trainerField.set(searchingService, trainnerDao);
    }

    @Test
    void testFindCousrseByName_Found() {
        // Arrange
        Cousrse c1 = new Cousrse();
        c1.setCousrseName("Java Core");

        when(cousrseDao.findAll()).thenReturn(Collections.singletonList(c1));

        // Act
        Cousrse result = searchingService.findCousrseByName("Java Core");

        // Assert
        assertNotNull(result, "Course should be found");
        assertEquals("Java Core", result.getCousrseName());
    }

    @Test
    void testFindLearnerByName_Found() {
        // Arrange
        Learner l1 = new Learner();
        l1.setStudentName("TuanND110");

        when(learnerDao.findAll()).thenReturn(Collections.singletonList(l1));

        // Act
        Learner result = searchingService.findLearnerByName("TuanND110");

        // Assert
        assertNotNull(result, "Learner should be found");
        assertEquals("TuanND110", result.getStudentName());
    }

    @Test
    void testFindTrainnerByName_Found() {
        // Arrange
        Trainner t1 = new Trainner();
        t1.setTrainnerName("Master Shi");

        when(trainnerDao.findAll()).thenReturn(Collections.singletonList(t1));

        // Act
        Trainner result = searchingService.findTrainnerByName("Master Shi");

        // Assert
        assertNotNull(result, "Trainer should be found");
        assertEquals("Master Shi", result.getTrainnerName());
    }
}