package main;

import controllers.ManufacturerAPI;
import controllers.TechnologyDeviceAPI;
import models.*;
import utils.ScannerInput;
import utils.OperatingSystemUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Driver {

    private final ManufacturerAPI manufacturerAPI;
    private final TechnologyDeviceAPI techDeviceAPI;

    public Driver() {
        // Initialize file paths
        File manufacturersFile = new File("manufacturers.xml");
        File techDevicesFile = new File("technology_devices.xml");
        manufacturerAPI = new ManufacturerAPI(manufacturersFile);
        techDeviceAPI = new TechnologyDeviceAPI(techDevicesFile);
    }

    public static void main(String[] args) {
        new Driver().start();
    }

    public void start() {
        try {
            // Load data
            manufacturerAPI.load();
            techDeviceAPI.load();
            System.out.println("Data loaded successfully");
        } catch (Exception e) {
            System.out.println("Warning: Failed to load data, starting with empty lists\n" + e.getMessage());
        }
        runMainMenu();
    }

    // --------------------- Main Menu ---------------------
    private void runMainMenu() {
        int option;
        do {
            printMainMenu();
            option = ScannerInput.readNextInt("Enter your choice: ");
            handleMainMenuOption(option);
        } while (option != 0);
        exitApp();
    }

    private void printMainMenu() {
        System.out.println("""
                         ---- Technology Store ----
                        | 1) Manufacturer Management Menu    |
                        | 2) Technology Device Management Menu|
                        | 3) Reports Menu                     |
                        |-------------------------------------|
                        | 4) Search Manufacturers             |
                        | 5) Search Technology Devices        |
                        | 6) Sort Technology Devices          |
                        |-------------------------------------|
                        | 10) Save All Data                   |
                        | 11) Load All Data                   |
                        |-------------------------------------|
                        | 0) Exit                             |
                         -------------------------------""");
    }

    private void handleMainMenuOption(int option) {
        switch (option) {
            case 1 -> runManufacturerMenu();
            case 2 -> runTechDeviceMenu();
            case 3 -> runReportsMenu();
            case 4 -> searchManufacturers();
            case 5 -> searchTechDevices();
            case 6 -> sortTechDevices();
            case 10 -> saveAllData();
            case 11 -> loadAllData();
            case 0 -> System.out.println("Exiting system");
            default -> {
                System.out.println("Invalid option, please try again");
                printMainMenu();
            }
        }
    }

    // --------------------- Manufacturer Management Menu ---------------------
    private void runManufacturerMenu() {
        int option;
        do {
            printManufacturerMenu();
            option = ScannerInput.readNextInt("Enter your choice: ");
            handleManufacturerMenuOption(option);
        } while (option != 0);
    }

    private void printManufacturerMenu() {
        System.out.println("""
                ---- Manufacturer Menu ----
               | 1) Add Manufacturer          |
               | 2) Delete Manufacturer       |
               | 3) Update Manufacturer       |
               | 4) List All Manufacturers    |
               | 5) Find Manufacturer         |
               | 0) Return to Main Menu       |
                ---------------------------""");
    }

    private void handleManufacturerMenuOption(int option) {
        switch (option) {
            case 1 -> addManufacturer();
            case 2 -> deleteManufacturer();
            case 3 -> updateManufacturer();
            case 4 -> System.out.println(manufacturerAPI.listManufacturers());
            case 5 -> findManufacturer();
            default -> {
                System.out.println("Invalid option");
                printManufacturerMenu();
            }
        }
    }

    private void addManufacturer() {
        String name = ScannerInput.readNextLine("Enter manufacturer name: ");
        if (manufacturerAPI.isValidManufacturer(name)) {
            System.out.println("Error: Manufacturer already exists");
            return;
        }
        int employees = ScannerInput.readNextInt("Enter number of employees: ");
        if (manufacturerAPI.addManufacturer(new Manufacturer(name, employees))) {
            System.out.println("Manufacturer added successfully");
        } else {
            System.out.println("Failed to add manufacturer");
        }
    }

    private void deleteManufacturer() {
        String name = ScannerInput.readNextLine("Enter manufacturer name to delete: ");
        if (manufacturerAPI.removeManufacturerByName(name) != null) {
            System.out.println("Manufacturer deleted successfully");
        } else {
            System.out.println("Manufacturer not found");
        }
    }

    private void updateManufacturer() {
        String name = ScannerInput.readNextLine("Enter manufacturer name to update: ");
        Manufacturer mfr = manufacturerAPI.getManufacturerByName(name);
        if (mfr == null) {
            System.out.println("Error: Manufacturer not found");
            return;
        }
        int newEmployees = ScannerInput.readNextInt("Enter new number of employees: ");
        if (manufacturerAPI.updateManufacturer(name, newEmployees)) {
            System.out.println("Update successful");
        } else {
            System.out.println("Update failed");
        }
    }

    private void findManufacturer() {
        String name = ScannerInput.readNextLine("Enter manufacturer name: ");
        Manufacturer mfr = manufacturerAPI.getManufacturerByName(name);
        if (mfr != null) {
            System.out.println(mfr);
        } else {
            System.out.println("Manufacturer not found");
        }
    }

    // --------------------- Technology Device Management Menu ---------------------
    private void runTechDeviceMenu() {
        int option;
        do {
            printTechDeviceMenu();
            option = ScannerInput.readNextInt("Enter your choice: ");
            handleTechDeviceMenuOption(option);
        } while (option != 0);
    }

    private void printTechDeviceMenu() {
        System.out.println("""
                ---- Technology Device Menu ----
               | 1) Add Technology Device       |
               | 2) Delete Technology Device (by ID)|
               | 3) List All Technology Devices  |
               | 4) Update Technology Device Price|
               | 0) Return to Main Menu          |
                -------------------------------""");
    }

    private void handleTechDeviceMenuOption(int option) {
        switch (option) {
            case 1 -> addTechDevice();
            case 2 -> deleteTechDeviceById();
            case 3 -> System.out.println(techDeviceAPI.listAllTechnologyDevices());
            case 4 -> updateTechDevicePrice();
            default -> {
                System.out.println("Invalid option");
                printTechDeviceMenu();
            }
        }
    }

    private void addTechDevice() {
        // Select device type
        int type = ScannerInput.readNextInt("""
                Select device type:
                1) SmartBand
                2) SmartWatch
                3) Tablet
                Enter option: """);

        Manufacturer manufacturer = getValidManufacturer();
        if (manufacturer == null) return;

        String model = null;
        while (true) {
            model = ScannerInput.readNextLine("Enter model name (max 30 characters): ");
            if (model.length() <= 30) {
                break;
            }
            System.out.println("Model name should have a maximum of 30 characters. Please try again.");
        }

        double price = 0;
        while (true) {
            price = ScannerInput.readNextDouble("Enter price (must be >= 20): ");
            if (price >= 20) {
                break;
            }
            System.out.println("Price must be at least 20. Please try again.");
        }

        String id = null;
        while (true) {
            id = ScannerInput.readNextLine("Enter device ID (max 10 characters): ");
            if (id.length() <= 10) {
                break;
            }
            System.out.println("ID should have a maximum of 10 characters. Please try again.");
        }

        Technology tech;
        switch (type) {
            case 1 -> {
                String material = ScannerInput.readNextLine("Enter material: ");
                String size = ScannerInput.readNextLine("Enter size: ");
                boolean isVerified=false;
                while(true){
                    char verified=ScannerInput.readNextChar("Has heart rate monitor? (y/n): ");
                    if((verified=='y')||(verified=='Y')){
                        isVerified=true;
                        break;
                    }else if((verified=='n')||(verified=='N')){
                        isVerified=false;
                        break;
                    }else{
                        System.out.println("Invalid verified entered: "+verified);
                    }
                }
                tech = new SmartBand(model, price, manufacturer, id, material, size, isVerified);
            }
            case 2 -> {
                String material = ScannerInput.readNextLine("Enter material: ");
                String size = ScannerInput.readNextLine("Enter size: ");
                String displayType = ScannerInput.readNextLine("Enter display type (AMOLED, LCD, LED, TFT): ");
                try {
                    tech = new SmartWatch(model, price, manufacturer, id, material, size, displayType);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
            case 3 -> {
                String processor = ScannerInput.readNextLine("Enter processor: ");
                int storage = ScannerInput.readNextInt("Enter storage (GB): ");
                String operatingSystem = ScannerInput.readNextLine("Enter operating system: ");
                if (!OperatingSystemUtility.isValidOperatingSystem(operatingSystem)) {
                    System.out.println("Invalid operating system. Using default: Windows");
                    operatingSystem = "Windows";
                }
                tech = new Tablet(model, price, manufacturer, id, processor, storage, operatingSystem);
            }
            default -> {
                System.out.println("Invalid type");
                return;
            }
        }

        if (techDeviceAPI.addTechnologyDevice(tech)) {
            System.out.println("Device added successfully");
        } else {
            System.out.println("Error: Device ID already exists or invalid data");
        }
    }

    private void deleteTechDeviceById() {
        String id = ScannerInput.readNextLine("Enter device ID to delete: ");
        if (techDeviceAPI.deleteTechnologyById(id) != null) {
            System.out.println("Device deleted successfully");
        } else {
            System.out.println("Device not found");
        }
    }

    private void updateTechDevicePrice() {
        String id = ScannerInput.readNextLine("Enter device ID to update: ");
        Technology tech = techDeviceAPI.getTechnologyDeviceById(id);
        if (tech == null) {
            System.out.println("Device not found");
            return;
        }
        double newPrice = 0;
        while (true) {
            newPrice = ScannerInput.readNextDouble("Enter new price (must be >= 20): ");
            if (newPrice >= 20) {
                break;
            }
            System.out.println("Price must be at least 20. Please try again.");
        }
        try {
            tech.setPrice(newPrice);
            if (tech instanceof SmartBand) {
                techDeviceAPI.updateSmartBand(id, (SmartBand) tech);
            } else if (tech instanceof SmartWatch) {
                techDeviceAPI.updateSmartWatch(id, (SmartWatch) tech);
            } else if (tech instanceof Tablet) {
                techDeviceAPI.updateTablet(id, (Tablet) tech);
            }
            System.out.println("Price updated successfully");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // --------------------- Reports Menu ---------------------
    private void runReportsMenu() {
        int option;
        do {
            printReportsMenu();
            option = ScannerInput.readNextInt("Enter your choice: ");
            handleReportsOption(option);
        } while (option != 0);
    }

    private void printReportsMenu() {
        System.out.println("""
                ---- Reports Menu ----
               | 1) Manufacturer Reports    |
               | 2) Technology Reports      |
               | 0) Return to Main Menu     |
                -------------------------""");
    }

    private void handleReportsOption(int option) {
        switch (option) {
            case 1 -> printManufacturerReports();
            case 2 -> printTechDeviceReports();
            default -> {
                System.out.println("Invalid option");
                printReportsMenu();
            }
        }
    }

    private void printManufacturerReports() {
        System.out.println("--- All Manufacturers ---");
        System.out.println(manufacturerAPI.listManufacturers());
        System.out.println("--- Technology Count by Manufacturer ---");
        for (Manufacturer mfr : manufacturerAPI.getManufacturers()) {
            int count = techDeviceAPI.numberOfTechnologyByChosenManufacturer(mfr);
            System.out.println(mfr.getManufacturerName() + ": " + count + " devices");
        }
    }

    private void printTechDeviceReports() {
        System.out.println("--- All Technology Devices ---");
        System.out.println(techDeviceAPI.listAllTechnologyDevices());
        System.out.println("--- Technology Statistics ---");
        System.out.println("SmartBands: " + techDeviceAPI.numberOfSmartBands());
        System.out.println("SmartWatches: " + techDeviceAPI.numberOfSmartWatch());
        System.out.println("Tablets: " + techDeviceAPI.numberOfTablets());
    }

    // --------------------- Search Functions ---------------------
    private void searchManufacturers() {
        String keyword = ScannerInput.readNextLine("Enter manufacturer name or keyword: ");
        List<Manufacturer> results = new ArrayList<>();
        for (Manufacturer mfr : manufacturerAPI.getManufacturers()) {
            if (mfr.getManufacturerName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(mfr);
            }
        }
        if (results.isEmpty()) {
            System.out.println("No matching manufacturers found");
        } else {
            System.out.println("Search Results:");
            results.forEach(System.out::println);
        }
    }

    private void searchTechDevices() {
        String keyword = ScannerInput.readNextLine("Enter device ID or model keyword: ");
        List<Technology> results = new ArrayList<>();
        for (Technology tech : techDeviceAPI.technologyList) {
            if (tech.getId().contains(keyword) || tech.getModelName().contains(keyword)) {
                results.add(tech);
            }
        }
        if (results.isEmpty()) {
            System.out.println("No matching technology devices found");
        } else {
            System.out.println("Search Results:");
            results.forEach(System.out::println);
        }
    }

    // --------------------- Sorting Function ---------------------
    private void sortTechDevices() {
        int choice = ScannerInput.readNextInt("""
                Select sorting option:
                1) Price Ascending
                2) Price Descending
                Enter option: """);
        switch (choice) {
            case 1 -> {
                techDeviceAPI.sortByPriceAscending();
                System.out.println("Sorted by price ascending:");
                System.out.println(techDeviceAPI.listAllTechnologyDevices());
            }
            case 2 -> {
                techDeviceAPI.sortByPriceDescending();
                System.out.println("Sorted by price descending:");
                System.out.println(techDeviceAPI.listAllTechnologyDevices());
            }
            default -> {
                System.out.println("Invalid sorting option");
                System.out.println("Please select either 1 for Price Ascending or 2 for Price Descending.");
            }
        }
    }

    // --------------------- Helper Methods ---------------------
    private Manufacturer getValidManufacturer() {
        String name = ScannerInput.readNextLine("Enter manufacturer name: ");
        if (manufacturerAPI.isValidManufacturer(name)) {
            return manufacturerAPI.getManufacturerByName(name);
        } else {
            System.out.println("Error: Manufacturer does not exist, please add first");
            return null;
        }
    }

    private void saveAllData() {
        try {
            manufacturerAPI.save();
            techDeviceAPI.save();
            System.out.println("Data saved successfully");
        } catch (Exception e) {
            System.out.println("Failed to save data: " + e.getMessage());
        }
    }

    private void loadAllData() {
        try {
            manufacturerAPI.load();
            techDeviceAPI.load();
            System.out.println("Data reloaded successfully");
        } catch (Exception e) {
            System.out.println("Failed to load data: " + e.getMessage());
        }
    }

    private void exitApp() {
        saveAllData();
        System.out.println("System exited");
    }
}