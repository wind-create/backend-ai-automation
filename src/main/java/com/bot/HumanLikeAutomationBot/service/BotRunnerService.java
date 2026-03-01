package com.bot.HumanLikeAutomationBot.service;

import com.bot.HumanLikeAutomationBot.entity.ActionScript;
import com.bot.HumanLikeAutomationBot.entity.BrainLogic;
import com.bot.HumanLikeAutomationBot.entity.Workflow;
import org.springframework.stereotype.Service;
import com.bot.HumanLikeAutomationBot.repository.ActionScriptRepository;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class BotRunnerService {

    private final ActionScriptRepository actionScriptRepository;
    // 🔥 multi process storage (REAL python process)
    private final Map<Long, Process> processMap = new ConcurrentHashMap<>();

    // 🔥 log buffer realtime
    private final Map<Long, List<String>> logMap = new ConcurrentHashMap<>();
    private static final long WATCHDOG_INTERVAL_MS = 3000;
    private static final long STOP_DELAY_MS = 10_000;

    public void startPython(
            Long sessionId,
            Workflow workflow,
            BrainLogic brain,

            ActionScript action,
            Consumer<Integer> onProcessStopped
    ) {

        try {

            // ==============================
            // PREPARE SESSION FOLDER
            // ==============================
            String basePath = "bot-runtime/session-" + sessionId + "/";
            new File(basePath).mkdirs();

            try(FileWriter fw = new FileWriter(basePath + "workflow.json")){
                fw.write(workflow.getJsonDefinition());
            }

            try(FileWriter fw = new FileWriter(basePath + "otak.py")){
                fw.write(brain.getScriptContent());
            }

            copyActionPy(basePath, action.getId());

            String title = "BOT-" + sessionId + "-" + workflow.getName();

            // ==============================
            // 🔥 1. START CMD WINDOW (VISUAL)
            // ==============================
            new ProcessBuilder(
                    "cmd.exe",
                    "/c",
                    "start",
                    "\"" + title + "\"",
                    "python",
                    "-u",
                    basePath + "action.py"
            ).start();

            // ==============================
            // 🔥 2. START REAL PYTHON PROCESS (MONITOR)
            // ==============================
            ProcessBuilder pb = new ProcessBuilder(
                    "python",
                    "-u",
                    basePath + "action.py"
            );

            pb.redirectErrorStream(true);

            Process process = pb.start();

            processMap.put(sessionId, process);
            logMap.put(sessionId, new ArrayList<>());

            streamLogs(sessionId, process.getInputStream());

            System.out.println("Python monitor started for session: " + sessionId);

            startWatchdog(sessionId, process, onProcessStopped);

            // ==============================
            // MONITOR EXIT
            // ==============================
            new Thread(() -> {
                try {

                    int exitCode = process.waitFor();

                    System.out.println("Session " + sessionId + " exited. Wait 10s...");

                    Thread.sleep(STOP_DELAY_MS);

                    processMap.remove(sessionId);

                    onProcessStopped.accept(exitCode);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            throw new RuntimeException("Failed start python", e);
        }
    }

    // ==============================
    // STREAM LOG REALTIME
    // ==============================
    private void streamLogs(Long sessionId, InputStream inputStream){

        new Thread(() -> {
            try(BufferedReader reader =
                        new BufferedReader(new InputStreamReader(inputStream))){

                String line;
                while((line = reader.readLine()) != null){

                    String logLine = "[SESSION " + sessionId + "] " + line;

                    System.out.println(logLine);

                    logMap.get(sessionId).add(logLine);
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    public List<String> getLogs(Long sessionId){
        return logMap.getOrDefault(sessionId, List.of());
    }
private void copyActionPy(String basePath, Long actionScriptId) throws IOException {

    var action = actionScriptRepository.findById(actionScriptId)
            .orElseThrow(() -> new RuntimeException("ActionScript not found"));

    File target = new File(basePath + "action.py");

    try (FileWriter fw = new FileWriter(target)) {
        fw.write(action.getScriptContent());
    }
}

    public void stopPython(Long sessionId){

        Process process = processMap.get(sessionId);

        if(process != null && process.isAlive()){
            process.destroy();
            processMap.remove(sessionId);
        }
    }

    public Set<Long> getActiveSessions(){
        return processMap.keySet();
    }

    private void startWatchdog(Long sessionId, Process process, Consumer<Integer> onProcessStopped){

    new Thread(() -> {

        try {

            while(true){

                Thread.sleep(WATCHDOG_INTERVAL_MS);

                if(!process.isAlive()){

                    int exitCode;
                    try{
                        exitCode = process.exitValue();
                    }catch(Exception e){
                        exitCode = -1;
                    }

                    System.out.println("WATCHDOG: CMD/Python closed for session " + sessionId);

                    processMap.remove(sessionId);

                    onProcessStopped.accept(exitCode);

                    break;
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }).start();
}
}