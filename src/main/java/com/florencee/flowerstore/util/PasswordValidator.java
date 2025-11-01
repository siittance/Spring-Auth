package com.florencee.flowerstore.util;

import java.util.regex.Pattern;

/**
 * Валидатор пароля с требованиями:
 * - Минимальная длина: 6 символов
 * - Должен содержать хотя бы одну заглавную букву
 * - Должен содержать хотя бы одну строчную букву
 * - Должен содержать хотя бы одну цифру
 * - Должен содержать хотя бы один специальный символ (!@#$%^&*()_+-=[]{}|;:,.<>?)
 */
public class PasswordValidator {

    private static final int MIN_LENGTH = 6;
    private static final Pattern HAS_UPPERCASE = Pattern.compile(".*[A-ZА-Я].*");
    private static final Pattern HAS_LOWERCASE = Pattern.compile(".*[a-zа-я].*");
    private static final Pattern HAS_DIGIT = Pattern.compile(".*\\d.*");
    private static final Pattern HAS_SPECIAL = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{}|;:,.<>?].*");

    public static ValidationResult validate(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            return new ValidationResult(false, "Пароль должен содержать минимум " + MIN_LENGTH + " символов");
        }

        if (!HAS_UPPERCASE.matcher(password).matches()) {
            return new ValidationResult(false, "Пароль должен содержать хотя бы одну заглавную букву");
        }

        if (!HAS_LOWERCASE.matcher(password).matches()) {
            return new ValidationResult(false, "Пароль должен содержать хотя бы одну строчную букву");
        }

        if (!HAS_DIGIT.matcher(password).matches()) {
            return new ValidationResult(false, "Пароль должен содержать хотя бы одну цифру");
        }

        if (!HAS_SPECIAL.matcher(password).matches()) {
            return new ValidationResult(false, "Пароль должен содержать хотя бы один специальный символ (!@#$%^&*()_+-=[]{}|;:,.<>?)");
        }

        return new ValidationResult(true, "Пароль валиден");
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}
