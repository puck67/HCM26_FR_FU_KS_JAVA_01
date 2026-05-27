package fa.training.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Airport implements Serializable, Identifiable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private double runwaySize;
    private int maxFixedWingParkingPlace;
    private int maxRotatedWingParkingPlace;
    private List<String> fixedWingAirplaneIds;
    private List<String> helicopterIds;

    public Airport() {
        this.fixedWingAirplaneIds = new ArrayList<>();
        this.helicopterIds = new ArrayList<>();
    }

    public Airport(String id, String name, double runwaySize,
            int maxFixedWingParkingPlace, int maxRotatedWingParkingPlace) {
        this.id = id;
        this.name = name;
        this.runwaySize = runwaySize;
        this.maxFixedWingParkingPlace = maxFixedWingParkingPlace;
        this.maxRotatedWingParkingPlace = maxRotatedWingParkingPlace;
        this.fixedWingAirplaneIds = new ArrayList<>();
        this.helicopterIds = new ArrayList<>();
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRunwaySize() {
        return runwaySize;
    }

    public void setRunwaySize(double runwaySize) {
        this.runwaySize = runwaySize;
    }

    public int getMaxFixedWingParkingPlace() {
        return maxFixedWingParkingPlace;
    }

    public void setMaxFixedWingParkingPlace(int maxFixedWingParkingPlace) {
        this.maxFixedWingParkingPlace = maxFixedWingParkingPlace;
    }

    public int getMaxRotatedWingParkingPlace() {
        return maxRotatedWingParkingPlace;
    }

    public void setMaxRotatedWingParkingPlace(int maxRotatedWingParkingPlace) {
        this.maxRotatedWingParkingPlace = maxRotatedWingParkingPlace;
    }

    public List<String> getFixedWingAirplaneIds() {
        return fixedWingAirplaneIds;
    }

    public void setFixedWingAirplaneIds(List<String> fixedWingAirplaneIds) {
        this.fixedWingAirplaneIds = fixedWingAirplaneIds;
    }

    public List<String> getHelicopterIds() {
        return helicopterIds;
    }

    public void setHelicopterIds(List<String> helicopterIds) {
        this.helicopterIds = helicopterIds;
    }

    public boolean hasFixedWingCapacity() {
        return fixedWingAirplaneIds.size() < maxFixedWingParkingPlace;
    }

    public boolean hasHelicopterCapacity() {
        return helicopterIds.size() < maxRotatedWingParkingPlace;
    }

    public boolean isFixedWingParked(String fwId) {
        return fixedWingAirplaneIds.contains(fwId);
    }

    public boolean isHelicopterParked(String hcId) {
        return helicopterIds.contains(hcId);
    }

    public boolean canAccommodateRunway(double requiredRunway) {
        return runwaySize >= requiredRunway;
    }

    @Override
    public String toString() {
        return String.format(
                "Airport{id='%s', name='%s', runway=%.1f, maxFixed=%d, maxRotated=%d}",
                id, name, runwaySize, maxFixedWingParkingPlace, maxRotatedWingParkingPlace);
    }
}
