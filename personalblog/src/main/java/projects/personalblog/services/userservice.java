package projects.personalblog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.personalblog.model.User;
import projects.personalblog.repositories.userrepository;

import java.util.Optional;
@Service
public class userservice {

    @Autowired
    private userrepository r;
    public void save(User user) {
        r.save(user);
    }

    public void update(int id,User user) {
        /*Optional<User> newuser= r.findById(id);
        newuser.setUser_name(user.getUser_name());
        newuser.setPassword(user.getPassword());
        r.save(newuser);*/
    }

    public void remove(int id) {
        r.deleteById(id);
    }
}
