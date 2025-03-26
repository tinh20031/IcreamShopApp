package com.example.iceamapp.entity;

import java.util.Collections;
import java.util.List;

public class GeminiRequest {
    private List<Content> contents;

    public GeminiRequest(String message) {
        this.contents = Collections.singletonList(new Content(message));
    }

    public static class Content {
        private List<Part> parts;

        public Content(String message) {
            this.parts = Collections.singletonList(new Part(message));
        }
    }

    public static class Part {
        private String text;

        public Part(String text) {
            this.text = text;
        }
    }
}
