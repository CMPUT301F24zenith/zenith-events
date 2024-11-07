package com.example.zenithevents;

import static org.junit.Assert.assertEquals;

//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zenithevents.Objects.User;

import org.junit.Test;
import org.junit.runner.RunWith;


public class UserObjectTest {

    @Test
    public void testUserObject() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("jdoe@example.com");
        user.setPhoneNumber("1234567890");


        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("jdoe@example.com", user.getEmail());
        assertEquals("1234567890", user.getPhoneNumber());
    }
}
