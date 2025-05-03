package models;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class WearableDeviceTest {

    private SmartWatch validDevice;
    private SmartWatch invalidDevice;

    @Before
    public void setUp() {
        Manufacturer validManufacturer = new Manufacturer("FitTech", 100);
        Manufacturer invalidManufacturer = new Manufacturer("ABCDEFGHIJKLMNOPQRSTU", 0);


        validDevice = new SmartWatch("Smart Watch", 199.99, validManufacturer, "12345", "Aluminum", "Medium", "OLED");


        invalidDevice = new SmartWatch("Bad Watch", -1, invalidManufacturer, "12345678901", "MaterialTooLongggggg", "SizeTooLong", "DisplayTooLonggggggg");
    }

    @After
    public void tearDown() {
        validDevice = null;
        invalidDevice = null;
    }

    @Test
    public void testGetMaterial() {
        assertEquals("Aluminum", validDevice.getMaterial());
        assertEquals("MaterialTooLongggggg", invalidDevice.getMaterial());
    }

    @Test
    public void testGetSize() {
        assertEquals("Medium", validDevice.getSize());
        assertEquals("SizeTooLon", invalidDevice.getSize());
    }

    @Test
    public void testSetMaterial() {

        validDevice.setMaterial("Steel");
        assertEquals("Steel", validDevice.getMaterial());


        validDevice.setMaterial("ThisIsAVeryVeryLongMaterialNameThatExceedsTwentyCharacters");
        assertEquals("ThisIsAVeryVeryLongM", validDevice.getMaterial());
    }

    @Test
    public void testSetSize() {

        validDevice.setSize("Large");
        assertEquals("Large", validDevice.getSize());


        validDevice.setSize("ThisSizeIsWayTooLong");
        assertEquals("ThisSizeIs", validDevice.getSize());
    }

}