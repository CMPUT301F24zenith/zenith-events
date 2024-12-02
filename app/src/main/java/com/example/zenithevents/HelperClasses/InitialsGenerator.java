package com.example.zenithevents.HelperClasses;

/**
 * Utility class for generating initials from a given first name and last name.
 * The JavaDocs for this class were generated using OpenAI's ChatGPT.
 */
public class InitialsGenerator {

    /**
     * Generates the initials from the provided first and last names.
     * The initials are formed by taking the first character of each name, converting them to uppercase, and appending them together.
     *
     * @param firstName The first name from which to generate the initial. Can be null or empty.
     * @param lastName The last name from which to generate the initial. Can be null or empty.
     * @return A string containing the initials in uppercase, or an empty string if both names are null or empty.
     */
    public static String getInitials(String firstName, String lastName) {
        StringBuilder initials = new StringBuilder();

        if (firstName != null && !firstName.isEmpty()) {
            initials.append(Character.toUpperCase(firstName.charAt(0)));
        }

        if (lastName != null && !lastName.isEmpty()) {
            initials.append(Character.toUpperCase(lastName.charAt(0)));
        }

        return initials.toString();
    }
}
