package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nutritionists")
public class Nutritionist extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String profession;
    private boolean available;
    @OneToMany(mappedBy = "nutritionist")
    private List<Client>clients;
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;


}
