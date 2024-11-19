package ar.gym.gym.service;



public interface ProgressEvaluationService {
     double calculateIMC(String dni) ;

    double calculateBodyFatPercentage(String dni);

     double calculateMuscleMass(String dni);
}
