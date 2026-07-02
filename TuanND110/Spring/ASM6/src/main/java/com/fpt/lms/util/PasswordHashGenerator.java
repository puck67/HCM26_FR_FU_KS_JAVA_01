package com.fpt.lms.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Developer utility to generate BCrypt hashes for seed data.
 *
 * <p><b>WARNING:</b> This class is for development/seeding purposes ONLY.
 * It should NOT be deployed to production. Run it locally to generate
 * password hashes for {@code data.sql}.
 *
 * <p>Usage:
 * <pre>
 *   mvn exec:java -Dexec.mainClass="com.fpt.lms.util.PasswordHashGenerator"
 * </pre>
 */
public final class PasswordHashGenerator {

    private PasswordHashGenerator() {
        // Utility class — no instantiation
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String[] passwords = args.length > 0 ? args : new String[]{"password", "admin123"};

        for (String password : passwords) {
            String hash = encoder.encode(password);
            System.out.printf("Input:  %-15s%n", password);
            System.out.printf("Hash:   %s%n", hash);
            System.out.printf("Valid:  %s%n%n", encoder.matches(password, hash));
        }
    }
}
