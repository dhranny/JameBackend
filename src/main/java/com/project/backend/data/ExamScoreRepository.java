package com.project.backend.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.beans.ExamScore;

public interface ExamScoreRepository extends JpaRepository<ExamScore, String>{
    
}
