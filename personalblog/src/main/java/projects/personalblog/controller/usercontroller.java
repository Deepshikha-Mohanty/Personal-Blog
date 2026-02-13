package projects.personalblog.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projects.personalblog.model.User;
import projects.personalblog.services.userservice;

@RestController
@RequestMapping("/api")
public class usercontroller {

    @Autowired
    private userservice s;

    @PostMapping("/user/save")
    public String addUser(@RequestBody User user)
    {
        s.save(user);
        return "Saved";
    }

    @PutMapping("/user/update/{id}")
    public String updateUser(@PathVariable int id,@RequestBody User user)
    {
        s.update(id,user);
        return "Updated";
    }

    @DeleteMapping("/user/remove/{id}")
    public String removeUser(@PathVariable int id)
    {
        s.remove(id);
        return "deleted";
    }
}
