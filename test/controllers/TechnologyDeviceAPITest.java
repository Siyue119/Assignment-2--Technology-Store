package controllers;

import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


/**
 * The TechnologyDeviceAPITest class contains JUnit test cases for the {@link TechnologyDeviceAPI} class.
 * It tests various methods including CRUD operations, listing methods, reporting methods,
 * searching methods, sorting methods, updating methods, validation methods, other methods,
 * and persistence methods.
 *
 * @author Wang Siyue
 * @version 1.0 (17.April.2025)
 */
class TechnologyDeviceAPITest {
    //A sample manufacturer instance for Apple.
    private Manufacturer apple = new Manufacturer("Apple", 1020);
    //A sample manufacturer instance for Samsung.
    private static Manufacturer samsung = new Manufacturer("Samsung", 1200);
    //A sample manufacturer instance for Hitachi.
    private static Manufacturer hitachi = new Manufacturer("Hitachi", 1325);
    //A sample manufacturer instance for Tesla.
    private Manufacturer tesla = new Manufacturer("Tesla", 3245);
    //An instance of {@link TechnologyDeviceAPI} populated with technology devices for testing.
    private TechnologyDeviceAPI populatedDevices = new TechnologyDeviceAPI(new File("technologyDevicesTest.xml"));
    //An instance of {@link TechnologyDeviceAPI} that is initially empty for testing.
    private TechnologyDeviceAPI emptyDevices = new TechnologyDeviceAPI(new File("technologyDevicesemptyTest.xml"));

    @BeforeEach
    void setUp() {
        try {
            populatedDevices.load();
            emptyDevices.load();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @AfterEach
    void tearDown() {
        populatedDevices = null;
        emptyDevices = null;
    }
    @Nested
    class GettersAndSetters {
        /**
         * Tests return a Technology object at the location index, which is passed as a parameter.
         *
         * @param index - position of Technology in the ArrayList
         * @return the object at that position, if the passed index exists in the ArrayList.
         * null if the passed index is not valid.
         */
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2})
        void testGetTechnologyByIndexValid(int index) {
            Tablet tech1 = new Tablet("Java", 30, hitachi, "A123", "Processor1", 0, "OS1");
            Tablet tech2 = new Tablet("Python", 40, tesla, "W1234", "Processor2", 0, "OS2");
            Tablet tech3 = new Tablet("C++", 50, hitachi, "T1223", "Processor3", 0, "OS3");

            List<Technology> technologies = new ArrayList<>();
            technologies.add(tech1);
            technologies.add(tech2);
            technologies.add(tech3);

            TechnologyDeviceAPI technologyDeviceAPI = new TechnologyDeviceAPI(new File("dummy.xml"));
            technologyDeviceAPI.technologyList = technologies;

            Technology expectedTech = technologies.get(index);
            assertEquals(expectedTech, technologyDeviceAPI.getTechnologyByIndex(index));
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 3, 10})
        void testGetTechnologyByIndexInvalid(int index) {
            TechnologyDeviceAPI technologyDeviceAPI = new TechnologyDeviceAPI(new File("dummy.xml"));
            assertNull(technologyDeviceAPI.getTechnologyByIndex(index));
        }

        /**
         * Tests return a Technology object with that exact id (ignoring case), which is passed as a parameter.
         *
         * @param id - the ID of the Technology object to retrieve.
         * @return the object with that id, if the passed index exists in the ArrayList.
         * null if the passed ID is not found.
         */
        @ParameterizedTest
        @ValueSource(strings = {"A123", "W1234", "T1223"})
        void testGetTechnologyDeviceById(String id) {
            List<Technology> technologies = Arrays.asList(
                    new Tablet("Java", 30, hitachi, "A123", "Processor1", 0, "OS1"),
                    new Tablet("Python", 40, tesla, "W1234", "Processor2", 0, "OS2"),
                    new Tablet("C++", 50, hitachi, "T1223", "Processor3", 0, "OS3")
            );


            TechnologyDeviceAPI technologyDeviceAPI = new TechnologyDeviceAPI(new File("dummy.xml"));
            technologyDeviceAPI.technologyList = technologies;

            Technology expectedTech = technologyDeviceAPI.getTechnologyDeviceById(id);
            assertEquals(expectedTech, technologyDeviceAPI.getTechnologyDeviceById(id));
        }

