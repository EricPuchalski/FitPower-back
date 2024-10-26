package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "gyms")
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String gymCode;
    private String name;
    private String phone;
    private String email;
    private String address;
    @OneToMany(mappedBy = "gym")
    private List<Client>clientList;
    @OneToMany(mappedBy = "gym")
    private List<Trainer>trainerList;
    @OneToMany(mappedBy = "gym")
    private List<Nutritionist>nutritionistList;

}
