package fa.training.model;

public class Fixedwing extends Airplane {

    private static final long serialVersionUID = 1L;

    private PlaneType planeType;
    private double minNeededRunwaySize;

    public Fixedwing() {
    }

    public Fixedwing(String id, String model, double cruiseSpeed,
            double emptyWeight, double maxTakeoffWeight,
            PlaneType planeType, double minNeededRunwaySize) {
        super(id, model, cruiseSpeed, emptyWeight, maxTakeoffWeight);
        this.planeType = planeType;
        this.minNeededRunwaySize = minNeededRunwaySize;
    }

    public PlaneType getPlaneType() {
        return planeType;
    }

    public void setPlaneType(PlaneType planeType) {
        this.planeType = planeType;
    }

    public double getMinNeededRunwaySize() {
        return minNeededRunwaySize;
    }

    public void setMinNeededRunwaySize(double minNeededRunwaySize) {
        this.minNeededRunwaySize = minNeededRunwaySize;
    }

    @Override
    public void fly() {
        System.out.println("Fixedwing is flying with fixed wings.");
    }

    @Override
    public String toString() {
        return String.format("Fixedwing{id='%s', model='%s', planeType=%s, minRunway=%.1f}",
                id, model, planeType, minNeededRunwaySize);
    }
}
