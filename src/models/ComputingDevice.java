package models;

public abstract class ComputingDevice extends Technology {

    int storage;
    String processor;

    //storage : This is the storage of the Computing device, stored in GB. It must be between 8 and 128, and must be divisible by 8
    //processor : This is the processor of the computing device. It should be 20 chars or less, there is no default.

    //There is one constructor for this class. The parameter list for this constructor should
    // be the parameter list for the Technology with the additional two fields above . The
    //constructor should call the superclass constructor.

    public ComputingDevice(String modelName, double price,
                           Manufacturer manufacturer, String id, String processor, int storage) {
        super(modelName, price, manufacturer, id);
        if (storage >= 8 && storage <= 128 && storage % 8 == 0) {
            this.storage = storage;
        } else {
            this.storage = 8;
        }
        if (processor.length() > 20) {
            this.processor = processor.substring(0, 20);
        } else {
            this.processor = processor;
        }
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        if (processor.length() > 20) {
            return;
        } else {
            this.processor = processor;
        }
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        if (storage >= 8 && storage <= 128 && storage % 8 == 0) {
            this.storage = storage;
        }
    }

    @Override
    public String toString() {
        return "Processor: " + processor + ", Storage: " + storage + "GB";
    }

    //Abstract methods
    //• getInsurancePremium()
    //• connectToInternet() this can be left as abstract in this class
    public abstract double getInsurancePremium();
    public abstract String connectToInternet();

}
