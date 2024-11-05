package com.example.llmproject.repository;

import com.example.llmproject.model.StudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long> {
    List<StudyRecord> findByStudentNameAndDateBetween(String studentName, LocalDate startDate, LocalDate endDate);
}
