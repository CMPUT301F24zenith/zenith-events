package com.example.zenithevents.HelperClasses;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class providing validation methods for different data types.
 *
 * <p>This class contains methods to validate various types of data inputs, such as
 * email addresses. It uses regular expressions to perform the validations.</p>
 *
 * <p><strong>Note:</strong> The JavaDocs for this class were generated using OpenAI's ChatGPT.</p>
 */
public class ValidationUtils {

    /**
     * Validates whether the provided email address is in a valid format.
     *
     * <p>This method uses a regular expression to check if the given email address
     * conforms to a standard email format. The email must contain an "@" symbol,
     * a domain name, and a top-level domain with at least two characters.</p>
     *
     * @param email The email address to validate. Can be a {@code String} that
     *              represents an email.
     * @return {@code true} if the email is valid according to the regular expression,
     *         {@code false} otherwise. If the input is {@code null} or empty,
     *         the method will also return {@code false}.
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+(?<!\\.)\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
