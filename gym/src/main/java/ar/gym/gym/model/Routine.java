package ar.gym.gym.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "routines")
@ToString(exclude = {"client", "sessions", "trainingPlan"})  // Excluir las relaciones que causan la recursión
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private String name;
    private LocalDate creationDate;
    private boolean active;
    private boolean completed;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_plan_id")  // Columna que mapea la relación
    private TrainingPlan trainingPlan;
}
