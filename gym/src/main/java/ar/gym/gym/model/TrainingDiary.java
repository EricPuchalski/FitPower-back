package ar.gym.gym.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "training_diary")
public class TrainingDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;  // Este diario está asociado a un cliente

    private LocalDateTime creationDate;  // Fecha y hora de la entrada en el diario
    private String observation;  // Notas del cliente o entrenador sobre el entrenamiento realizado

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private Routine routine;  // El diario está asociado a una rutina

    @OneToMany(mappedBy = "trainingDiary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions;  // Listado de sesiones registradas en este diario
}
