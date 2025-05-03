package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SmartBandTest {

    private SmartBand validSmartBand;
    private SmartBand invalidSmartBand;

    @After
    public void tearDown() {
        validSmartBand = invalidSmartBand = null;
    }

    @Before
    public void setUp() {
        Manufacturer manufacturer = new Manufacturer("Apple", 123);
        Manufacturer invalidManufacturer = new Manufacturer("InvalidBrand", 0);
        validSmartBand = new SmartBand("Apple Watch Series 7", 399.99, manufacturer, "00001", "Stainless Steel", "Medium", true);
        invalidSmartBand = new SmartBand("Generic Smart Band", 19.99, invalidManufacturer, "00002", "Plastic", "Small", false);
    }

    @Test
    public void testHasHeartRateMonitor() {
        assertEquals(true, validSmartBand.hasHeartRateMonitor());
        assertEquals(false, invalidSmartBand.hasHeartRateMonitor());
    }

    @Test
    public void testGetInsurancePremium() {
        double validPremium = validSmartBand.getPrice() * 0.07;
        double invalidPremium = invalidSmartBand.getPrice() * 0.07;
        assertEquals(validPremium, validSmartBand.getInsurancePremium(), 0.001);
        assertEquals(invalidPremium, invalidSmartBand.getInsurancePremium(), 0.001);
    }

    @Test
    public void testConnectToInternet() {
        assertEquals("Connects to the internet via Companion App", validSmartBand.connectToInternet());
        assertEquals("Connects to the internet via Companion App", invalidSmartBand.connectToInternet());
    }

    @Test
    public void testToString() {
        String validHeartRateInfo = validSmartBand.hasHeartRateMonitor() ? "Includes Heart Rate Monitor" : "No Heart Rate Monitor included";
        String invalidHeartRateInfo = invalidSmartBand.hasHeartRateMonitor() ? "Includes Heart Rate Monitor" : "No Heart Rate Monitor included";
        String validExpected = "Model Name: Apple Watch Series 7, Price: 399.99, Manufacturer: Apple, ID: 00001, Material: Stainless Steel, Size: Medium, "
                + validHeartRateInfo + ", Insurance Premium: " + validSmartBand.getInsurancePremium() + ", Connects to the internet via Companion App";
        String invalidExpected = "Model Name: Generic Smart Band, Price: 20.0, Manufacturer: InvalidBrand, ID: 00002, Material: Plastic, Size: Small, "
                + invalidHeartRateInfo + ", Insurance Premium: " + invalidSmartBand.getInsurancePremium() + ", Connects to the internet via Companion App";
        assertEquals(validExpected, validSmartBand.toString());
        assertEquals(invalidExpected, invalidSmartBand.toString());
    }

}