package com.bot.HumanLikeAutomationBot.controller;

import com.bot.HumanLikeAutomationBot.entity.BrainLogic;
import com.bot.HumanLikeAutomationBot.service.BrainLogicService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brains")
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
@RequiredArgsConstructor
public class BrainLogicController {

    private final BrainLogicService brainLogicService;

    @GetMapping
    public List<BrainLogic> all() {
        return brainLogicService.findAll();
    }

    @GetMapping("/{id}")
    public BrainLogic byId(@PathVariable Long id) {
        return brainLogicService.findById(id);
    }

    @PostMapping
    public BrainLogic create(@RequestBody BrainLogic brain) {
        return brainLogicService.create(brain);
    }

    @PutMapping("/{id}")
    public BrainLogic update(@PathVariable Long id,
                             @RequestBody BrainLogic brain) {
        return brainLogicService.update(id, brain);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        brainLogicService.delete(id);
    }
}