package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "nutritionists")
public class Nutritionist extends Person {
    private String profession;
    private boolean available;
    @OneToMany(mappedBy = "nutritionist")
    private List<NutritionalPlan> nutritionalPlanList;
    @OneToMany(mappedBy = "nutritionist")
    private List<Client>clients;
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;


}
