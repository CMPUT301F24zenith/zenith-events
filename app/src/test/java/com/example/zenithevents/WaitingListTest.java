package com.example.zenithevents;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import androidx.test.core.app.ActivityScenario;
import com.example.zenithevents.EntrantsList.WaitlistedEntrants;
import com.example.zenithevents.Objects.User;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class WaitingListTest {

    private List<User> testingWaitlistedUsers;

    @Before
    public void startUp() {
        testingWaitlistedUsers = new ArrayList<>();
        testingWaitlistedUsers.add(new User("phone 1", "User", "Fourth", "random@gmail.com", "1234567890"));
        testingWaitlistedUsers.add(new User("phone 2", "User", "Fifth", "random2@gmail.com", "0987654321"));
    }

    @Test
    public void testWaitingPart() {
        ActivityScenario<WaitlistedEntrants> scenario = ActivityScenario.launch(WaitlistedEntrants.class);

        scenario.onActivity(activity -> {
            try {
                Field dataListField = WaitlistedEntrants.class.getDeclaredField("dataList");
                dataListField.setAccessible(true);
                dataListField.set(activity, new ArrayList<>(testingWaitlistedUsers));
                List<User> dataList = (List<User>) dataListField.get(activity);
                assertEquals(2, dataList.size());
                User userOne = dataList.get(0);
                assertEquals("User Fourth", userOne.getFirstName() + " " + userOne.getLastName());
                assertEquals("random@gmail.com", userOne.getEmail());

                User userTwo = dataList.get(1);
                assertEquals("User Fifth", userTwo.getFirstName() + " " + userTwo.getLastName());
                assertEquals("random2@gmail.com", userTwo.getEmail());

                assertNotNull(dataList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
