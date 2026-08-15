package projects.personalblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projects.personalblog.model.Blog;

import java.util.List;

@Repository
public interface blogrepository extends JpaRepository<Blog, Integer> {

    @Query("SELECT b FROM Blog b WHERE b.genre = :genre ORDER BY b.createdAt DESC")
    List<Blog> findByGenre(@Param("genre") String genre);

    @Query("SELECT b FROM Blog b WHERE b.title = :title")
    Blog findByTitle(String title);

    List<Blog> findAllByOrderByCreatedAtDesc();

    @Query("SELECT b FROM Blog b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(b.genre) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY b.createdAt DESC")
    List<Blog> search(@Param("keyword") String keyword);
}
