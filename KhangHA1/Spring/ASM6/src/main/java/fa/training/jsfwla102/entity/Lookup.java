package fa.training.jsfwla102.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_lookup")
public class Lookup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "code", nullable = false)
    private Integer code;

    @Column(name = "label", nullable = false, length = 50)
    private String label;
}
