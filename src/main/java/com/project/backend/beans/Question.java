package com.project.backend.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String question;
    private char answer;
    @ElementCollection
    @CollectionTable(
        name = "options_table", 
        joinColumns = @JoinColumn(name = "options_id")
    )
    @MapKeyColumn(name = "options_tag")
    @Column(name = "options")
    private Map<Character, String> options;
}