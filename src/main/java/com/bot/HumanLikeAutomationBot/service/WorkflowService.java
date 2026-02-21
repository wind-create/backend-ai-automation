package com.bot.HumanLikeAutomationBot.service;

import com.bot.HumanLikeAutomationBot.entity.Workflow;
import com.bot.HumanLikeAutomationBot.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;

    public List<Workflow> findAll() {
        return workflowRepository.findAll();
    }

    public Workflow findById(Long id) {
        return workflowRepository.findById(id).orElseThrow();
    }

    public Workflow create(Workflow workflow) {
        workflow.setCreatedAt(LocalDateTime.now());
        workflow.setUpdatedAt(LocalDateTime.now());
        return workflowRepository.save(workflow);
    }

    public Workflow update(Long id, Workflow req) {
        Workflow wf = workflowRepository.findById(id).orElseThrow();

        wf.setName(req.getName());
        wf.setJsonDefinition(req.getJsonDefinition());
        wf.setVersion(req.getVersion());
        wf.setStatus(req.getStatus());
        wf.setUpdatedAt(LocalDateTime.now());

        return workflowRepository.save(wf);
    }

    public void delete(Long id) {
        workflowRepository.deleteById(id);
    }
}