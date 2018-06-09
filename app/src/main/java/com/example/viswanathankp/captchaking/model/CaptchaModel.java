package com.example.viswanathankp.captchaking.model;

import org.parceler.Parcel;

@Parcel
public class CaptchaModel {
    public int id;
    public String name;
    public int difficulty;
    public String answer;

    public boolean isCorrectAnswer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAnswer() {
        return answer;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }
}
