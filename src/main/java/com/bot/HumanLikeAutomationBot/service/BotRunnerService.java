package com.bot.HumanLikeAutomationBot.service;

import com.bot.HumanLikeAutomationBot.entity.BrainLogic;
import com.bot.HumanLikeAutomationBot.entity.Workflow;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

@Service
public class BotRunnerService {

    private Process process;

    private static final long STOP_DELAY_MS = 10_000;

    public void startPython(
            Workflow workflow,
            BrainLogic brain,
            Consumer<Integer> onProcessStopped // 🔥 CALLBACK
    ) {

        try {

            // ==============================
            // SIMPAN FILE
            // ==============================
            try (FileWriter fw = new FileWriter("bot-runtime/workflow.json")) {
                fw.write(workflow.getJsonDefinition());
            }

            try (FileWriter fw = new FileWriter("bot-runtime/otak.py")) {
                fw.write(brain.getScriptContent());
            }

            // ==============================
            // START PYTHON
            // ==============================
            ProcessBuilder pb = new ProcessBuilder(
                    "python",
                    "-u",
                    "bot-runtime\\action.py"
            );

            pb.redirectErrorStream(true);

            process = pb.start();

            System.out.println("Python bot started...");

            // ==============================
            // MONITOR THREAD
            // ==============================
            new Thread(() -> {
                try {

                    int exitCode = process.waitFor();

                    System.out.println("Python exited. Waiting 10 seconds...");

                    Thread.sleep(STOP_DELAY_MS);

                    // 🔥 CALL CALLBACK TANPA INJECT SERVICE
                    onProcessStopped.accept(exitCode);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            throw new RuntimeException("Failed to start python bot", e);
        }
    }

    public void stopPython() {
        if (process != null && process.isAlive()) {
            process.destroy();
        }
    }
}