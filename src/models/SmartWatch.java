package models;

import static utils.DisplayTypeUtility.isValidDisplayType;

public class SmartWatch extends WearableDevice{
    private String displayType = "LCD";

    public SmartWatch(String modelName, double price, Manufacturer manufacturer, String id, String material, String size,String displayType) {
        super(modelName, price, manufacturer, id,material,size);
        if (isValidDisplayType(displayType)) {
            this.displayType = displayType;
        } else {
            return; // Return without updating the display type
        }
    }
    private boolean isValidDisplayType(String type) {
        return type.equals("AMOLED") || type.equals("LCD") || type.equals("LED") || type.equals("TFT");
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        if (isValidDisplayType(displayType)) {
            this.displayType = displayType;
        } else {
            return; // Return without updating the display type
        }
    }

    public double getInsurancePremium(double price) {
        return price * 0.06;
    }

    @Override
    public double getInsurancePremium() {
        return 0;
    }

    public String connectToInternet() {
        return "Connects to the internet via bluetooth";// return the String "Connects to the internet via bluetooth"

    }

    @Override
    public String toString() {
        return "Model: " + modelName +
                ", Price: €" + price +
                ", Manufacturer: " + manufacturer.getManufacturerName() +
                ", ID: " + id +
                ", Material: " + material +
                ", Size: " + size +
                ", Display Type: " + displayType;
    }
}