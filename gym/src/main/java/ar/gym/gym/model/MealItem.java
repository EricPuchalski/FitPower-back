package ar.gym.gym.model;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
public class MealItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    private Integer quantity;
    private String measureUnit;
}
