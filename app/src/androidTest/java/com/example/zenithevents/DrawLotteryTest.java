import static android.graphics.Insets.add;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.core.widget.TextViewCompat;

import com.example.zenithevents.Objects.Event;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class DrawLotteryTest {
    private Event event;

    @Before
    public void setUp() {
        event = new Event();
        event.setEventId("12345");
        event.setEventTitle("Random Event");

        ArrayList<String> waitingList = new ArrayList<>();
        waitingList.add("person1");
        waitingList.add("person2");
        waitingList.add("person3");
        waitingList.add("person4");
        event.setWaitingList(waitingList);

        event.setSelectedLimit(3);
    }

    @Test
    public void drawSampleNolimit() {
        event.setSelectedLimit(0);
        ArrayList<String> selected = event.drawLottery(null);
        assertEquals(4, selected.size());
        assertTrue(selected.contains("person1"));
        assertTrue(selected.contains("person2"));
        assertTrue(selected.contains("person3"));
        assertTrue(selected.contains("person4"));
    }

    @Test
    public void drawSampleLimit() {
        event.setSelectedLimit(2);
        ArrayList<String> selected = event.drawLottery(null);
        assertEquals(2, selected.size());
        assertTrue(selected.contains("person1") || selected.contains("person2"));
        assertTrue(selected.contains("person3") || selected.contains("person4"));

        ArrayList<String> waitingList = event.getWaitingList();
        assertEquals(2, waitingList.size());
    }

    @Test
    public void drawSampleSizeLimit() {
        event.setSelectedLimit(3);
        event.setRegistrants(new ArrayList<String>() {{
            add("person5");
        }});

        ArrayList<String> selected = event.drawLottery(null);
        assertEquals(2, selected.size());
        ArrayList<String> waitingList = event.getWaitingList();
        assertEquals(2, waitingList.size());
    }


}