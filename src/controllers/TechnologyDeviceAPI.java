package controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import models.*;

import utils.ISerializer;
import utils.OperatingSystemUtility;

import utils.Utilities;

import java.io.*;
import java.util.*;

//TODO - ensure that this class implements iSerializer
//TODO - create 2 fields

public class TechnologyDeviceAPI implements ISerializer {
    //TODO - create constructor
    public List<Technology> technologyList = new ArrayList<>();
    private File file;
    //TODO - CRUD Methods
    public TechnologyDeviceAPI(File file) {
        this.file = file;
    }

    public boolean addTechnologyDevice(Technology tech) {
        for (int i = 0; i < technologyList.size(); i++) {
            if (technologyList.get(i).getId().equalsIgnoreCase(tech.getId())) {
                technologyList.set(i, tech);
                return true;
            }
        }
        technologyList.add(tech);
        return true;
    }

    public Technology deleteTechnologyByIndex(int index) {
        if (index < 0 || index >= technologyList.size()) {
            return null;
        }
        return technologyList.remove(index);
    }

    public Technology deleteTechnologyById(String id) {
        for (int i = 0; i < technologyList.size(); i++) {
            Technology tech = technologyList.get(i);
            if (tech.getId().equalsIgnoreCase(id)) {
                return technologyList.remove(i);
            }
        }
        return null;
    }

    public Technology getTechnologyByIndex(int index) {
        if (index >= 0 && index < technologyList.size()) {
            return technologyList.get(index);
        }
        return null;
    }

    public Technology getTechnologyDeviceById(String id) {
        for (Technology tech : technologyList) {
            if (tech.getId().equalsIgnoreCase(id)) {
                return tech;
            }
        }
        return null;
    }


    public int numberTechnologyDevices() {
        return technologyList.size();
    }


    public int numberOfTablets() {
        int count = 0;
        for (Technology tech : technologyList) {
            if (tech instanceof Tablet) {
                count++;
            }
        }
        return count;
    }


    public int numberOfSmartBands() {
        int count = 0;
        for (Technology tech : technologyList) {
            if (tech instanceof SmartBand) {
                count++;
            }
        }
        return count;
    }


    public int numberOfSmartWatch() {
        int count = 0;
        for (Technology tech : technologyList) {
            if (tech instanceof SmartWatch) {
                count++;
            }
        }
        return count;
    }


    public int numberOfTechnologyByChosenManufacturer(Manufacturer manufacturer) {
        String mfrName = manufacturer.getManufacturerName();
        int count = 0;
        for (Technology tech : technologyList) {
            if (mfrName.equalsIgnoreCase(tech.getManufacturer().getManufacturerName())) {
                count++;
            }
        }
        return count;
    }

    public String listAllTechnologyDevices() {
        if (technologyList.isEmpty()) {
            return "No Technology Devices";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < technologyList.size(); i++) {
            sb.append(i).append(": ").append(technologyList.get(i).toString()).append("\n");
        }
        return sb.toString();
    }


    public String listAllSmartBands() {
        StringBuilder sb = new StringBuilder();
        boolean hasSmartBand = false;
        for (Technology tech : technologyList) {
            if (tech instanceof SmartBand) {
                hasSmartBand = true;
                sb.append(technologyList.indexOf(tech)).append(": ").append(tech.toString()).append("\n");
            }
        }
        if (!hasSmartBand) {
            return "No Smart Bands";
        }
        return sb.toString();
    }


    public String listAllSmartWatches() {
        StringBuilder sb = new StringBuilder();
        boolean hasSmartWatch = false;
        for (Technology tech : technologyList) {
            if (tech instanceof SmartWatch) {
                hasSmartWatch = true;
                sb.append(technologyList.indexOf(tech)).append(": ").append(tech.toString()).append("\n");
            }
        }
        if (!hasSmartWatch) {
            return "No Smart Watches";
        }
        return sb.toString();
    }

    public String listAllTablets() {
        StringBuilder sb = new StringBuilder();
        boolean hasTablet = false;
        for (Technology tech : technologyList) {
            if (tech instanceof Tablet) {
                hasTablet = true;
                sb.append(technologyList.indexOf(tech)).append(": ").append(tech.toString()).append("\n");
            }
        }
        if (!hasTablet) {
            return "No Tablets";
        }
        return sb.toString();
    }

    public String listAllTechnologyAbovePrice(double price) {
        StringBuilder sb = new StringBuilder();
        boolean hasTechAbovePrice = false;
        for (Technology tech : technologyList) {
            if (tech.getPrice() >= price) {
                hasTechAbovePrice = true;
                sb.append(tech.toString()).append("\n");
            }
        }
        if (!hasTechAbovePrice) {
            return "No technology more expensive than " + price + "?";
        }
        return sb.toString();
    }

    public String listAllTechnologyBelowPrice(double price) {
        StringBuilder sb = new StringBuilder();
        boolean hasTechBelowPrice = false;
        for (Technology tech : technologyList) {
            if (tech.getPrice() <= price) {
                hasTechBelowPrice = true;
                sb.append(tech.toString()).append("\n");
            }
        }
        if (!hasTechBelowPrice) {
            return "No technology cheaper than " + price + "?";
        }
        return sb.toString();
    }


