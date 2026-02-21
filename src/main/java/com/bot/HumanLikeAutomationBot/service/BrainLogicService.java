package com.bot.HumanLikeAutomationBot.service;

import com.bot.HumanLikeAutomationBot.entity.BrainLogic;
import com.bot.HumanLikeAutomationBot.repository.BrainLogicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrainLogicService {

    private final BrainLogicRepository brainLogicRepository;

    public List<BrainLogic> findAll() {
        return brainLogicRepository.findAll();
    }

    public BrainLogic findById(Long id) {
        return brainLogicRepository.findById(id).orElseThrow();
    }

    public BrainLogic create(BrainLogic brain) {
        brain.setCreatedAt(LocalDateTime.now());
        brain.setUpdatedAt(LocalDateTime.now());
        return brainLogicRepository.save(brain);
    }

    public BrainLogic update(Long id, BrainLogic req) {
        BrainLogic brain = brainLogicRepository.findById(id).orElseThrow();

        brain.setName(req.getName());
        brain.setScriptContent(req.getScriptContent());
        brain.setVersion(req.getVersion());
        brain.setStatus(req.getStatus());
        brain.setUpdatedAt(LocalDateTime.now());

        return brainLogicRepository.save(brain);
    }

    public void delete(Long id) {
        brainLogicRepository.deleteById(id);
    }
}