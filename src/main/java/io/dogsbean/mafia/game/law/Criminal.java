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

    public static List<Crime> getCrimes(String playerName) {
        return crimeRecords.getOrDefault(playerName, new ArrayList<>());
    }

    public static void clearCrimes() {
        crimeRecords.clear();
    }
}
