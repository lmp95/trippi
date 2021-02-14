package com.example.trippi;

import android.app.Activity;
import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class BookingTest {

    @Mock
    Context context;

    @Mock
    Activity activity;

    private Booking booking;

    @Test
    public void testCalculateTotalPrice() throws Exception{
        Hotel hotel = new Hotel("1", "image_url", "Hotel Name",
                "Hotel Location", 12f, 34f, 3.5f);
        Room room = new Room("1", "Room Name", 90f);
        Booking localBooking = new Booking("123", hotel, room, "14/2/2021",
                "16/2/2021", true, 0, "user1",
                "Billing Name", "Billing Email");
        assertEquals(215, localBooking.calculateTotalPrice(2, 90f, true), 0);
    }

    @Test
    public void testUpdatePrice() throws Exception {
        Hotel hotel = new Hotel("1", "image_url", "Hotel Name",
                "Hotel Location", 12f, 34f, 3.5f);
        Room room = new Room("1", "Room Name", 90f);
        Booking localBooking = new Booking("123", hotel, room, "14/2/2021",
                "16/2/2021", true, 0, "user1",
                "Billing Name", "Billing Email");
        assertEquals("No extra bed", 180, localBooking.updatePrice(215, false), 0);
        assertEquals("Extra bed included", 215, localBooking.updatePrice(180, true), 0);
    }

}
