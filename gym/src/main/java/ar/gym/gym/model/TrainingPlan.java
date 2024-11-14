package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "training_plans")
public class TrainingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;  // Este plan está asociado a un cliente

    private boolean active; // Saber cuál es el plan activo del cliente
    private String name;  // Nombre del plan de entrenamiento
    private LocalDate creationDate;  // Fecha de creación del plan
    private String description;  // Descripción general del plan

    @OneToMany(mappedBy = "trainingPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Routine> routines;  // Listado de rutinas asociadas al plan de entrenamiento
}
