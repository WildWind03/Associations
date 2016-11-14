package com.chirikhin.association;

public class RoundEndedEvent {
    private final int finalScore;

    public RoundEndedEvent(int finalScore) {
        this.finalScore = finalScore;
    }

    public int getFinalScore() {
        return finalScore;
    }
}
