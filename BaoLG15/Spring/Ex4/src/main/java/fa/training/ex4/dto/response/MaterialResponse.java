package fa.training.ex4.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialResponse {
    private Long material_id;
    private String file_name;
    private String stored_file_name;
    private Long file_size;
    private String file_type;
    private LocalDateTime upload_date;
    private String description;
}
