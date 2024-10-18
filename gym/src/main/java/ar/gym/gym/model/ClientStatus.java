package ar.gym.gym.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "client_status")
public class ClientStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double weight;
    private Double height;
    private Double bodymass;
    private Double bodyfat;
}
