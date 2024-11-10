package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "foods")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double calories;
    private Double protein;       // proteína
    private Double carbs;         // carbohidratos
    private Double fats;          // grasas
    private Double fiber;         // fibra
    private Double sugar;         // azúcar
    private Double sodium;        // sodio
    private Double iron;          // hierro
    private Double potassium;     // potasio
    private Double calcium;       // calcio


    @Enumerated(EnumType.STRING)
    private FoodCategory category; // Categoría del alimento

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal; // Comida a la que pertenece este alimento

    private boolean completed; // Indica si se consumió
}
