package fa.training.hungplt1.asm6.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_lookup")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
