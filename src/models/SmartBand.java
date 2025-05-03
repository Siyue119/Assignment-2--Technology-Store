package models;


public class SmartBand extends WearableDevice {
    private boolean heartRateMonitor;

    public SmartBand(String modelName, double price, Manufacturer manufacturer, String id, String material, String size,boolean heartRateMonitor) {
        super(modelName, price, manufacturer, id,material,size);
        this.heartRateMonitor = heartRateMonitor;
    }

    public double getInsurancePremium() {
        return getPrice() * 0.07;
    }

    public String connectToInternet() {
        return "Connects to the internet via Companion App";
    }

    @Override
    public String toString() {
        String modelName = getModelName();
        double price = getPrice();
        Manufacturer manufacturer = getManufacturer();
        String id = getId();
        String material = getMaterial();
        String size = getSize();

        String manufacturerName = (manufacturer != null) ? manufacturer.getManufacturerName() : "null";

        String heartRateInfo = heartRateMonitor
                ? "Includes Heart Rate Monitor"
                : "No Heart Rate Monitor included";

        double insurancePremium = getInsurancePremium();

        return "Model Name: " + modelName +
                ", Price: " + price +
                ", Manufacturer: " + manufacturerName +
                ", ID: " + id +
                ", Material: " + material +
                ", Size: " + size +
                ", " + heartRateInfo +
                ", Insurance Premium: " + insurancePremium +
                ", Connects to the internet via Companion App";
    }

    public boolean hasHeartRateMonitor() {
        return heartRateMonitor;
    }
}