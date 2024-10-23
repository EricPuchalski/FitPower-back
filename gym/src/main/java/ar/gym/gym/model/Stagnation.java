package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "stagnations")
public class Stagnation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
//    private StagnationType type;
    private Double currentWeight;
    private Double bodyMass;
    private String observations;
    @ManyToOne
    @JoinColumn(name = "record_id")
    private Record record;

}