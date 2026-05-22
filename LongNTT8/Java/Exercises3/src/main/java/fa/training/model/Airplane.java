package fa.training.model;

import java.io.Serializable;

public abstract class Airplane implements Serializable, Identifiable {

    private static final long serialVersionUID = 1L;

    protected String id;
    protected String model;
    protected double cruiseSpeed;
    protected double emptyWeight;
    protected double maxTakeoffWeight;

    protected Airplane() {
    }

    protected Airplane(String id, String model, double cruiseSpeed,
            double emptyWeight, double maxTakeoffWeight) {
        this.id = id;
        this.model = model;
        this.cruiseSpeed = cruiseSpeed;
        this.emptyWeight = emptyWeight;
        this.maxTakeoffWeight = maxTakeoffWeight;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getCruiseSpeed() {
        return cruiseSpeed;
    }

    public void setCruiseSpeed(double cruiseSpeed) {
        this.cruiseSpeed = cruiseSpeed;
    }

    public double getEmptyWeight() {
        return emptyWeight;
    }

    public void setEmptyWeight(double emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public double getMaxTakeoffWeight() {
        return maxTakeoffWeight;
    }

    public void setMaxTakeoffWeight(double maxTakeoffWeight) {
        this.maxTakeoffWeight = maxTakeoffWeight;
    }

    public abstract void fly();

    @Override
    public String toString() {
        return String.format("Airplane{id='%s', model='%s', cruiseSpeed=%.1f, emptyWeight=%.1f, maxTakeoffWeight=%.1f}",
                id, model, cruiseSpeed, emptyWeight, maxTakeoffWeight);
    }
}
