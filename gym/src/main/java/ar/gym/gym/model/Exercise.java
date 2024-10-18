package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
@Table(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String equipment;
    private float weight;
    private LocalTime restTime;
    private LocalTime duration;
    @OneToMany(mappedBy = "session")
    private Session session;
}


