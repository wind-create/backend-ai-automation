package com.bot.HumanLikeAutomationBot.service;

import com.bot.HumanLikeAutomationBot.dto.StartBotRequest;
import com.bot.HumanLikeAutomationBot.entity.BotSession;
import com.bot.HumanLikeAutomationBot.repository.ActionScriptRepository;
import com.bot.HumanLikeAutomationBot.repository.BotSessionRepository;
import com.bot.HumanLikeAutomationBot.repository.WorkflowRepository;
import com.bot.HumanLikeAutomationBot.repository.BrainLogicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BotService {

    private final BotRunnerService botRunnerService;
    private final BotSessionRepository botSessionRepository;
    private final WorkflowRepository workflowRepository;
    private final BrainLogicRepository brainLogicRepository;
    private final ActionScriptRepository actionScriptRepository;

    public BotSession startBot(StartBotRequest req) {

    // 🔥 Cek apakah ada bot RUNNING
    botSessionRepository.findByStatus("RUNNING")
            .ifPresent(s -> {
                throw new RuntimeException("Bot already running");
            });

    var workflow = workflowRepository.findById(req.getWorkflowId())
            .orElseThrow(() -> new RuntimeException("Workflow not found"));

    var brain = brainLogicRepository.findById(req.getBrainId())
            .orElseThrow(() -> new RuntimeException("Brain not found"));

    var action = actionScriptRepository.findById(req.getActionScriptId())
            .orElseThrow(() -> new RuntimeException("ActionScript not found"));

    BotSession session = BotSession.builder()
            .workflowId(workflow.getId())
            .brainId(brain.getId())
            .actionScriptId(action.getId())   // ⭐ simpan di session
            .status("RUNNING")
            .startTime(LocalDateTime.now())
            .machineName(req.getMachineName() == null ? "LOCAL-PC" : req.getMachineName())
            .build();

    session = botSessionRepository.save(session);

    Long sessionId = session.getId();

    System.out.println("Starting BOT session: " + sessionId);

    botRunnerService.startPython(
            sessionId,
            workflow,
            brain,
            action,  // ⭐ kirim action ke runner
            (exitCode) -> handleProcessStopped(sessionId, exitCode)
    );

    return session;
}

    // =========================================
    // 🔥 DIPANGGIL JIKA PYTHON SELESAI / CMD DITUTUP
    // =========================================
    public void handleProcessStopped(Long sessionId, int exitCode) {

        botSessionRepository.findById(sessionId)
                .ifPresent(session -> {

                    // Jangan overwrite jika sudah STOP manual
                    if (!"RUNNING".equals(session.getStatus())) {
                        return;
                    }

                    if (exitCode == 0) {
                        session.setStatus("STOPPED");
                    } else {
                        session.setStatus("ERROR");
                    }

                    session.setEndTime(LocalDateTime.now());

                    botSessionRepository.save(session);

                    System.out.println("Session " + sessionId + " finished with status: " + session.getStatus());
                });
    }

    // =========================================
    // 🔥 STOP MANUAL
    // =========================================
    public void stopBot(Long sessionId) {

        BotSession session = botSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!"RUNNING".equals(session.getStatus())) {
            throw new RuntimeException("Bot is not running");
        }

        System.out.println("Stopping BOT session: " + sessionId);

        botRunnerService.stopPython(sessionId);

        session.setStatus("STOPPED");
        session.setEndTime(LocalDateTime.now());

        botSessionRepository.save(session);
    }

    // =========================================
// 🔥 LIST ALL SESSIONS
// =========================================
public List<BotSession> getAllSessions() {
    return botSessionRepository.findAll();
}

// =========================================
// 🔥 GET SESSION BY ID
// =========================================
public BotSession getSessionById(Long sessionId) {
    return botSessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));
}

// =========================================
// 🔥 GET ACTIVE (RUNNING) SESSION
// =========================================
public BotSession getActiveSession() {
    return botSessionRepository.findByStatus("RUNNING")
            .orElseThrow(() -> new RuntimeException("No active session"));
}
}