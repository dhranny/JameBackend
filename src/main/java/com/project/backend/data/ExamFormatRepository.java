package com.project.backend.data;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.backend.beans.ExamFormat;

public interface ExamFormatRepository extends JpaRepository<ExamFormat, String>{
    
}
