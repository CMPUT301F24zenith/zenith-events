package com.example.zenithevents;

import com.example.zenithevents.Objects.User;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultProfileImageTest {

    private User user;
    private static final String DEFAULT_IMAGE = "default_image_url";


    @Before
    public void startUp() {
        user = new User("deviceId", "John", "Doe", "john@gmail.com", "1234567890");
        user.setProfileImageURL(null);
    }

    @Test
    public void testingDefaultImage() {
        String profileUrl = user.getProfileImageURL() == null ? DEFAULT_IMAGE : user.getProfileImageURL();
        assertEquals("Expected url", DEFAULT_IMAGE, profileUrl);
    }
}


