package com.sib.sayitbettr.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "evaluation")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @Column(nullable = false)
    private String pronunciation;

    @Column(nullable = false)
    private double accuracy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private int score;



    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Word getWord() {
        return word;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getScore() {
        return score;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
