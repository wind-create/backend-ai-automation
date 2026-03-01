package com.bot.HumanLikeAutomationBot.service;

import com.bot.HumanLikeAutomationBot.entity.ActionScript;
import com.bot.HumanLikeAutomationBot.repository.ActionScriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionScriptService {

    private final ActionScriptRepository actionScriptRepository;

    public List<ActionScript> findAll() {
        return actionScriptRepository.findAll();
    }

    public ActionScript findById(Long id) {
        return actionScriptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ActionScript not found"));
    }

    public ActionScript create(ActionScript action) {
        action.setCreatedAt(LocalDateTime.now());
        action.setUpdatedAt(LocalDateTime.now());
        return actionScriptRepository.save(action);
    }

    public ActionScript update(Long id, ActionScript req) {

        ActionScript action = actionScriptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ActionScript not found"));

        action.setName(req.getName());
        action.setScriptContent(req.getScriptContent());
        action.setVersion(req.getVersion());
        action.setStatus(req.getStatus());
        action.setUpdatedAt(LocalDateTime.now());

        return actionScriptRepository.save(action);
    }

    public void delete(Long id) {

        if (!actionScriptRepository.existsById(id)) {
            throw new RuntimeException("ActionScript not found");
        }

        actionScriptRepository.deleteById(id);
    }
}