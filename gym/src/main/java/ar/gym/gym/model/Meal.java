package ar.gym.gym.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference  // Indica que este es el "lado padre" en la relación
    private List<MealDetail> mealDetails;
}