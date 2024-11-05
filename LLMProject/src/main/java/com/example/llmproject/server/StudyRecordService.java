package com.example.llmproject.server;

import com.example.llmproject.model.StudyRecord;
import com.example.llmproject.repository.StudyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudyRecordService {
    @Autowired
    private StudyRecordRepository repository;

    public void saveStudyRecord(StudyRecord studyRecord) {
        repository.save(studyRecord);
    }

    public List<StudyRecord> getMonthlyRecords(String studentName, YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        return repository.findByStudentNameAndDateBetween(studentName, start, end);
    }

    public Map<String, Integer> getMonthlyReport(String studentName, YearMonth yearMonth) {
        List<StudyRecord> records = getMonthlyRecords(studentName, yearMonth);
        return records.stream().collect(Collectors.groupingBy(StudyRecord::getSubject,
                Collectors.summingInt(StudyRecord::getHours)));
    }

    public void saveRecord(StudyRecord record) {
    }
}
