package ar.gym.gym.service.impl;

import ar.gym.gym.model.NutritionalPlan;
import ar.gym.gym.repository.NutritionalPlanRepository;

public class NutritionalPlanServiceImpl {
    private final NutritionalPlanRepository nutritionalPlanRepository;


    public NutritionalPlanServiceImpl(NutritionalPlanRepository nutritionalPlanRepository) {
        this.nutritionalPlanRepository = nutritionalPlanRepository;
    }

//    public List<NutritionalPlan> getAllPlans() {
//        return nutritionalPlanRepository.findAll();
//    }
//
//    public NutritionalPlan getPlanById(Long id) {
//        return nutritionalPlanRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Plan no encontrado"));
//    }
//
//    public NutritionalPlan savePlan(NutritionalPlan plan) {
//        return nutritionalPlanRepository.save(plan);
//    }
//
//    public void deletePlan(Long id) {
//        if (!nutritionalPlanRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Plan no encontrado");
//        }
//        nutritionalPlanRepository.deleteById(id);
//    }
//
//    public List<NutritionalPlan> getPlansByClientId(Long clientId) {
//        return nutritionalPlanRepository.findByClientId(clientId);
//    }
}
