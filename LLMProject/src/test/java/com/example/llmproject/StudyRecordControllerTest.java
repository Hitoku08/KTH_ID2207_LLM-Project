package com.example.llmproject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.example.llmproject.controller.StudyRecordController;
import com.example.llmproject.server.StudyRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.YearMonth;
import java.util.Collections;
import java.util.Map;

@WebMvcTest(StudyRecordController.class)
public class StudyRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudyRecordService service;

    @Test
    public void testRecordStudyHours() throws Exception {
        mockMvc.perform(post("/record")
                        .param("studentName", "John Doe")
                        .param("subject", "Math")
                        .param("hours", "2")
                        .param("date", "2023-10-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testGetMonthlyReport() throws Exception {
        when(service.getMonthlyReport(anyString(), any(YearMonth.class)))
                .thenReturn((Map<String, Integer>) Collections.emptyList());

        mockMvc.perform(get("/report")
                        .param("studentName", "John Doe")
                        .param("month", "2023-10"))
                .andExpect(status().isOk())
                .andExpect(view().name("report"))
                .andExpect(model().attributeExists("records"));
    }
}
