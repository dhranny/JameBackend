package com.project.backend.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.beans.Question;
import com.project.backend.beans.Source;

public interface SourceRepository extends JpaRepository<Source, String> {
    List<Question> findByName(String name);
}