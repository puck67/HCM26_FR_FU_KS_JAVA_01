package fa.training.TrainingMaterialManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardDTO {

    private long totalSubjects;

    private long totalMaterials;

    private long totalStorage;
}