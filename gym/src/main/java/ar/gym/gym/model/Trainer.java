package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "trainers")
public class Trainer extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String profession;
    private boolean available;
    @OneToMany(mappedBy = "trainer")
    private List<Routine>routineList;
    @OneToMany(mappedBy = "trainer")
    private List<Client>clients;
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;

}
