package fa.training.Ex2.utils;

import fa.training.Ex2.dto.MenuDTO;

public class Validator {
    private Validator() {
    }

    public static void validateMenu(MenuDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Menu is required");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Menu name is required");
        }
        if (dto.getUrl() == null || dto.getUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("Menu URL is required");
        }
        if (dto.getDisplayOrder() == null) {
            throw new IllegalArgumentException("Menu display order is required");
        }
        if (dto.getDisplayOrder() < 0) {
            throw new IllegalArgumentException("Display order must be positive");
        }
    }
}
