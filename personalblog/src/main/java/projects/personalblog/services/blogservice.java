package projects.personalblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.personalblog.model.Blog;
import projects.personalblog.repositories.blogrepository;

import java.util.List;
import java.util.Optional;

@Service
public class blogservice {

    @Autowired
    private blogrepository r;

    public List<Blog> sendAllBlogs() {
        return r.findAllByOrderByCreatedAtDesc();
    }

    public List<Blog> sendBlogGenre(String genre) {
        return r.findByGenre(genre);
    }

    public List<Blog> search(String keyword) {
        return r.search(keyword);
    }

    public void save(Blog blog) {
        r.save(blog);
    }

    public void remove(int id) {
        r.deleteById(id);
    }

    public void update(int id, Blog blog) {
        Optional<Blog> existing = r.findById(id);
        if (existing.isPresent()) {
            Blog toUpdate = existing.get();
            toUpdate.setTitle(blog.getTitle());
            toUpdate.setGenre(blog.getGenre());
            toUpdate.setContent(blog.getContent());
            // createdAt is intentionally preserved (not overwritten on edit)
            r.save(toUpdate);
        }
    }

    public Blog sendBlogTitle(String title) {
        return r.findByTitle(title);
    }

    public Optional<Blog> sendBlogId(int id) {
        return r.findById(id);
    }
}
