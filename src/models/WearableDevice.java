package models;

public abstract class WearableDevice extends Technology {

    String material;
    String size;

    //• material : This is the material of the wearable. It should be less than 20 chars, there is no default.
    //• size : This is the size of the wearable. It should be less than 10 chars, there is no default.

    //There is one constructor for this class. The parameter list for this constructor should
    // be the parameter list for the Technology with the additional two fields above . The
    // constructor should call the superclass constructor.
    public WearableDevice(String modelName, double price, Manufacturer manufacturer, String id, String material, String size) {
        super(modelName, price, manufacturer, id);
        if (material.length() > 20) {
            this.material = material.substring(0, 20);
        } else {
            this.material = material;
        }
        if (size.length() > 10) {
            this.size = size.substring(0, 10);
        } else {
            this.size = size;
        }
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        if (material.length() > 20) {
            this.material = material.substring(0, 20);
        } else {
            this.material = material;
        }
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        if (size.length() > 10) {
            this.size = size.substring(0, 10);
        } else {
            this.size = size;
        }
    }

    public abstract double getInsurancePremium();
    public abstract String connectToInternet();
}
