package com.bot.HumanLikeAutomationBot.service;

import com.bot.HumanLikeAutomationBot.dto.StartBotRequest;
import com.bot.HumanLikeAutomationBot.entity.BotSession;
import com.bot.HumanLikeAutomationBot.repository.BotSessionRepository;
import com.bot.HumanLikeAutomationBot.repository.WorkflowRepository;
import com.bot.HumanLikeAutomationBot.repository.BrainLogicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BotService {

    private final BotRunnerService botRunnerService;
    private final BotSessionRepository botSessionRepository;
    private final WorkflowRepository workflowRepository;
    private final BrainLogicRepository brainLogicRepository;

    public BotSession startBot(StartBotRequest req) {

        // ==============================
        // 1. CEK BOT RUNNING
        // ==============================
        botSessionRepository.findByStatus("RUNNING")
                .ifPresent(s -> {
                    throw new RuntimeException("Bot already running");
                });

        // ==============================
        // 2. AMBIL WORKFLOW & BRAIN
        // ==============================
        var workflow = workflowRepository.findById(req.getWorkflowId())
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        var brain = brainLogicRepository.findById(req.getBrainId())
                .orElseThrow(() -> new RuntimeException("Brain not found"));

        // ==============================
        // 3. BUAT SESSION RUNNING
        // ==============================
        BotSession session = BotSession.builder()
                .workflowId(workflow.getId())
                .brainId(brain.getId())
                .status("RUNNING")
                .startTime(LocalDateTime.now())
                .machineName(req.getMachineName() == null ? "LOCAL-PC" : req.getMachineName())
                .build();

        session = botSessionRepository.save(session);

        // ==============================
        // 4. START PYTHON
        // ==============================
        botRunnerService.startPython(
        workflow,
        brain,
        this::handleProcessStopped   // 🔥 callback method reference
);

        return session;
    }

    // ==============================
    // 🔥 CALLBACK DARI RUNNER
    // ==============================
    public void handleProcessStopped(int exitCode) {

        botSessionRepository.findByStatus("RUNNING")
                .ifPresent(session -> {

                    if(exitCode == 0){
                        session.setStatus("STOPPED");
                    }else{
                        session.setStatus("ERROR");
                    }

                    session.setEndTime(LocalDateTime.now());

                    botSessionRepository.save(session);
                });
    }
}