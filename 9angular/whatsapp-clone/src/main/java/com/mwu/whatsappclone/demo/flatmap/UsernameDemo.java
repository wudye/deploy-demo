package com.mwu.whatsappclone.demo.flatmap;

import java.util.Optional;

public class UsernameDemo {
    private final String value;

    private UsernameDemo(String value) {
        this.value = value;
    }

    public static Optional<UsernameDemo> of(String raw) {
        if (raw == null || raw.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(new UsernameDemo(raw));
    }

    @Override
    public String toString() {
        return "Username{" + value + "}";
    }
}
