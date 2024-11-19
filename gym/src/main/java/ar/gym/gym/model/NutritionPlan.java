package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "nutrition_plans")
public class NutritionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "nutritionist_id")
    private Nutritionist nutritionist;
    private String type;
    private Date startDate;
    private Date endDate;
    private boolean active;
    private String recommendation; //recomendaciones del nutricionista, una especie de guia

    @Enumerated(EnumType.STRING)
    private NutritionStatus status; // Estado del plan (por ejemplo, ACTIVO, COMPLETADO, EN_PROGRESO)

    @OneToMany(mappedBy = "nutritionPlan", cascade = CascadeType.ALL)
    private List<NutritionLog> logs; // Registro de consumo diario asociado al plan

    private boolean completed; // Indica si el cliente pudo finalizar el plan de nutrición.
}
