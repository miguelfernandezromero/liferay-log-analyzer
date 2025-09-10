package com.porvenir.logreader.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.porvenir.logreader.dto.ErrorReport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // Regex para capturar fecha y hora al inicio de la línea
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})"
    );

    // Regex para capturar excepciones (ej: NullPointerException, SQLException)
    private static final Pattern EXCEPTION_PATTERN = Pattern.compile("(\\w+Exception)");

    /**
     * Procesa el archivo de log y devuelve un reporte de errores/fallos.
     *
     * @param file archivo .log de entrada
     * @param start fecha/hora inicial (puede ser null)
     * @param end fecha/hora final (puede ser null)
     * @return lista de errores con su contador
     */
    public List<ErrorReport> processLogFile(MultipartFile file,
                                            LocalDateTime start,
                                            LocalDateTime end) throws Exception {
        Map<String, Long> errorCounts = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {

                // Detectar fecha en la línea
                Matcher dateMatcher = DATE_PATTERN.matcher(line);
                LocalDateTime timestamp = null;
                if (dateMatcher.find()) {
                    timestamp = LocalDateTime.parse(dateMatcher.group(1), FORMATTER);
                }

                // Clasificar el error
                String errorType = classifyError(line);

                if (errorType != null) {
                    // Filtrar por rango de fechas si aplica
                    if (timestamp == null ||
                        (start == null || !timestamp.isBefore(start)) &&
                        (end == null || !timestamp.isAfter(end))) {
                        errorCounts.put(errorType, errorCounts.getOrDefault(errorType, 0L) + 1);
                    }
                }
            }
        }

        // Convertir a lista de reportes
        List<ErrorReport> reports = new ArrayList<>();
        errorCounts.forEach((type, count) -> reports.add(new ErrorReport(type, count)));
        return reports;
    }

    /**
     * Clasifica el tipo de error en base al contenido de la línea.
     */
    private String classifyError(String line) {
        // Buscar excepciones
        Matcher exMatcher = EXCEPTION_PATTERN.matcher(line);
        if (exMatcher.find()) {
            return exMatcher.group(1);
        }

        // Timeout
        if (line.toLowerCase().contains("timed out") || line.toLowerCase().contains("timeout")) {
            return "Timeout";
        }

        // Failed
        if (line.toLowerCase().contains("failed")) {
            return "FailedOperation";
        }

        // Unable
        if (line.toLowerCase().contains("unable to")) {
            return "UnableOperation";
        }

        // Cualquier ERROR genérico
        if (line.contains("ERROR")) {
            return "GenericError";
        }

        return null; // no es error relevante
    }
}


