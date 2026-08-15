package projects.personalblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projects.personalblog.model.Blog;
import projects.personalblog.services.blogservice;

import java.util.List;
import java.util.Optional;

/**
 * JSON REST API for the blog data. Consumed internally by the MVC pages
 * (via WebController + the Thymeleaf templates) and also available for
 * any external/AJAX client.
 */
@RestController
@RequestMapping("/api")
public class Blogcontroller {

    @Autowired
    private blogservice s;

    @GetMapping("/")
    public List<Blog> allBlogs() {
        return s.sendAllBlogs();
    }

    @GetMapping("/blog/genre/{genre}")
    public List<Blog> blogGenre(@PathVariable String genre) {
        return s.sendBlogGenre(genre);
    }

    @GetMapping("/blog/title/{title}")
    public Blog blogTitle(@PathVariable String title) {
        return s.sendBlogTitle(title);
    }

    @GetMapping("/blog/search/{keyword}")
    public List<Blog> search(@PathVariable String keyword) {
        return s.search(keyword);
    }

    @GetMapping("/blog/id/{id}")
    public Optional<Blog> blogId(@PathVariable int id) {
        return s.sendBlogId(id);
    }

    @PostMapping("/blog/add")
    public String addBlog(@RequestBody Blog blog) {
        s.save(blog);
        return "Saved";
    }

    @DeleteMapping("/blog/remove/{id}")
    public String removeBlog(@PathVariable int id) {
        s.remove(id);
        return "Removed";
    }

    @PutMapping("/blog/update/{id}")
    public String updateBlog(@PathVariable int id, @RequestBody Blog blog) {
        s.update(id, blog);
        return "Updated";
    }
}
