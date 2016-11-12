package com.chirikhin.association;

public class TeamNamesTypedEvent {
    private final String firstTeamName;
    private final String secondTeamName;

    public TeamNamesTypedEvent(String firstTeamName, String secondTeamName) {
        this.firstTeamName = firstTeamName;
        this.secondTeamName = secondTeamName;
    }

    public String getFirstTeamName() {
        return firstTeamName;
    }

    public String getSecondTeamName() {
        return secondTeamName;
    }
}
