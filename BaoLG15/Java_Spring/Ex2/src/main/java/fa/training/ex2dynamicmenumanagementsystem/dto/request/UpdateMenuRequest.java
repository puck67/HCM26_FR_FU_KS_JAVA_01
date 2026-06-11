package fa.training.ex2dynamicmenumanagementsystem.dto.request;

import lombok.Data;

@Data
public class UpdateMenuRequest {
    private Long id;
    private String name;
    private String url;
    private String icon;
    private Long parentId;
    private Integer displayOrder;
    private Boolean status;
    private String roles;
}
