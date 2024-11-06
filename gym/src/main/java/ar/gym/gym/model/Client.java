package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "clients")
public class Client extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    private List<ClientStatus> status;
    private String goal;
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
    @ManyToOne
    @JoinColumn(name = "nutritionist_id")
    private Nutritionist nutritionist;
    @OneToMany(mappedBy = "client")
    private List<Routine>routines;
    @OneToMany(mappedBy = "client")
    private List<NutritionalPlan>nutritionalPlans;
//    @OneToOne
//    private java.lang.Record record;
    @OneToMany(mappedBy = "client")
    private List<TrainingDiary>trainingDiaryList;
    @OneToMany(mappedBy = "client")
    private List<NutritionalDiary>nutritionalDiaryList;
    private Boolean active = true;
}
