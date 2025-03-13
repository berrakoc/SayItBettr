package com.sib.sayitbettr.Model;

import java.time.LocalDateTime;

public class EvaluationRequest {
    private Long userId;
    private Long wordId;
    private String pronunciation;
    private double accuracy;
    private int score;
    private LocalDateTime date;

    // Getter ve Setter'lar
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getWordId() { return wordId; }
    public void setWordId(Long wordId) { this.wordId = wordId; }

    public String getPronunciation() { return pronunciation; }
    public void setPronunciation(String pronunciation) { this.pronunciation = pronunciation; }

    public double getAccuracy() { return accuracy; }
    public void setAccuracy(double accuracy) { this.accuracy = accuracy; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}

