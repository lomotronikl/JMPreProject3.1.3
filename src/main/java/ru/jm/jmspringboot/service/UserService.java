package ru.jm.jmspringboot.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.jm.jmspringboot.model.DTOUser;
import ru.jm.jmspringboot.model.Role;
import ru.jm.jmspringboot.model.User;
import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    void saveUser(DTOUser user);
    void removeUserById(long id);
    List<DTOUser> getAllUsers();
    User getUser(long id);
    DTOUser getDTOUser(long id);
    User getUser(String userName);
    void updateUser(User updateUser);
    void updateUser(long id, DTOUser user);
  //  void updateUserShort(long id, User user);
    void setRoles(Set<Role> roles, User user);
    void updateUserRoles(User user, boolean isAdmin, boolean isUser);
    Role getAdminRole();
    void createRoles(); // нужно для создания ролей в бд
}
