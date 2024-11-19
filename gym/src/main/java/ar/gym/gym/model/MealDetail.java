    package ar.gym.gym.model;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import lombok.Builder;
    import lombok.Data;

    @Data
    @Entity
    @Builder
    @Table(name = "meal_details")
    public class MealDetail {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "meal_id")
        @JsonBackReference  // Indica que este es el "lado hijo" que no debe ser serializado
        private Meal meal;

        @ManyToOne//debe ser manyToOne si o si
        @JoinColumn(name = "food_id")
        private Food food;

        private Integer quantity;
        private String measureUnit;
    }
