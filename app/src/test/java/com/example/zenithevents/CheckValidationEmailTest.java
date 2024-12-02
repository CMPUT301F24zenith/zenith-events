package com.example.zenithevents;


import com.example.zenithevents.HelperClasses.ValidationUtils;

import org.junit.Test;
import static org.junit.Assert.*;

public class CheckValidationEmailTest {

    @Test
    public void testValidEmails() {
        // Test with various valid email formats
        assertTrue("Expected valid email", ValidationUtils.isValidEmail("ronaldo.name+tag@example.co.uk"));
        assertTrue("Expected valid email", ValidationUtils.isValidEmail("user_name@domain.org"));
        assertTrue("Expected valid email", ValidationUtils.isValidEmail("ronaldo.name123@sub.domain.com"));
        assertTrue("Expected valid email", ValidationUtils.isValidEmail("ronaldo@example.com"));
    }

    @Test
    public void testInvalidEmails() {
        // Test with various invalid email formats
        assertFalse("Expected invalid email", ValidationUtils.isValidEmail("messi"));
        assertFalse("Expected invalid email", ValidationUtils.isValidEmail("missingatsign.com"));
        assertFalse("Expected invalid email", ValidationUtils.isValidEmail("toronto@.com"));
        assertFalse("Expected invalid email", ValidationUtils.isValidEmail("crazy@domain"));
        assertFalse("Expected invalid email", ValidationUtils.isValidEmail("joe@domain.c"));
        assertFalse("Expected invalid email", ValidationUtils.isValidEmail("john@domain,com"));
        assertFalse("Expected invalid email", ValidationUtils.isValidEmail("thatscrazy@domain..com"));
    }

    @Test
    public void testNullAndEmptyEmails() {
        // Test with null and empty strings
        assertFalse("Expected invalid email", ValidationUtils.isValidEmail(null));
        assertFalse("Expected invalid email", ValidationUtils.isValidEmail(""));
    }
}
