package com.serengeti.hyu.backend.character.enums;

public enum ResponseOption {
    STRONGLY_AGREE("매우그렇다"),
    AGREE("그렇다"),
    DISAGREE("그렇지않다"),
    STRONGLY_DISAGREE("매우그렇지 않다");

    private final String text;

    ResponseOption(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

