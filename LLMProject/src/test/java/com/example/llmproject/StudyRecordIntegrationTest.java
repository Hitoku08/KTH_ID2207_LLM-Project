package com.example.llmproject;

import com.example.llmproject.model.StudyRecord;
import com.example.llmproject.repository.StudyRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")  // Loads MySQL test profile
@Transactional
public class StudyRecordIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudyRecordRepository repository;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void testAddStudyRecord() throws Exception {
        mockMvc.perform(post("/addRecord")
                        .param("studentName", "John Doe")
                        .param("subject", "Math")
                        .param("hours", "3")
                        .param("date", "2023-10-01"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/report?studentName=John Doe&yearMonth=*"));

        // 验证数据库中是否成功保存数据
        StudyRecord record = repository.findAll().get(0);
        assert record.getStudentName().equals("John Doe");
        assert record.getSubject().equals("Math");
        assert record.getHours() == 3;
        assert record.getDate().equals(LocalDate.of(2023, 10, 1));
    }

    @Test
    public void testGetMonthlyReport() throws Exception {
        // 先保存一些记录到数据库
        StudyRecord record1 = new StudyRecord();
        record1.setStudentName("John Doe");
        record1.setSubject("Math");
        record1.setHours(3);
        record1.setDate(LocalDate.of(2023, 10, 1));

        StudyRecord record2 = new StudyRecord();
        record2.setStudentName("John Doe");
        record2.setSubject("Science");
        record2.setHours(2);
        record2.setDate(LocalDate.of(2023, 10, 5));

        repository.save(record1);
        repository.save(record2);

        // 发送请求获取月度报告
        mockMvc.perform(get("/report")
                        .param("studentName", "John Doe")
                        .param("yearMonth", "2023-10"))
                .andExpect(status().isOk())
                .andExpect(view().name("report"))
                .andExpect(model().attributeExists("studentName", "yearMonth", "report", "totalHours"))
                .andExpect(model().attribute("totalHours", 5));
    }
}
