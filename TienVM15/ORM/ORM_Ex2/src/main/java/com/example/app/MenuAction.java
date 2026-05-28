package com.example.app;

/**
 * Functional interface representing a named menu action (lambda-compatible).
 */
@FunctionalInterface
public interface MenuAction {
    void execute();
}
