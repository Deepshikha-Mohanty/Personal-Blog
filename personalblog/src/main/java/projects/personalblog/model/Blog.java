package projects.personalblog.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int blog_id;
    String title;
    @Column(columnDefinition = "TEXT")
    String content;
    String genre;
    LocalDateTime createdAt = LocalDateTime.now();

}
