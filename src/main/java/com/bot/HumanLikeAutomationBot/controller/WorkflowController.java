package com.bot.HumanLikeAutomationBot.controller;

import com.bot.HumanLikeAutomationBot.entity.Workflow;
import com.bot.HumanLikeAutomationBot.service.WorkflowService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workflows")
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    @GetMapping
    public List<Workflow> all() {
        return workflowService.findAll();
    }

    @GetMapping("/{id}")
    public Workflow byId(@PathVariable Long id) {
        return workflowService.findById(id);
    }

    @PostMapping
    public Workflow create(@RequestBody Workflow workflow) {
        return workflowService.create(workflow);
    }

    @PutMapping("/{id}")
    public Workflow update(@PathVariable Long id,
                           @RequestBody Workflow workflow) {
        return workflowService.update(id, workflow);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        workflowService.delete(id);
    }
}