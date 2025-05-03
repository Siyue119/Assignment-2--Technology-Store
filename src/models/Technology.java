package models;

public abstract class Technology {
    double price=20.0;
    String id = "unknown";
    String modelName;
    Manufacturer manufacturer;
    double InsurancePremium;
    String connectToInternet;

    public Technology(String modelName, double price, Manufacturer manufacturer, String id) {
        if (modelName == null) {
            this.modelName = "unknown";
        } else if (modelName.length() > 30) {
            this.modelName = modelName.substring(0, 30);
        } else {
            this.modelName = modelName;
        }

        if (price >= 20.0) {
            this.price = price;
        }

        this.manufacturer = manufacturer;

        if (id == null) {
            this.id = "unknown";
            return;
        }

        String digitsOnly = id.replaceAll("[^0-9]", "");

        if (digitsOnly.length() > 10) {
            this.id = "unknown";
        } else {
            this.id = digitsOnly;
        }
    }

    public abstract double getInsurancePremium();

    public abstract String connectToInternet();

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price >= 20.0) {
            this.price = price;
        }
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        if (modelName == null) {
            return;
        } else if (modelName.length() > 30) {
            return;
        } else {
            this.modelName = modelName;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null) {
            this.id = "unknown";
            return;
        }

        String digitsOnly = id.replaceAll("[^0-9]", "");

        if (digitsOnly.length() > 10) {
            this.id = "unknown";
        } else {
            this.id = digitsOnly;
        }
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Model: " + modelName +
                ", Price: €" + price +
                ", Manufacturer Details: Manufacturer{manufacturerName='" + manufacturer.getManufacturerName() +
                "', numEmployees=" + manufacturer.getNumEmployees()  + (manufacturer.getNumEmployees()==1 ? " employee" : " employees") + "}, ID: " + id;
    }
}
