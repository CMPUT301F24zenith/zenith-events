package com.example.zenithevents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.example.zenithevents.Objects.User;

import org.junit.Before;
import org.junit.Test;

public class RemoveUserImageTest {
    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setProfileImageURL("https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o=");
    }

    @Test
    public void testRemoveUserImage() {

        assertNotNull(user.getProfileImageURL());
        assertEquals("https://media.istockphoto.com/id/1300845620/vector/user-icon-flat-isolated-on-white-background-user-symbol-vector-illustration.jpg?s=612x612&w=0&k=20&c=yBeyba0hUkh14_jgv1OKqIH0CCSWU_4ckRkAoy2p73o=", user.getProfileImageURL());
        user.setProfileImageURL(null);
        assertNull(user.getProfileImageURL());

    }
}
