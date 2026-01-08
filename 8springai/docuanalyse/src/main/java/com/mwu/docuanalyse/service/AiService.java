package com.mwu.docuanalyse.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AiService {
    String generateReadingGuide(String apiKey, String content);
    void generateReadingGuideStream(String apiKey, String content, SseEmitter emitter, StringBuilder collector);
}
