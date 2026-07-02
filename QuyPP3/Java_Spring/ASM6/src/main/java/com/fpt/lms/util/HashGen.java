package com.fpt.lms.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGen {
    public static void main(String[] args) {
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
        String hash = enc.encode("password");
        System.out.println("BCrypt hash for 'password': " + hash);
        System.out.println("Verify: " + enc.matches("password", hash));
    }
}
