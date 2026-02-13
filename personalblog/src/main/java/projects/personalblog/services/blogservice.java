package projects.personalblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.personalblog.model.Blog;
import projects.personalblog.repositories.blogrepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
@Service
public class blogservice {

    @Autowired
    private blogrepository r;

    public ArrayList<Blog> sendAllBlogs() {
        ArrayList<Blog> all= (ArrayList<Blog>) r.findAll();
        return all;
    }

    public ArrayList<Blog> sendBlogGenre(String genre) {
        ArrayList<Blog> all= r.findByGenre(genre);
        return all;
    }

    public void save(Blog blog) {
        r.save(blog);
    }

    public void remove(int id) {
        r.deleteById(id);
    }

    public void update(int id,Blog blog) {
       /* Optional<Blog> newblog=r.findById(id);
        newblog.setTitle(blog.getTitle());
        newblog.setGenre(blog.getGenre());
        newblog.setContent(blog.getContent());
        newblog.setCreatedAt(LocalDateTime.now());
        r.save(newblog);*/
    }

    public Blog sendBlogTitle(String title) {
        Blog a= r.findByTitle(title);
        return a;
    }

    public Optional<Blog> sendBlogId(int id) {
        Optional<Blog> a= r.findById(id);
        return a;
    }
}
