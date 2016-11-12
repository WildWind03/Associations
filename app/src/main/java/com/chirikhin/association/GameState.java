package com.chirikhin.association;

public enum GameState {
    MAIN_MENU(0), TEAM_WINDOW(1), GAME_WINDOW(2);

    private final int num;

    GameState(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
