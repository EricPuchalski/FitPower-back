package ar.gym.gym.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class AgeCalculator {
    public static int calculateAge(LocalDate birthDate) {
        if (birthDate != null) {
            return Period.between(birthDate, LocalDate.now()).getYears();
        } else {
            return 0; // O manejar el caso de fecha de nacimiento nula de otra manera
        }
    }
}
