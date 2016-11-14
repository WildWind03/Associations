package com.chirikhin.association;

public class NewRoundEvent {
    private final String teamName;

    public NewRoundEvent(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }
}
