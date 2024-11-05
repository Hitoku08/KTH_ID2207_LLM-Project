package com.example.llmproject.controller;

import com.example.llmproject.model.StudyRecord;
import com.example.llmproject.server.StudyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.Map;

@Controller
public class StudyRecordController {
    @Autowired
    private StudyRecordService service;

    @GetMapping("/addRecord")
    public String showAddRecordForm(Model model) {
        model.addAttribute("studyRecord", new StudyRecord());
        return "addRecord";
    }

    @PostMapping("/addRecord")
    public String addStudyRecord(@ModelAttribute StudyRecord studyRecord) {
        service.saveStudyRecord(studyRecord);
        return "redirect:/report?studentName=" + studyRecord.getStudentName() + "&yearMonth=" + YearMonth.now();
    }

    @GetMapping("/report")
    public String getMonthlyReport(@RequestParam String studentName, @RequestParam String yearMonth, Model model) {
        YearMonth ym = YearMonth.parse(yearMonth);
        Map<String, Integer> report = service.getMonthlyReport(studentName, ym);
        int totalHours = report.values().stream().mapToInt(Integer::intValue).sum();

        model.addAttribute("studentName", studentName);
        model.addAttribute("yearMonth", ym);
        model.addAttribute("report", report);
        model.addAttribute("totalHours", totalHours);

        return "report";
    }
}
