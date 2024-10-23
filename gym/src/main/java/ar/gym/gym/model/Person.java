package ar.gym.gym.model;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@MappedSuperclass
@NoArgsConstructor
public abstract class Person {
    private String dni;
    private String surname;
    private String name;
    private String phone;
    private String email;
    private String address;
    private boolean active;
}
