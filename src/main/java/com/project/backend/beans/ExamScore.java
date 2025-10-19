package com.project.backend.beans;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ManyToAny;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_score")
public class ExamScore {

    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String formatId;
    @ManyToAny
    private List<Question> questions;
    private int score;
}
