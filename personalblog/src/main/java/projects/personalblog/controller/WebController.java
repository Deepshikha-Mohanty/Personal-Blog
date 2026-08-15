package projects.personalblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projects.personalblog.model.Blog;
import projects.personalblog.services.blogservice;

import java.util.List;
import java.util.Optional;

/**
 * Serves the server-rendered (Thymeleaf) pages and wires them to the
 * backend through Spring MVC's Model, as opposed to the JSON API in
 * Blogcontroller which is meant for programmatic/AJAX clients.
 */
@Controller
public class WebController {

    @Autowired
    private blogservice s;

    // Root -> send logged-in users to the home page
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    // Login page (GET only - the POST to /login is handled by Spring Security itself)
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Home page: lists all blogs, or filtered results when a search keyword is supplied
    @GetMapping("/home")
    public String home(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Blog> blogs;
        if (keyword != null && !keyword.trim().isEmpty()) {
            blogs = s.search(keyword.trim());
        } else {
            blogs = s.sendAllBlogs();
        }
        model.addAttribute("blogs", blogs);
        model.addAttribute("keyword", keyword);
        return "home";
    }

    // Single blog detail page
    @GetMapping("/blog/{id}")
    public String viewBlog(@PathVariable int id, Model model) {
        Optional<Blog> found = s.sendBlogId(id);
        if (found.isEmpty()) {
            return "redirect:/home";
        }
        model.addAttribute("blog", found.get());
        return "blog";
    }

    // Show "add blog" form
    @GetMapping("/add-blog")
    public String addBlogForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "add_blog";
    }

    // Handle "add blog" form submission
    @PostMapping("/blogs")
    public String addBlog(@ModelAttribute Blog blog) {
        s.save(blog);
        return "redirect:/home";
    }

    // Show "edit blog" form, pre-filled with the existing blog
    @GetMapping("/edit-blog/{id}")
    public String editBlogForm(@PathVariable int id, Model model) {
        Optional<Blog> found = s.sendBlogId(id);
        if (found.isEmpty()) {
            return "redirect:/home";
        }
        model.addAttribute("blog", found.get());
        return "edit_blog";
    }

    // Handle "edit blog" form submission
    @PostMapping("/update-blog")
    public String updateBlog(@ModelAttribute Blog blog) {
        s.update(blog.getBlog_id(), blog);
        return "redirect:/home";
    }

    // Handle delete
    @PostMapping("/delete-blog/{id}")
    public String deleteBlog(@PathVariable int id) {
        s.remove(id);
        return "redirect:/home";
    }
}
