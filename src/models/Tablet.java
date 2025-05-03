package models;

import utils.OperatingSystemUtility;

public class Tablet extends ComputingDevice {
    private String operatingSystem;


    public Tablet(String modelName, double price, Manufacturer manufacturer, String id, String processor, int storage, String operatingSystem) {
        super(modelName, price, manufacturer, id, processor, storage);
        if (OperatingSystemUtility.isValidOperatingSystem(operatingSystem)) {
            this.operatingSystem = operatingSystem;
        } else {
            this.operatingSystem = "Windows";
        }
    }

    public double getInsurancePremium() {
        return getPrice() * 0.01;
    }

    public String connectToInternet() {
        return "Connects to the internet via Wi-Fi";
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        if (OperatingSystemUtility.isValidOperatingSystem(operatingSystem)) {
            this.operatingSystem = operatingSystem;
        }
    }


    @Override
    public String toString() {
        return "Model: " + modelName +
                ", Price: €" + price +
                ", Manufacturer Details: Manufacturer{manufacturerName='" + manufacturer.getManufacturerName() +
                "', numEmployees=" + manufacturer.getNumEmployees() + (manufacturer.getNumEmployees()==1 ? " employee" : " employees") + "}, ID: " + id+
                "Operating System: "+ operatingSystem+ ", Insurance Premium: €"+ getInsurancePremium()+"Processor: " + processor + ", Storage: " + storage + "GB";
        };
    }
