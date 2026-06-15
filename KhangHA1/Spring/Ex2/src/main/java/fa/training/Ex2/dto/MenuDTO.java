package fa.training.Ex2.dto;

import lombok.Data;

@Data
public class MenuDTO {
    private Long id;
    private String name;
    private String url;
    private String icon;
    private Long parentId;
    private Integer displayOrder;
    private Boolean status;
}
