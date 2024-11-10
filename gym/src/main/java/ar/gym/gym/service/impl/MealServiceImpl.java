package ar.gym.gym.service.impl;

import ar.gym.gym.mapper.MealMapper;
import ar.gym.gym.repository.MealRepository;
import ar.gym.gym.service.MealService;
import org.springframework.stereotype.Service;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    public MealServiceImpl(MealRepository mealRepository, MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
    }


}
