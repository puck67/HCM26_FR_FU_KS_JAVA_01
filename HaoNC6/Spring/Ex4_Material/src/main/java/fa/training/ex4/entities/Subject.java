package fa.training.ex4.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id", nullable = false)
    private Long subject_id;

    @Column(name = "subject_code", nullable = false, unique = true)
    private String subject_code;

    @Column(name = "subject_name", nullable = false)
    private String subject_name;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Material> materials;
}
