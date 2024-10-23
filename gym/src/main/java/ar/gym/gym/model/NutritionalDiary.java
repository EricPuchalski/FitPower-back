package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "nutritional_diary")
public class NutritionalDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "Client_id")
    private Client client;
    private LocalDateTime date;
    private float totalCaloriesConsumed;
    private String observations;
    private boolean completed;
}
