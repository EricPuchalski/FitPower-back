package ar.gym.gym.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "nutrition_logs")
public class NutritionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nutrition_plan_id")
    private NutritionPlan nutritionPlan;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDateTime date;
    private Float totalCaloriesConsumed;
    private Float dailyCalories;
    private String observations; //esto lo hace el nutricionista, el nutricionista puede ver los logs diarios del cliente
    private boolean completed;
    @OneToMany(mappedBy = "nutritionLog", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference  // Indica que este es el "lado hijo" que no debe ser serializado
    private List<Meal> meals;
}
