package com.porvenir.logreader.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.porvenir.logreader.dto.ErrorReport;
import com.porvenir.logreader.service.LogService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyzeLog(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String startDateTime,
            @RequestParam(required = false) String endDateTime) {
        try {
            LocalDateTime start = null;
            LocalDateTime end = null;

            if (startDateTime != null && !startDateTime.isEmpty()) {
                start = LocalDateTime.parse(startDateTime, FORMAT);
            }
            if (endDateTime != null && !endDateTime.isEmpty()) {
                end = LocalDateTime.parse(endDateTime, FORMAT);
            }

            List<ErrorReport> reports = logService.processLogFile(file, start, end);
            long total = reports.stream().mapToLong(ErrorReport::getCount).sum();

            Map<String, Object> response = new HashMap<>();
            response.put("totalErrors", total);
            response.put("details", reports);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}


