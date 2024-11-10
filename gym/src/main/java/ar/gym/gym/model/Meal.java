package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "meals")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MealTime mealTime; // Tiempo de la comida, como desayuno, almuerzo, etc.


    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private List<Food> foods; // Lista de alimentos en esta comida

    @ManyToOne
    @JoinColumn(name = "nutrition_plan_id")
    private NutritionPlan nutritionPlan; // Plan de nutrición al que pertenece esta comida

    private boolean completed; // Indica si el cliente consumió esta comida
}
