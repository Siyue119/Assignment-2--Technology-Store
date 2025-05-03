package models;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmartWatchTest {

    private SmartWatch validSmartWatch;
    private SmartWatch invalidSmartWatch;

    @After
    public void tearDown() {validSmartWatch=invalidSmartWatch=null;
    }
    @Before
    public void setUp() {
        Manufacturer validManufacturer = new Manufacturer("Apple", 1000);
        Manufacturer invalidManufacturer = new Manufacturer("Invalid", 0);
        validSmartWatch = new SmartWatch("Series 8", 399.99, validManufacturer, "SomeID", "SomeMaterial", "Large", "AMOLED");
        invalidSmartWatch = new SmartWatch("InvalidModel", 99.99, invalidManufacturer, "InvalidID", "InvalidMaterial", "Small", "LCD");
    }

    @Test
    public void testValidDisplayType() {
        assertEquals("AMOLED", validSmartWatch.getDisplayType());

    }
    @Test
    public void testInvalidDisplayType() {
        assertEquals("LCD", invalidSmartWatch.getDisplayType());
    }

    @Test
    public void testGetValidDisplayType() {
        assertEquals("AMOLED", validSmartWatch.getDisplayType());
    }


    @Test
    public void testSetValidDisplayType() {
        validSmartWatch.setDisplayType("LCD");
        assertEquals("LCD", validSmartWatch.getDisplayType());
    }


    @Test
    public void testGetInsurancePremium_ReturnsSixPercent() {
        double premium = validSmartWatch.getInsurancePremium(399.99);
        assertEquals(399.99 * 0.06, premium, 0.0001);
    }

    @Test
    public void testGetInsurancePremium_NoArgs_ReturnsZero() {
        Manufacturer samsung = new Manufacturer("Samsung", 1200);
        SmartWatch watch = new SmartWatch("Galaxy Watch", 349.99, samsung, "W1", "Plastic", "Medium", "AMOLED");

        double result = watch.getInsurancePremium();

        assertEquals(0, result, 0.01);
    }
    @Test
    public void testConnectToInternet() {
        assertEquals("Connects to the internet via bluetooth", validSmartWatch.connectToInternet());
    }

    @Test
    public void testToString() {
        SmartWatch smartWatch = new SmartWatch("Fitness Watch", 199.99, new Manufacturer("FitTech", 500),
                "SW12345", "Plastic", "Medium", "AMOLED");
        String expected = "Model: Fitness Watch, Price: €199.99, Manufacturer: FitTech, ID: 12345, Material: Plastic, Size: Medium, Display Type: AMOLED";
        assertEquals(expected, smartWatch.toString());
    }}