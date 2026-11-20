package com.demo.SpringAI.controller;

import com.demo.SpringAI.service.QaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/qa")
@CrossOrigin(origins = "${FRONTEND_URL:http://localhost:5173}")
public class QaController {

    private final QaService qaService;

    // Constructor injection
    public QaController(QaService qaService) {
        this.qaService = qaService;
    }

    // API: POST /api/qa/ask
    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(
            @RequestBody Map<String, String> body) {

        String question = body == null ? null : body.get("question");

        // Validate user input
        if (question == null || question.trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Question cannot be empty."));
        }

        try {
            String answer = qaService.askQuestion(question.trim());

            return ResponseEntity.ok(
                    Map.of("answer", answer)
            );

        } catch (Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "AI service is temporarily unavailable.",
                        "reason", ex.getClass().getSimpleName() + ": " + ex.getMessage()
                ));
    }
    }
}
