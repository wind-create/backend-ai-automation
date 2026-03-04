package com.bot.HumanLikeAutomationBot.controller;

import com.bot.HumanLikeAutomationBot.entity.ActionScript;
import com.bot.HumanLikeAutomationBot.service.ActionScriptService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
public class ActionScriptController {

    private final ActionScriptService actionScriptService;

    @GetMapping
    public List<ActionScript> all() {
        return actionScriptService.findAll();
    }

    @GetMapping("/{id}")
    public ActionScript byId(@PathVariable Long id) {
        return actionScriptService.findById(id);
    }

    @PostMapping
    public ActionScript create(@RequestBody ActionScript action) {
        return actionScriptService.create(action);
    }

    @PutMapping("/{id}")
    public ActionScript update(@PathVariable Long id,
                               @RequestBody ActionScript action) {
        return actionScriptService.update(id, action);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        actionScriptService.delete(id);
    }
}