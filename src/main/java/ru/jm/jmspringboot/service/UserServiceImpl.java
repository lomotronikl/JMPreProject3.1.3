package ru.jm.jmspringboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jm.jmspringboot.dao.RoleDao;
import ru.jm.jmspringboot.dao.UserDao;
import ru.jm.jmspringboot.model.DTOUser;
import ru.jm.jmspringboot.model.Role;
import ru.jm.jmspringboot.model.User;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@Service()
@Transactional
public class UserServiceImpl implements UserService{

    UserDao dao;
    RoleDao roleDao;

    @Autowired
    public void setDao(UserDao dao) {
        this.dao = dao;
        if (dao == null) {
            System.out.println("DAO NULL!!");
        }

    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {

        this.roleDao = roleDao;
    }

    public Role getAdminRole() {
        return roleDao.getRoleAdmin();
    }

    public void createRoles() {
        //   Создаем первоначальные роли
        roleDao.createRoles();
    }

    @Override
    public void saveUser(DTOUser dtoUser){
        User user = new User();
        user.fromDTOUser(dtoUser);
        //resolveStrRoles(user);
        updateUserRoles(user, user.isRoleAdmin(), user.isRoleUser());
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
       // System.out.println("save user:"+ user.toString());
        dao.saveUser(user);
    }

    @Override
    public void updateUserRoles(User user, boolean isAdmin, boolean isUser) {
        user.setRoleAdmin(isAdmin);
        user.setRoleUser(isUser);
        Set<Role> roles = new HashSet<>();
        if (isAdmin) {
            roles.add(roleDao.getRoleAdmin());
        }
        if (isUser){
            roles.add(roleDao.getRoleUser());
        }
        user.setRoles(roles);
    }

    @Override
    public void removeUserById(long id)  {
        dao.removeUserById(id);
    }

    private User userSetRolesFlag(User user) {
        if (user!=null) {
            for (Role role : user.getRoles()) {
                if (role.getRoleName().indexOf("ADMIN") >= 0) {
                    user.setRoleAdmin(true);
                } else {
                    if (role.getRoleName().indexOf("USER") >= 0) {
                        user.setRoleUser(true);
                    }
                }
            }
        }
        return user;
    }
    @Override
    public List<DTOUser> getAllUsers() {
        List<User> users = dao.getAllUsers();
        List<DTOUser> dtoUsers = new LinkedList<>();
        for ( User user: users ) {
             userSetRolesFlag(user);
             dtoUsers.add(new DTOUser(user));
        }
        return dtoUsers;
    }

    @Override
    public User getUser(long id) {

        return  userSetRolesFlag(dao.getUser(id));
    }
    public DTOUser getDTOUser(long id) {
        return  new DTOUser(getUser(id));
    }
    public User getUser(String userName) {
        return  userSetRolesFlag(dao.findByUsername(userName));
    }

    @Override
    @Transactional
    public void updateUser(User updateUser) {
         dao.updateUser(updateUser);
    }

    private User updateUserCommon(long id, DTOUser user) {
        User updateuser= getUser(id);
        updateuser.setUsername(user.getUsername());
        updateuser.setName(user.getName());
        updateuser.setLastName(user.getLastName());
        if (user.getPassword().length()>0) {
            updateuser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        return updateuser;
    }

   // public void updateUserShort(long id, User user) {
   //     updateUser(updateUserCommon(id, user));
   // }
    private void resolveStrRoles(User user) {
        String[] sRoles =  user.getUserRoles();
        user.setRoleAdmin(false);
        user.setRoleUser(false);
        if (sRoles != null){
            for ( String role: sRoles ) {
                if (role.indexOf("ADMIN") >= 0) {
                    user.setRoleAdmin(true);
                } else {
                    if (role.indexOf("USER") >= 0) {
                        user.setRoleUser(true);
                    }
                }
            }
        }
    }
    public void updateUser(long id, DTOUser user) {
        User updateuser= updateUserCommon(id,user);
        updateUserRoles(updateuser, user.getIsAdmin(), user.getIsUser());
        updateUser(updateuser);
    }

    public void setRoles(Set<Role> roles, User user) {
        user.setRoles(roles);
   }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = dao.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println(user.getUsername());
        org.springframework.security.core.userdetails.User userD= new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(),user.getAuthorities());
        return userD;
    }

    @Override
    public String toString(){
        return "USImpl";
    }

}
