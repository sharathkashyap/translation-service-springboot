package com.translation.config;

public enum TranslationEngine {
    GOOGLE("google"),
    OPENAI("openai"),
    LOCAL("local");

    private final String value;

    TranslationEngine(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TranslationEngine fromValue(String value) {
        for (TranslationEngine engine : TranslationEngine.values()) {
            if (engine.value.equalsIgnoreCase(value)) {
                return engine;
            }
        }
        throw new IllegalArgumentException("Unknown translation engine: " + value);
    }
}