    public String listAllTechDevicesByChosenManufacturer(Manufacturer manufacturer) {
        StringBuilder sb = new StringBuilder();
        boolean hasTechByManufacturer = false;
        for (Technology tech : technologyList) {
            if (tech.getManufacturer().equals(manufacturer)) {
                hasTechByManufacturer = true;
                sb.append(tech.toString()).append("\n");
            }
        }
        if (!hasTechByManufacturer) {
            return "No technology manufactured by " + manufacturer.getManufacturerName();
        }
        return sb.toString();
    }


    public String listAllTabletsByOperatingSystem(String os) {
        StringBuilder sb = new StringBuilder();
        boolean hasTabletWithOs = false;
        for (Technology tech : technologyList) {
            if (tech instanceof Tablet && ((Tablet) tech).getOperatingSystem().equalsIgnoreCase(os)) {
                hasTabletWithOs = true;
                sb.append(tech.toString()).append("\n");
            }
        }
        if (!hasTabletWithOs) {
            return "No tablet with the operating system " + os;
        }
        return sb.toString();
    }


    public boolean updateTablet(String id, Tablet updatedDetails) {
        for (int i = 0; i < technologyList.size(); i++) {
            Technology tech = technologyList.get(i);
            if (tech.getId().equalsIgnoreCase(id) && tech instanceof Tablet) {
                technologyList.set(i, updatedDetails);
                return true;
            }
        }
        return false;
    }


    public boolean updateSmartWatch(String id, SmartWatch updatedDetails) {
        for (int i = 0; i < technologyList.size(); i++) {
            Technology tech = technologyList.get(i);
            if (tech.getId().equalsIgnoreCase(id) && tech instanceof SmartWatch) {
                technologyList.set(i, updatedDetails);
                return true;
            }
        }
        return false;
    }


    public boolean updateSmartBand(String id, SmartBand updatedDetails) {
        for (int i = 0; i < technologyList.size(); i++) {
            Technology tech = technologyList.get(i);
            if (tech.getId().equalsIgnoreCase(id) && tech instanceof SmartBand) {
                technologyList.set(i, updatedDetails);
                return true;
            }
        }
        return false;
    }

    public boolean isValidId(String id) {
        for (Technology techDev : technologyList) {
            if (techDev.getId().equalsIgnoreCase(id)) {
                return false;
            }
        }
        return true;
    }


    public void sortByPriceAscending() {
        Collections.sort(technologyList, Comparator.comparingDouble(Technology::getPrice));
    }


    public void sortByPriceDescending() {
        Collections.sort(technologyList, Comparator.comparingDouble(Technology::getPrice).reversed());
    }

    private void swapTechnology(int i, int j) {
        Collections.swap(technologyList, i, j);
    }


    public List<Technology> topFiveMostExpensiveTechnology() {
        List<Technology> sortedList = new ArrayList<>(technologyList);
        sortedList.sort(Comparator.comparingDouble(Technology::getPrice).reversed());
        return sortedList.subList(0, Math.min(5, sortedList.size()));
    }


    public List<Technology> topFiveMostExpensiveSmartWatch() {
        List<Technology> smartWatchList = new ArrayList<>();
        for (Technology tech : technologyList) {
            if (tech instanceof SmartWatch) {
                smartWatchList.add(tech);
            }
        }
        smartWatchList.sort(Comparator.comparingDouble(Technology::getPrice).reversed());
        return smartWatchList.subList(0, Math.min(5, smartWatchList.size()));
    }


    public List<Technology> topFiveMostExpensiveTablet() {
        List<Technology> tabletList = new ArrayList<>();
        for (Technology tech : technologyList) {
            if (tech instanceof Tablet) {
                tabletList.add(tech);
            }
        }
        tabletList.sort(Comparator.comparingDouble(Technology::getPrice).reversed());
        return tabletList.subList(0, Math.min(5, tabletList.size()));
    }

    @SuppressWarnings("unchecked")
    public void save() throws Exception {
        XStream xstream = new XStream(new DomDriver());

        xstream.allowTypes(new Class[]{
                Technology.class,
                Tablet.class,
                SmartWatch.class,
                SmartBand.class
        });

        xstream.alias("technologyList", ArrayList.class);
        xstream.alias("tablet", Tablet.class);
        xstream.alias("smartWatch", SmartWatch.class);
        xstream.alias("smartBand", SmartBand.class);

        ObjectOutputStream os = xstream.createObjectOutputStream(new FileWriter(file));
        os.writeObject(technologyList);
        os.close();
    }

    public void load() throws Exception {
        XStream xstream = new XStream(new DomDriver());

        xstream.allowTypes(new Class[]{
                Technology.class,
                Tablet.class,
                SmartWatch.class,
                SmartBand.class
        });

        xstream.alias("technologyList", ArrayList.class);
        xstream.alias("tablet", Tablet.class);
        xstream.alias("smartWatch", SmartWatch.class);
        xstream.alias("smartBand", SmartBand.class);

        ObjectInputStream is = xstream.createObjectInputStream(new FileReader(file));
        technologyList = (List<Technology>) is.readObject();
        is.close();
    }
    public String fileName() {
        return file.getName();
    }
}
