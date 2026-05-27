package fa.training.model;

public class Helicopter extends Airplane {

    private static final long serialVersionUID = 1L;

    private double range;

    public Helicopter() {
    }

    public Helicopter(String id, String model, double cruiseSpeed,
            double emptyWeight, double maxTakeoffWeight, double range) {
        super(id, model, cruiseSpeed, emptyWeight, maxTakeoffWeight);
        this.range = range;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    @Override
    public void fly() {
        System.out.println("Helicopter is flying with rotating blades.");
    }

    @Override
    public String toString() {
        return String.format("Helicopter{id='%s', model='%s', range=%.1f}", id, model, range);
    }
}
