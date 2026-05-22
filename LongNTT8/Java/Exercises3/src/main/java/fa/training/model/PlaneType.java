package fa.training.model;

public enum PlaneType {

    CAG("Cargo"),
    LGR("Large"),
    PRV("Private");

    private final String displayName;

    PlaneType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PlaneType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Plane type cannot be null or blank");
        }

        return switch (value.trim().toUpperCase()) {
            case "CAG" -> CAG;
            case "LGR" -> LGR;
            case "PRV" -> PRV;
            default -> throw new IllegalArgumentException(
                    "Invalid plane type: '" + value + "'. Must be CAG, LGR, or PRV");
        };
    }

    public static boolean isValid(String value) {
        try {
            fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