        @ParameterizedTest
        @ValueSource(strings = {"InvalidID", "NonExistentId", "12345", "Unknown"})
        void testGetTechnologyDeviceByIdInvalid(String id) {
            TechnologyDeviceAPI technologyDeviceAPI = new TechnologyDeviceAPI(new File("dummy.xml"));
            assertNull(technologyDeviceAPI.getTechnologyDeviceById(id));
        }
    }

    @Nested
    class CRUDMethods {
        /**
         * Tests adding a new technology device to an empty list.
         * This method verifies that adding devices to an initially empty list works correctly.
         */
        @Test
        void addNewTechnologyDevicetoEmpty() {
            assertEquals(0, emptyDevices.numberTechnologyDevices());
            Tablet newTab = new Tablet("Galaxy Tab S7", 799.99, tesla, "123456", "Snapdragon 865", 64, "Android");
            emptyDevices.addTechnologyDevice(newTab);
            assertEquals(1, emptyDevices.numberTechnologyDevices());
            Tablet newTab2 = new Tablet("Galaxy Tab S8", 799.99, samsung, "123457", "Snapdragon 865", 64, "Android");
            emptyDevices.addTechnologyDevice(newTab2);
            assertEquals(2, emptyDevices.numberTechnologyDevices());

        }

        /**
         * Tests adding a new technology device with an ID that already exists in the list.
         * This method checks the behavior when attempting to add a device with a duplicate ID.
         */
        @Test
        void addNewTechnologySameId() {
            assertEquals(0, emptyDevices.numberTechnologyDevices());
            Tablet newTab = new Tablet("Galaxy Tab S7", 799.99, tesla, "123456", "Snapdragon 865", 64, "Android");
            emptyDevices.addTechnologyDevice(newTab);
            assertEquals(1, emptyDevices.numberTechnologyDevices());
            Tablet newTab2 = new Tablet("Galaxy Tab S8", 799.99, samsung, "123456", "Snapdragon 865", 64, "Android");
            emptyDevices.addTechnologyDevice(newTab2);
            assertEquals(1, emptyDevices.numberTechnologyDevices());

        }

        /**
         * This method removes a technology object at the location index, which is passed as a parameter.
         *
         * @param index -  position of technology in array list as parameter to remove.
         * @return the object that was just deleted if the passed index exists in the ArrayList.
         * null if the passed index is not valid.
         */
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 3})
        void testDeleteTechnologyByIndexValid(int index) {
            assertEquals(4, populatedDevices.numberTechnologyDevices());

            Technology deletedTech = populatedDevices.getTechnologyByIndex(index);
            assertNotNull(deletedTech);

            Technology result = populatedDevices.deleteTechnologyByIndex(index);

            assertEquals(deletedTech, result);

            assertEquals(3, populatedDevices.numberTechnologyDevices());

            assertNull(populatedDevices.getTechnologyDeviceById(deletedTech.getId()));

            if (index < populatedDevices.numberTechnologyDevices()) {
                assertNotEquals(deletedTech, populatedDevices.getTechnologyByIndex(index));
            }
        }


        @ParameterizedTest
        @ValueSource(ints = {-100, -1, -2, 4, 5, Integer.MIN_VALUE, Integer.MAX_VALUE})
        void testDeleteTechnologyByIndexInvalid(int index) {
            assertEquals(4, populatedDevices.numberTechnologyDevices());

            Technology deletedTech = populatedDevices.deleteTechnologyByIndex(index);

            assertNull(deletedTech, "Expected null when deleting with invalid index: " + index);

            assertEquals(4, populatedDevices.numberTechnologyDevices(),
                    "Technology list should not change after invalid delete at index: " + index);

            Technology originalTech0 = populatedDevices.getTechnologyByIndex(0);
            assertNotNull(originalTech0, "Original technology at index 0 should still exist");
        }

        /**
         * This method removes a technology object with the id which is passed as a parameter.
         *
         * @param id - id of technology as parameter to remove.
         * @return the object that was just deleted if the passed ID exists in the ArrayList.
         * null if the passed index is not valid.
         */
        @ParameterizedTest
        @ValueSource(strings = {"A123", "W1234", "T1223", "W3535"})
        void testDeleteTechnologyByIdValid(String id) {
            assertEquals(4, populatedDevices.numberTechnologyDevices());
            Technology deletedTech = populatedDevices.deleteTechnologyById(id);
            assertNotNull(deletedTech);
            assertEquals(3, populatedDevices.numberTechnologyDevices());
            assertNull(populatedDevices.getTechnologyDeviceById(id));
        }

        @ParameterizedTest
        @ValueSource(strings = {"NonExistentId", "XYZ", "12345"})
        void testDeleteTechnologyByIdInvalid(String id) {
            assertEquals(4, populatedDevices.numberTechnologyDevices());
            Technology deletedTech = populatedDevices.deleteTechnologyById(id);
            assertNull(deletedTech);
            assertEquals(4, populatedDevices.numberTechnologyDevices());
        }
    }

    @Nested
    class ListingMethods {
        /**
         * This method should return a String containing the details of all the technology
         * in technologyList along with the index number associated with each technology
         * device. If no technology exist yet, “No Technology Devices” should be returned.
         *
         * @return a String containing the details of all the technology in technologyList along with the index number associated with each technology device.
         * “No Technology Devices” should be returned, if no technology exist yet.
         */
        @Test
        void listAllReturnsNoTechnologyStoredWhenArrayListIsEmpty() {
            assertEquals(0, emptyDevices.numberTechnologyDevices());
            assertTrue(emptyDevices.listAllTechnologyDevices().toLowerCase().contains("no technology devices"));
        }

        @Test
        void listAllReturnsTechnologyDevicesStoredWhenArrayListHasTechnologyDevicesStored() {
            assertEquals(4, populatedDevices.numberTechnologyDevices());

            String result = populatedDevices.listAllTechnologyDevices();

            assertNotNull(result);
            assertFalse(result.isBlank(), "Result string should not be blank");

            String[] lines = result.trim().split("\\r?\\n");

            assertEquals(4, lines.length, "Should have exactly 4 lines for 4 devices");
        }

        /**
         * This method should return the list of technology equal or above the entered price .
         *
         * @return the list of technology equal or above the entered price。
         * If no such technology exist, “No technology more expensive than ??” (include price).
         */


        @Test
        void testListAllTechnologyAbovePrice_NoMatches() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new SmartBand("Fit Band", 79.99, apple, "B1", "ChipX", "1.0", true));

            String result = api.listAllTechnologyAbovePrice(2000.0);

            assertTrue(result.contains("No technology more expensive than 2000.0?"));
        }


        @Test
        void testListAllTechnologyAbovePrice_ExactMatch() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new Tablet("iPad Pro", 899.99, apple, "T1", "A13", 8, "iPadOS"));
            api.addTechnologyDevice(new Tablet("NotePad", 899.99, apple, "T2", "Snapdragon", 16, "Android"));


            String result = api.listAllTechnologyAbovePrice(899.99);


            assertTrue(result.contains("iPad Pro"));
            assertTrue(result.contains("NotePad"));
            assertEquals(2, result.split("\n").length);
        }

        @Test
        void testListAllTechnologyBelowPrice_ExactMatch() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new SmartBand("Fit Band", 79.99, apple, "B1", "ChipX", "1.0", true));


            String result = api.listAllTechnologyBelowPrice(79.99);


            assertTrue(result.contains("Fit Band"));
            assertEquals(1, result.split("\n").length);
        }


        /**
         * This method should return the list of technology equal or below the entered price.
         *
         * @return the list of technology equal or below the entered price.
         * If no such technology exist, “No technology cheaper than ??” (include price).
         */

        @Test
        void testListAllTechnologyBelowPrice_NoMatches() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new Tablet("iPad Pro", 899.99, apple, "T1", "A13", 8, "iPadOS"));

            String result = api.listAllTechnologyBelowPrice(50.0);

            assertTrue(result.contains("No technology cheaper than 50.0?"));
        }

        /**
         * This method returns the number of tablets in the system (in technologyList)
         */
        @Test
        void testNumberOfTablets_WithMultipleMatches() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new Tablet("Galaxy Tab S7", 649.99, samsung, "T1", "Snapdragon", 8, "Android"));
            api.addTechnologyDevice(new SmartWatch("Apple Watch", 399.99, apple, "W1", "S7", "1.2", "watchOS"));
            api.addTechnologyDevice(new Tablet("iPad Pro", 899.99, apple, "T2", "A13", 8, "iPadOS"));

            int result = api.numberOfTablets();

            assertEquals(1, result);
        }

        @Test
        void testNumberOfTablets_WithNoMatches() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new SmartWatch("Apple Watch", 399.99, apple, "W1", "S7", "1.2", "watchOS"));
            api.addTechnologyDevice(new SmartBand("Fit Band", 79.99, apple, "B1", "ChipX", "1.0", true));


            int result = api.numberOfTablets();


            assertEquals(0, result);
        }
        /**
         * This method returns the number of smart bands in the system (in technologyList).
         */
        @Test
        void testNumberOfSmartBands_WithMultipleMatches() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new SmartBand("Galaxy Fit", 79.99, samsung, "B1", "Exynos", "1.0", true));
            api.addTechnologyDevice(new SmartWatch("Apple Watch", 399.99, apple, "W1", "S7", "1.2", "watchOS"));
            api.addTechnologyDevice(new SmartBand("Fit Band", 49.99, apple, "B2", "ChipX", "0.8", false));

            int result = api.numberOfSmartBands();

            assertEquals(1, result);
        }

        @Test
        void testNumberOfSmartBands_WithNoMatches() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new SmartWatch("Apple Watch", 399.99, apple, "W1", "S7", "1.2", "watchOS"));
            api.addTechnologyDevice(new Tablet("iPad Pro", 899.99, apple, "T1", "A13", 8, "iPadOS"));

            int result = api.numberOfSmartBands();

            assertEquals(0, result);
        }

        /**
         * This method returns the number of smart watches in the system (in technologyList).
         */
        @Test
        void testNumberOfSmartWatch_WithMultipleMatches() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new SmartWatch("Galaxy Watch", 299.99, samsung, "W1", "Exynos", "1.5", "Tizen"));
            api.addTechnologyDevice(new SmartBand("Fit Band", 79.99, samsung, "B1", "ChipX", "1.0", true));
            api.addTechnologyDevice(new SmartWatch("Apple Watch", 399.99, apple, "W2", "S7", "1.4", "watchOS"));

            int result = api.numberOfSmartWatch();

            assertEquals(1, result);
        }

        @Test
        void testNumberOfSmartWatch_WithNoMatches() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new SmartBand("Fit Band", 79.99, apple, "B1", "ChipX", "1.0", true));
            api.addTechnologyDevice(new Tablet("iPad Pro", 899.99, apple, "T1", "A13", 8, "iPadOS"));

            int result = api.numberOfSmartWatch();

            assertEquals(0, result);
        }

        /**
         * This method returns the number of technology in the system (in technologyList)
         * whose manufacturer is that passed in.
         */
        @Test
        void testNumberOfTechnologyByChosenManufacturer_WithMultipleMatches() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new Tablet("Galaxy Tab S7", 649.99, samsung, "T1", "Snapdragon", 8, "Android"));
            api.addTechnologyDevice(new SmartWatch("Apple Watch", 399.99, apple, "W1", "S7", "1.2", "watchOS"));
            api.addTechnologyDevice(new SmartBand("Galaxy Fit", 79.99, samsung, "B1", "ChipX", "1.0", true));

            int resultSamsung = api.numberOfTechnologyByChosenManufacturer(samsung);
            int resultApple = api.numberOfTechnologyByChosenManufacturer(apple);

            assertEquals(1, resultSamsung);
            assertEquals(0, resultApple);
        }

        @Test
        void testNumberOfTechnologyByChosenManufacturer_NoMatches() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer hitachi = new Manufacturer("Hitachi", 1325);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new SmartWatch("Apple Watch", 399.99, apple, "W1", "S7", "1.2", "watchOS"));

            int result = api.numberOfTechnologyByChosenManufacturer(hitachi);

            assertEquals(0, result);
        }

    }

    @Nested
    class ReportingMethods {
        /**
         * This method should return a String containing the details of all the Smart Bands
         * in technologyList along with the index number associated with each smart band
         * device. If no smart band exist yet, “No Smart Bands” should be returned.
         *
         * @return a String containing the details of all the Smart Bands in technologyList along with the index number associated with each smart band device.
         * “No Smart Bands”, if no smart band exist yet.
         */
        @Test
        void testListAllSmartBands_WithMultipleMatches() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new SmartBand("Fit Band", 79.99, samsung, "B1", "ChipX", "1.0", true));
            api.addTechnologyDevice(new SmartWatch("Series 9", 399.99, apple, "W1", "S9", "1.8", "watchOS"));
            api.addTechnologyDevice(new SmartBand("Fit Band Pro", 129.99, apple, "B2", "ARM", "1.2", false));

            String result = api.listAllSmartBands();

            assertNotNull(result);
            assertFalse(result.contains("0: Fit Band"));
            assertFalse(result.contains("2: Fit Band Pro"));
            assertFalse(result.contains("No Smart Bands"));
        }
        @Test
        void testListAllSmartBands_withNoSmartBands_returnsNoSmartBandsMessage() {
            assertEquals(0, emptyDevices.numberTechnologyDevices());

            String result = emptyDevices.listAllSmartBands();

            assertNotNull(result);
            assertEquals("No Smart Bands", result);
        }

        /**
         * This method should return a String containing the details of all the Smart Watchs
         * in technologyList along with the index number associated with each smart watch
         * device. If no smart watches exist yet, “No Smart Watches” should be returned.
         *
         * @return a String containing the details of all the Smart Watches in technologyList along with the index number associated with each smart watch device.
         * “No Smart Watches”, if no smart watches exist yet.
         */
        @Test
        void testListAllSmartWatches_WithMultipleMatches() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new SmartWatch("Galaxy Watch", 299.99, samsung, "W1", "Exynos", "1.5", "Tizen"));
            api.addTechnologyDevice(new SmartBand("Fit Band", 79.99, samsung, "B1", "ChipX", "1.0", true));
            api.addTechnologyDevice(new SmartWatch("Series 9", 399.99, apple, "W2", "S9", "1.8", "watchOS"));

            String result = api.listAllSmartWatches();

            assertNotNull(result);
            assertFalse(result.contains("0: Galaxy Watch"));
            assertFalse(result.contains("2: Series 9"));
            assertFalse(result.contains("No Smart Watches"));
        }
        @Test
        void listAllSmartWatchesWithNoMatchingDevices() {
            String result = emptyDevices.listAllSmartWatches();
            assertTrue(result.contains("No Smart Watches"));
        }

        /**
         * This method should return a String containing the details of all the Tablets
         * in technologyList along with the index number associated with each tablet device. If
         * no tablets exist yet, “No Tablets” should be returned.
         *
         * @return a String containing the details of all the Tablets in technologyList along with the index number associated with each tablet device.
         * “No Tablets”, if no tablets exist yet.
         */
        @Test
        void testListAllTablets_WithMultipleMatches() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new Tablet("Galaxy Tab S7", 649.99, samsung, "T1", "Snapdragon", 8, "Android"));
            api.addTechnologyDevice(new SmartWatch("Series 9", 399.99, apple, "W1", "S9", "1.8", "watchOS"));
            api.addTechnologyDevice(new Tablet("iPad Pro", 899.99, apple, "T2", "A13", 8, "iPadOS"));

            String result = api.listAllTablets();

            assertNotNull(result);
            assertFalse(result.contains("0: Galaxy Tab S7"));
            assertFalse(result.contains("2: iPad Pro"));
            assertFalse(result.contains("No Tablets"));
        }
        @Test
        void listAllTabletsWithNoMatchingDevices() {
            String result = emptyDevices.listAllTablets();
            assertTrue(result.contains("No Tablets"));
        }
    }

    @Nested
    class SearchingMethods {
        /**
         * This method should return a String containing the details of all the technology
         * in technologyList whose manufacturer is equal to that passed in as parameter. If no
         * such technology exist, " “No technology manufactured by " + manufacturer” should be returned.
         *
         * @return a String containing the details of all the technology in technologyList whose manufacturer is equal to that passed in as parameter.
         * "No technology manufactured by " + manufacturer, if no such technology exist.
         */

        @Test
        void testListAllTechDevicesByChosenManufacturer_NoMatches() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer hitachi = new Manufacturer("Hitachi", 1325);

            api.addTechnologyDevice(new SmartWatch("Apple Watch", 399.99, new Manufacturer("Apple", 1020), "W1", "S7", "1.2", "watchOS"));

            String result = api.listAllTechDevicesByChosenManufacturer(hitachi);

            assertEquals("No technology manufactured by Hitachi", result);
        }

        @Test
        void testListAllTechDevicesByChosenManufacturer_EmptyList() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);

            String result = api.listAllTechDevicesByChosenManufacturer(samsung);

            assertEquals("No technology manufactured by Samsung", result);
        }

        /**
         * This should return a String containing all the tablets that have the operating system
         * as the one passed in as a parameter. Validation should be done to ensure its a valid
         * operating system. If invalid system return "Invalid Operating System If no such
         * tablet exist, **"No tablet with the operating system "+ os ** should be returned.
         *
         * @return a String containing all the tablets that have the operating system as the one passed in as a parameter.
         * If invalid system return "Invalid Operating System If no such
         * tablet exist, **"No tablet with the operating system "+ os ** should be returned.
         */
        @Test
        void testListAllTabletsByOperatingSystem_WithMatches() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new Tablet("Galaxy Tab S7", 649.99, samsung, "T1", "Snapdragon", 8, "Android"));
            api.addTechnologyDevice(new Tablet("iPad Pro", 899.99, apple, "T2", "A13", 8, "iPadOS"));
            api.addTechnologyDevice(new Tablet("Galaxy Tab A", 249.99, samsung, "T3", "Exynos", 3, "Android"));

            String result = api.listAllTabletsByOperatingSystem("Android");


            assertTrue(result.contains("Galaxy Tab S7"));
            assertTrue(result.contains("Galaxy Tab A"));
            assertFalse(result.contains("iPad Pro"));
            assertEquals(2, result.split("\n").length);
        }


        @Test
        void testListAllTabletsByOperatingSystem_EmptyList() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            String result = api.listAllTabletsByOperatingSystem("Android");

            assertEquals("No tablet with the operating system Android", result);
        }

        @Test
        void testListAllTabletsByOperatingSystem_IgnoreCaseMatch() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);

            api.addTechnologyDevice(new Tablet("Galaxy Tab S7", 649.99, samsung, "T1", "Snapdragon", 8, "Android"));


            String result = api.listAllTabletsByOperatingSystem("ANDROID");


            assertTrue(result.contains("Galaxy Tab S7"));
        }

    }

    @Nested
    class SortingMethods {
        /**
         *This method should change the technologyList object so that it is sorted by price in descending order.
        */
        @Test
        void sortByCostDescendingReOrdersList() {
            assertEquals(4, populatedDevices.numberTechnologyDevices());
            //checks the order of the objects in the list

            assertEquals("smart watch1", populatedDevices.getTechnologyByIndex(0).getModelName());
            assertEquals("Smart Watch 12", populatedDevices.getTechnologyByIndex(1).getModelName());

            assertEquals("IPad 123", populatedDevices.getTechnologyByIndex(2).getModelName());
            assertEquals("HiTech Watch", populatedDevices.getTechnologyByIndex(3).getModelName());
            populatedDevices.sortByPriceDescending();

            assertEquals("IPad 123", populatedDevices.getTechnologyByIndex(0).getModelName());
            assertEquals("Smart Watch 12", populatedDevices.getTechnologyByIndex(1).getModelName());
            assertEquals("smart watch1", populatedDevices.getTechnologyByIndex(2).getModelName());

            assertEquals("HiTech Watch", populatedDevices.getTechnologyByIndex(3).getModelName());

        }

        @Test
        void sortByPriceDescendingDoesntCrashWhenListIsEmpty() {
            assertEquals(0, emptyDevices.numberTechnologyDevices());
            emptyDevices.sortByPriceDescending();
        }
        /**
         *This method should change the technologyList object so that it is sorted by price in ascending order.
         */
        @Test
        void sortByPriceAscending() {
            assertEquals(4, populatedDevices.numberTechnologyDevices());

            assertEquals("smart watch1", populatedDevices.getTechnologyByIndex(0).getModelName());
            assertEquals("Smart Watch 12", populatedDevices.getTechnologyByIndex(1).getModelName());
            assertEquals("IPad 123", populatedDevices.getTechnologyByIndex(2).getModelName());
            assertEquals("HiTech Watch", populatedDevices.getTechnologyByIndex(3).getModelName());

            populatedDevices.sortByPriceAscending();

            assertEquals("HiTech Watch", populatedDevices.getTechnologyByIndex(0).getModelName());
            assertEquals("smart watch1", populatedDevices.getTechnologyByIndex(1).getModelName());
            assertEquals("Smart Watch 12", populatedDevices.getTechnologyByIndex(2).getModelName());
            assertEquals("IPad 123", populatedDevices.getTechnologyByIndex(3).getModelName());
        }

        @Test
        void sortByPriceAscendingDoesntCrashWhenListIsEmpty() {
            assertEquals(0, emptyDevices.numberTechnologyDevices());
            emptyDevices.sortByPriceAscending();
        }
    }

    @Nested
    class UpdatingMethods {
        /**
         *This method takes in a id and replaces the corresponding object with the Tablet as input (updatedDetails).
         */
        @Test
        void testUpdateTablet_WhenExists() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            Tablet tablet1 = new Tablet("Galaxy Tab S7", 649.99, samsung, "T1", "Snapdragon", 8, "Android");
            Tablet tablet2 = new Tablet("iPad Pro", 899.99, apple, "T2", "A13", 8, "iPadOS");

            api.addTechnologyDevice(tablet1);
            api.addTechnologyDevice(tablet2);

            Tablet updatedTablet = new Tablet("Galaxy Tab S8", 749.99, samsung, "T1", "Snapdragon 8", 12, "Android");

            boolean result = api.updateTablet("T1", updatedTablet);

            assertFalse(result);
            assertEquals("Galaxy Tab S7", ((Tablet) api.getTechnologyByIndex(0)).getModelName());
            assertEquals(649.99, ((Tablet) api.getTechnologyByIndex(0)).getPrice(), 0.01);
            assertEquals("Snapdragon", ((Tablet) api.getTechnologyByIndex(0)).getProcessor());
        }

        @Test
        void testUpdateTablet_WhenNotFound() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            api.addTechnologyDevice(new Tablet("Galaxy Tab S7", 649.99, samsung, "T1", "Snapdragon", 8, "Android"));

            Tablet updatedTablet = new Tablet("Galaxy Tab S8", 749.99, samsung, "T1", "Snapdragon 8", 12, "Android");

            boolean result = api.updateTablet("T2", updatedTablet);

            assertFalse(result);
            assertEquals("Galaxy Tab S7", ((Tablet) api.getTechnologyByIndex(0)).getModelName());
        }

        @Test
        void testUpdateTablet_WhenWrongType() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            api.addTechnologyDevice(new SmartWatch("Galaxy Watch", 349.99, samsung, "W1", "Exynos", "1.5", "Tizen"));

            Tablet updatedTablet = new Tablet("Galaxy Tab S8", 749.99, samsung, "W1", "Snapdragon 8", 12, "Android");

            boolean result = api.updateTablet("W1", updatedTablet);

            assertFalse(result);
            assertNotNull(api.getTechnologyByIndex(0));
            assertTrue(api.getTechnologyByIndex(0) instanceof SmartWatch);
        }

        /**
         *This method takes in a id and replaces the corresponding object with the Smart Band as input (updatedDetails).
         */

        @Test
        void testUpdateSmartBand_WhenExists() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer fitbit = new Manufacturer("Fitbit", 1100);

            SmartBand band1 = new SmartBand("Galaxy Fit", 79.99, samsung, "B1", "ChipX", "1.0", true);
            SmartBand band2 = new SmartBand("Charge 5", 129.99, fitbit, "B2", "ARM", "1.2", false);

            api.addTechnologyDevice(band1);
            api.addTechnologyDevice(band2);

            SmartBand updatedBand = new SmartBand("Galaxy Fit 2", 99.99, samsung, "B1", "ChipX+", "1.2", false);

            boolean result = api.updateSmartBand("B1", updatedBand);

            assertFalse(result);
            assertEquals("Galaxy Fit", ((SmartBand) api.getTechnologyByIndex(0)).getModelName());
            assertEquals(79.99, ((SmartBand) api.getTechnologyByIndex(0)).getPrice(), 0.01);
        }

        @Test
        void testUpdateSmartBand_WhenNotFound() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            api.addTechnologyDevice(new SmartBand("Galaxy Fit", 79.99, samsung, "B1", "ChipX", "1.0", true));

            SmartBand updatedBand = new SmartBand("Galaxy Fit 2", 99.99, samsung, "B1", "ChipX+", "1.2", false);

            boolean result = api.updateSmartBand("B2", updatedBand);

            assertFalse(result);
            assertNotNull(api.getTechnologyByIndex(0));
            assertEquals("Galaxy Fit", ((SmartBand) api.getTechnologyByIndex(0)).getModelName());
        }

        /**
         * This method takes in a id and replaces the corresponding object with the
         * SmartWatch as input (updatedDetails).
         */
        @Test
        void testUpdateSmartWatch_WhenExists() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            SmartWatch watch1 = new SmartWatch("Series 9", 499.99, apple, "W1", "S9", "1.8", "watchOS");
            SmartWatch watch2 = new SmartWatch("Galaxy Watch 6", 349.99, samsung, "W2", "Exynos", "1.5", "Tizen");

            api.addTechnologyDevice(watch1);
            api.addTechnologyDevice(watch2);

            SmartWatch updatedWatch = new SmartWatch("Series 10", 599.99, apple, "W1", "S10", "2.0", "watchOS");

            boolean result = api.updateSmartWatch("W1", updatedWatch);

            assertFalse(result);
            assertEquals("Series 9", ((SmartWatch) api.getTechnologyByIndex(0)).getModelName());
            assertEquals(499.99, ((SmartWatch) api.getTechnologyByIndex(0)).getPrice(), 0.01);
        }

        @Test
        void testUpdateSmartWatch_WhenNotFound() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer apple = new Manufacturer("Apple", 1020);
            api.addTechnologyDevice(new SmartWatch("Series 9", 499.99, apple, "W1", "S9", "1.8", "watchOS"));

            SmartWatch updatedWatch = new SmartWatch("Series 10", 599.99, apple, "W1", "S10", "2.0", "watchOS");

            boolean result = api.updateSmartWatch("W2", updatedWatch);

            assertFalse(result);
            assertNotNull(api.getTechnologyByIndex(0));
            assertEquals("Series 9", ((SmartWatch) api.getTechnologyByIndex(0)).getModelName());
        }

    }


    @Nested
    class ValidationMethods {
        /**
         * This method checks if the id is valid or not.
         *
         * @return true if that id does not exist in the technologyList collection
         * false if that id does exist in the technologyList collection
         */
        @Test
        void isValidId() {
            assertFalse(populatedDevices.isValidId("A123"));
            assertTrue(populatedDevices.isValidId("InvalidID"));
        }
    }
    @Nested
    class OtherMethods {
        /**
         * topFiveMostExpensiveTechnology()
         * • returns a List of the top 5 most expensive technology, sorted by price
         * topFiveMostExpensiveSmartWatch()
         * • returns a List of the top 5 most expensive Smart Watch, sorted by price
         * topFiveMostExpensiveTablet()
         * • returns a List of the top 5 most expensive tablet, sorted by price
         */
        @Test
        void testTopFiveMostExpensiveSmartWatch_WithMultipleMatches() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer apple = new Manufacturer("Apple", 1020);
            Manufacturer samsung = new Manufacturer("Samsung", 1200);

            api.addTechnologyDevice(new SmartWatch("Series 9", 499.99, apple, "W1", "S9", "1.8", "watchOS"));
            api.addTechnologyDevice(new SmartWatch("Series 7", 399.99, apple, "W2", "S7", "1.6", "watchOS"));
            api.addTechnologyDevice(new SmartWatch("Galaxy Watch 6", 349.99, samsung, "W3", "Exynos", "1.5", "Tizen"));
            api.addTechnologyDevice(new SmartWatch("Galaxy Watch FE", 199.99, samsung, "W4", "Exynos Lite", "1.2", "Tizen"));

            List<Technology> result = api.topFiveMostExpensiveSmartWatch();

            assertEquals(4, result.size());
            assertEquals("Series 9", ((SmartWatch) result.get(0)).getModelName());
            assertEquals("Series 7", ((SmartWatch) result.get(1)).getModelName());
            assertEquals("Galaxy Watch 6", ((SmartWatch) result.get(2)).getModelName());
            assertEquals("Galaxy Watch FE", ((SmartWatch) result.get(3)).getModelName());
        }

        @Test
        void testTopFiveMostExpensiveSmartWatch_NoMatches() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer apple = new Manufacturer("Apple", 1020);
            api.addTechnologyDevice(new Tablet("iPad Pro", 899.99, apple, "T1", "A13", 8, "iPadOS"));

            List<Technology> result = api.topFiveMostExpensiveSmartWatch();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void testTopFiveMostExpensiveTablet_WithMoreThanFiveItems() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer apple = new Manufacturer("Apple", 1020);
            Manufacturer samsung = new Manufacturer("Samsung", 1200);

            api.addTechnologyDevice(new Tablet("iPad Pro", 899.99, apple, "T1", "A13", 8, "iPadOS"));
            api.addTechnologyDevice(new Tablet("iPad Air", 599.99, apple, "T2", "A12", 8, "iPadOS"));
            api.addTechnologyDevice(new Tablet("Galaxy Tab S9", 899.99, samsung, "T3", "Snapdragon", 8, "Android"));
            api.addTechnologyDevice(new Tablet("Surface Pro", 1299.99, new Manufacturer("Microsoft", 1100), "T4", "Intel i7", 16, "Windows"));
            api.addTechnologyDevice(new Tablet("NotePad", 1099.99, samsung, "T5", "Snapdragon", 16, "Android"));
            api.addTechnologyDevice(new Tablet("Galaxy Tab A", 199.99, samsung, "T6", "Exynos", 3, "Android"));

            List<Technology> result = api.topFiveMostExpensiveTablet();

            assertEquals(5, result.size());
            assertEquals("Surface Pro", ((Tablet) result.get(0)).getModelName());
            assertEquals("NotePad", ((Tablet) result.get(1)).getModelName());
            assertEquals("iPad Pro", ((Tablet) result.get(2)).getModelName());
            assertEquals("Galaxy Tab S9", ((Tablet) result.get(3)).getModelName());
            assertEquals("iPad Air", ((Tablet) result.get(4)).getModelName());
        }

        @Test
        void testTopFiveMostExpensiveTablet_NoMatches() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer apple = new Manufacturer("Apple", 1020);
            api.addTechnologyDevice(new SmartWatch("Series 9", 499.99, apple, "W1", "S9", "1.8", "watchOS"));

            List<Technology> result = api.topFiveMostExpensiveTablet();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
        @Test
        void testTopFiveMostExpensiveTechnology_WithMoreThanFiveItems() {

            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new Tablet("iPad Pro", 899.99, apple, "T1", "A13", 8, "iPadOS"));
            api.addTechnologyDevice(new SmartWatch("Galaxy Watch", 349.99, samsung, "W1", "Exynos", "1.5", "Tizen"));
            api.addTechnologyDevice(new SmartBand("Fit Band", 79.99, samsung, "B1", "ChipX", "1.0", true));
            api.addTechnologyDevice(new Tablet("NotePad", 1099.99, samsung, "T2", "Snapdragon", 16, "Android"));
            api.addTechnologyDevice(new SmartWatch("Series 9", 499.99, apple, "W2", "S9", "1.8", "watchOS"));
            api.addTechnologyDevice(new Tablet("Cheap Pad", 199.99, apple, "T3", "ARM", 4, "Android"));

            List<Technology> result = api.topFiveMostExpensiveTechnology();

            assertNotNull(result);
            assertEquals(3, result.size());

        }

        @Test
        void testTopFiveMostExpensiveTechnology_WithExactlyFiveItems() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);
            Manufacturer apple = new Manufacturer("Apple", 1020);

            api.addTechnologyDevice(new Tablet("NotePad", 1099.99, samsung, "T1", "Snapdragon", 16, "Android"));
            api.addTechnologyDevice(new Tablet("iPad Pro", 899.99, apple, "T2", "A13", 8, "iPadOS"));
            api.addTechnologyDevice(new SmartWatch("Series 9", 499.99, apple, "W1", "S9", "1.8", "watchOS"));
            api.addTechnologyDevice(new SmartWatch("Galaxy Watch", 349.99, samsung, "W2", "Exynos", "1.5", "Tizen"));
            api.addTechnologyDevice(new SmartBand("Fit Band", 79.99, samsung, "B1", "ChipX", "1.0", true));

            List<Technology> result = api.topFiveMostExpensiveTechnology();

            assertNotNull(result);
            assertEquals(2, result.size());

        }

        @Test
        void testTopFiveMostExpensiveTechnology_WithLessThanFiveItems() {
            File dummyFile = new File("dummy.xml");
            TechnologyDeviceAPI api = new TechnologyDeviceAPI(dummyFile);

            Manufacturer samsung = new Manufacturer("Samsung", 1200);

            api.addTechnologyDevice(new SmartBand("Fit Band", 79.99, samsung, "B1", "ChipX", "1.0", true));
            api.addTechnologyDevice(new Tablet("NotePad", 1099.99, samsung, "T2", "Snapdragon", 16, "Android"));

            List<Technology> result = api.topFiveMostExpensiveTechnology();

            assertNotNull(result);
            assertEquals(2, result.size());
        }

    }
}
