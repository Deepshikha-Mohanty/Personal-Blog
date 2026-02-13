package projects.personalblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projects.personalblog.model.User;
@Repository
public interface userrepository extends JpaRepository<User,Integer> {
}
