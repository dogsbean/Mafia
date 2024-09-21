package io.dogsbean.mafia.npc;

public enum Personality {
    FRIENDLY, NEUTRAL, HOSTILE;

    public String respondToQuestion() {
        switch (this) {
            case FRIENDLY: return "믿을 수 있는 정보를 제공함.";
            case NEUTRAL: return "일반적인 대답.";
            case HOSTILE: return "정보를 주지 않거나 거짓 정보를 제공함.";
            default: return "반응 없음.";
        }
    }
}
