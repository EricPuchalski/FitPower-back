package ar.gym.gym.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@MappedSuperclass
@NoArgsConstructor
public abstract class Person {
    @Column(unique = true)
    private String dni;
    private String lastname;
    private String name;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String email;
    private String address;
    private boolean active;
}
