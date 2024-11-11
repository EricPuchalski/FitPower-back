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

    @ManyToOne
    @JoinColumn(name = "nutrition_log_id")
    private NutritionLog nutritionLog; // Registro de nutrición al que pertenece esta comida

    private boolean completed; // Indica si el cliente consumió esta comida

    // Referencia a la colección de MealItem (opcional)
    @OneToMany(mappedBy = "meal")
    private List<MealItem> mealItems;
}