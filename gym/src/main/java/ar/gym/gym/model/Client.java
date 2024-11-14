package ar.gym.gym.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<ClientStatus> statuses;
    private String goal;

    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "nutritionist_id")
    private Nutritionist nutritionist;

    // Cambiamos de List<Routine> a List<TrainingPlan>
    @JsonManagedReference
    @OneToMany(mappedBy = "client")
    private List<TrainingPlan> trainingPlans;

    @JsonManagedReference
    @OneToMany(mappedBy = "client")
    private List<NutritionalPlan> nutritionalPlans;

    @JsonManagedReference
    @OneToMany(mappedBy = "client")
    private List<TrainingDiary> trainingDiaryList;

    @JsonManagedReference
    @OneToMany(mappedBy = "client")
    private List<NutritionalDiary> nutritionalDiaryList;

    @JsonManagedReference
    @OneToMany(mappedBy = "client")
    private List<Notification> notifications;
}
