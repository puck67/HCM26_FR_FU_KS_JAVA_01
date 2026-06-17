package fa.training.ex4.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "materials")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id", nullable = false)
    private Long material_id;

    @Column(name = "file_name", nullable = false)
    private String file_name;

    @Column(name = "stored_file_name", nullable = false)
    private String stored_file_name;

    @Column(name = "file_size", nullable = false)
    private Long file_size;

    @Column(name = "file_type", nullable = false)
    private String file_type;

    @CreationTimestamp
    @Column(name = "upload_date", nullable = false, updatable = false)
    private LocalDateTime upload_date;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
}
