package io.dogsbean.mafia.game.law;

public class Crime {
    private String name;
    private String description;
    private int severity;

    public Crime(String name, String description, int severity) {
        this.name = name;
        this.description = description;
        this.severity = severity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSeverity() {
        return severity;
    }
}
