package io.dogsbean.mafia.util;

import io.dogsbean.mafia.Main;
import io.dogsbean.mafia.npc.Personality;
import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.models.OllamaResult;
import io.github.amithkoujalgi.ollama4j.core.utils.OptionsBuilder;
import io.github.amithkoujalgi.ollama4j.core.utils.PromptBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class LlamaAPI {

    private Map<Player, Long> cooldowns = new HashMap<>();

    public void sendLlamaMessage(Personality personality, Player player, String message) {
        long currentTime = System.currentTimeMillis();

        if (cooldowns.containsKey(player) && (currentTime - cooldowns.get(player) < 1000)) {
            player.sendMessage("잠시 후에 다시 시도하세요.");
            return;
        }

        cooldowns.put(player, currentTime);

        try {
            CompletableFuture.supplyAsync(() -> {
                        try {
                            String prompt = ("먼저, 오직 한국어로 대답하세요. 메시지의 출처에 대해 언급하거나 코멘트하지 마세요. " +
                                    "문법 오류를 수정할 수 있지만, 텍스트를 너무 많이 변경하지 말고 변경했다는 사실을 알려주지 마세요. " +
                                    "제공된 프롬프트 메시지 외에 사용자와 대화하는 것을 피하세요. " +
                                    "당신은 그 상황에 몰입해야 합니다: '이 경우 어떻게 반응할 것인가? (예: '아야! 아프다!') " +
                                    "또한, 감정과 반응을 표현하려고 노력하고, " +
                                    "당신이 정말 그 상황에 처해 있는 것처럼 자연스럽고 진정성 있는 응답이 되도록 하세요. " +
                                    "상황에 대한 대답 외에 추가적인 정보를 제공하지 마세요. " +
                                    "상황에 맞지 않는 단어 사용과 반응을 하지 마세요. 당신은 지금 상황을 만든 플레이어와 " + personality + "한 시민입니다.");

                            PromptBuilder promptBuilder = Main.getInstance().getPromptBuilder()
                                    .addLine(prompt)
                                    .addLine("``````")
                                    .addSeparator()
                                    .add(message);

                            String model = "Llama3";

                            OllamaResult aiResponse = Main.getInstance().getOllamaAPI().generate(model, promptBuilder.build(), new OptionsBuilder().build());

                            if (aiResponse == null) {
                                return null;
                            }

                            String aiMessage = aiResponse.getResponse();
                            return new Pair<>(aiMessage, player);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .thenAccept(result -> {
                        if (result != null) {
                            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                Player p = result.getTwo();
                                String aiMessage = result.getOne();
                                p.sendMessage(aiMessage);
                            });
                        }
                    })
                    .exceptionally(throwable -> {
                        handleError(throwable);
                        return null;
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Void handleError(Throwable throwable) {
        throwable.printStackTrace();
        return null;
    }

    private void handleResponse(Pair<String, Player> response) {
        Pair<String, Player> playerAndResponse = response;
        if (playerAndResponse == null) {
            // 응답이 null인 경우 처리
            return; // 또는 적절한 오류 메시지 전송
        }

        Player receiver = playerAndResponse.getTwo();
        if (receiver.isOnline()) {
            String message = playerAndResponse.getOne();
            String rawAnswerString = "§r%PLAYER% §r: §r§b%TRANSLATION%";

            rawAnswerString = rawAnswerString.replace("%TRANSLATION%", message);
            rawAnswerString = rawAnswerString.replace("%PLAYER%", receiver.getDisplayName());

            Bukkit.broadcastMessage(rawAnswerString);
        }

    }
}
