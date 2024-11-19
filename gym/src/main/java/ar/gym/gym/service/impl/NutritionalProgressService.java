package ar.gym.gym.service.impl;

import ar.gym.gym.repository.NutritionLogRepository;
import ar.gym.gym.repository.NutritionPlanRepository;
import org.springframework.stereotype.Service;

@Service
public class NutritionalProgressService {

    private final NutritionLogServiceImpl nutritionLogService;
    private final NutritionPlanServiceImpl nutritionPlanService;
    private final NutritionLogRepository nutritionLogRepository;
    private final NutritionPlanRepository nutritionPlanRepository;
    //Agregar lo que se necesita conforme se realice la clase..

    public NutritionalProgressService(NutritionLogServiceImpl nutritionLogService,
                                      NutritionPlanServiceImpl nutritionPlanService,
                                      NutritionLogRepository nutritionLogRepository,
                                      NutritionPlanRepository nutritionPlanRepository) {

        this.nutritionLogService = nutritionLogService;
        this.nutritionPlanService = nutritionPlanService;
        this.nutritionLogRepository = nutritionLogRepository;
        this.nutritionPlanRepository = nutritionPlanRepository;
    }

    //Agregar métodos para realizar métricas y verificar si los objetivos diarios del plan de nutrición se cumplen o no.
    //Agregar métodos para verificar el progreso del cliente, por ejemplo, si el cliente actualiza su clientStatus ver si progresó en base a su objetivo o no.
}
