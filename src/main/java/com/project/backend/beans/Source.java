
package com.project.backend.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "source")
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String type;
    private String author;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "source_questions",
            joinColumns = @JoinColumn(name = "source_id"),
            inverseJoinColumns = @JoinColumn(name = "questions_id   ")
    )
    private List<Question> questions;
}
