package com.example.zenithevents;

import com.example.zenithevents.Objects.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserProfileTest {

    private User user;

    @Before
    public void startUp() {
        user = new User("phone1", "Test", "User", "randomuser@gmail.com", "1234567890");

    }

    @Test
    public void testingUser() {
        assertEquals("phone1", user.getDeviceID());
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("randomuser@gmail.com", user.getEmail());
        assertEquals("1234567890", user.getPhoneNumber());
    }

    @Test
    public void testingSetters() {
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@gmail.com");
        user.setPhoneNumber("0987654321");

        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@gmail.com", user.getEmail());
        assertEquals("0987654321", user.getPhoneNumber());
    }


    @Test
    public void testingUploadImage() {
        String sampleImage = "http://example.com/sample-image.png";
        user.setProfileImageURL(sampleImage);
        assertEquals("Profile should have a url", sampleImage, user.getProfileImageURL());
    }

    @Test
    public void testingRemoveImage() {
        String sampleImage = "http://example.com/sample-image.png";
        user.setProfileImageURL(null);
        assertNull("Url should be null", user.getProfileImageURL());
    }

}
