package ar.gym.gym.model;

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
    private float totalCaloriesConsumed;
    private Double dailyCalories;
    private String observations;
    private boolean completed;

    @OneToMany(mappedBy = "nutritionLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals;
}
