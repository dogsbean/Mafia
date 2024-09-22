package io.dogsbean.mafia.game.law;

import java.util.*;

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

        Set<String> uniqueCrimes = new HashSet<>();
        StringBuilder crimeList = new StringBuilder();

        for (Crime crime : crimes) {
            String description = crime.getDescription();
            if (uniqueCrimes.add(description)) {
                crimeList.append(description).append(", ");
            }
        }

        if (crimeList.length() == 0) {
            return "범죄 기록이 없습니다.";
        }

        crimeList.setLength(crimeList.length() - 2);
        return crimeList.toString();
    }

    public static void clearCrimes() {
        crimeRecords.clear();
    }
}
