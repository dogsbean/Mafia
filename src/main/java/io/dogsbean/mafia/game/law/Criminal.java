package io.dogsbean.mafia.game.law;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Criminal {
    private static Map<String, List<Crime>> crimeRecords = new HashMap<>();

    public static void commitCrime(String playerName, Crime crime) {
        crimeRecords.putIfAbsent(playerName, new ArrayList<>());
        crimeRecords.get(playerName).add(crime);
    }

    public static String getMostWanted() {
        String mostWanted = null;
        int highestSeverity = 0;

        for (Map.Entry<String, List<Crime>> entry : crimeRecords.entrySet()) {
            int totalSeverity = entry.getValue().stream().mapToInt(Crime::getSeverity).sum();
            if (totalSeverity > highestSeverity) {
                highestSeverity = totalSeverity;
                mostWanted = entry.getKey();
            }
        }

        return mostWanted;
    }

    public static String getCrimes(String playerName) {
        List<Crime> crimes = crimeRecords.getOrDefault(playerName, new ArrayList<>());
        if (crimes.isEmpty()) {
            return "범죄 기록이 없습니다.";
        }

        // 범죄 목록을 쉼표로 구분된 문자열로 변환
        StringBuilder crimeList = new StringBuilder();
        for (Crime crime : crimes) {
            crimeList.append(crime.getDescription()).append(", ");
        }

        // 마지막 쉼표 및 공백 제거
        crimeList.setLength(crimeList.length() - 2);
        return crimeList.toString();
    }

    public static void clearCrimes() {
        crimeRecords.clear();
    }
}
