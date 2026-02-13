package projects.personalblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projects.personalblog.model.Blog;

import java.util.ArrayList;

@Repository
public interface blogrepository extends JpaRepository<Blog,Integer> {
    @Query(value="SELECT b FROM Blog b WHERE b.genre= :genre")
    ArrayList<Blog> findByGenre(String genre);

    @Query(value="SELECT b FROM Blog b WHERE b.title= :title")
    Blog findByTitle(String title);
}
