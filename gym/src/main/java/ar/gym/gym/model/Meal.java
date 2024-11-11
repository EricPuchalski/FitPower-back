package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;

@Data
@Entity
@Table(name = "meals")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MealTime mealTime; // Tiempo de la comida, como desayuno, almuerzo, etc.

    @ElementCollection
    @CollectionTable(name = "meal_foods", joinColumns = @JoinColumn(name = "meal_id"))
    @MapKeyJoinColumn(name = "food_id")
    @Column(name = "quantity")
    private Map<Food, Integer> foods; // Mapa de alimentos y sus cantidades en esta comida

    private String measureUnit;   // Unidad de medida, como "grams", "ml", o "piece"

    @ManyToOne
    @JoinColumn(name = "nutrition_log_id")
    private NutritionLog nutritionLog; // Registro de nutrición al que pertenece esta comida

    private boolean completed; // Indica si el cliente consumió esta comida
}