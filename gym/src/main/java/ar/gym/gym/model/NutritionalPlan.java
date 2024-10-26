package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name= "nutritional_plans")
public class NutritionalPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    private String type;
    private Date startDate;
    private Date endDate;
    private String description;
    private Double dailyCalories;
    private boolean active;
    @OneToMany(mappedBy = "nutritionalPlan")
    private List<Food> foodList;


}
