package projects.personalblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projects.personalblog.model.Blog;
import projects.personalblog.services.blogservice;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class Blogcontroller {

    @Autowired
    private blogservice s;

    @GetMapping("/")
    public ArrayList<Blog> Home()
    {
        ArrayList<Blog> all= s.sendAllBlogs();
        return all;
    }

    @GetMapping("/blog/{genre}")
    public ArrayList<Blog> blogGenre(@PathVariable String genre)
    {
        ArrayList<Blog> some= s.sendBlogGenre(genre);
        return some;
    }

    @GetMapping("/blog/{title}")
    public Blog blogtitle(@PathVariable String title)
    {
        Blog blogt=s.sendBlogTitle(title);
        return blogt;
    }

    @GetMapping("/blog/{id}")
    public Optional<Blog> Blogid(@PathVariable int id)
    {
        Optional<Blog> blogid=s.sendBlogId(id);
        return blogid;
    }

    @PostMapping("/blog/add")
    public String addblog(@RequestBody Blog blog)
    {
        s.save(blog);
        return "Saved";
    }

    @DeleteMapping("/blog/remove/{id}")
    public String removeblog(@PathVariable int id)
    {
        s.remove(id);
        return "Removed";
    }

    @PutMapping("/blog/update/{id}")
    public String updateblog(@PathVariable int id, @RequestBody Blog blog)
    {
        s.update(id,blog);
        return "Updated";
    }

}
