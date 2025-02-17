package com.sib.sayitbettr.Model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_EMPTY) // Boş nesneleri JSON'a dahil etme
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "word")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter //bu neden var anlamadım ama silmeye korktum
    @Column(name="title")
    private String title;

    @Column(name="level")
    private int level;

    @Column(name="phonetic")
    private String phonetic;

    @Column(name="meaning")
    private String meaning;

    @Column(name="pronunciation")
    private String pronunciation;

    @Column(name="wordType")
    private String wordType;

    @ManyToOne
    @JoinColumn(name = "owneruserid_fk",referencedColumnName = "id")
    private User ownerUserIdFk;

    @Column(name = "tarih")
    private LocalDate tarih;

    public Long getId() {return id;}
    public String getTitle() { return title; }
    public int getLevel() { return level; }
    public String getPhonetic() { return phonetic; }
    public String getMeaning() { return meaning; }
    public String getPronunciation() { return pronunciation; }
    public String getWordType() { return wordType; }
    public LocalDate getTarih() { return tarih; }

}

