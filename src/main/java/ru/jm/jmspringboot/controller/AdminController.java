package ru.jm.jmspringboot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.jm.jmspringboot.model.DTOUser;
import ru.jm.jmspringboot.service.UserService;
import java.util.List;

@RestController
@RequestMapping(path = "/admin", produces = "application/json;charset=UTF-8")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping({"/index", "/"})
    public List<DTOUser> admin(ModelMap model) {
        List<DTOUser> list = userService.getAllUsers();
        //   System.out.println("get users:"+list.size());
        return list;
    }

    @GetMapping("/userinfo")
    public DTOUser userinfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DTOUser user = new DTOUser(userService.getUser(auth.getName()));
        return user;
    }


    @GetMapping("/getUser/{id}")
    @ResponseBody
    public DTOUser adminEditUser(@PathVariable("id") int id) {
        DTOUser user = userService.getDTOUser(id);
        return user;
    }


    @PostMapping(value = "/update")
    public DTOUser updateUser(@RequestBody DTOUser user) {
        userService.updateUser(user.getId(), user);
        return user;
    }

    @PostMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.removeUserById(id);
        return "";
    }

    @PostMapping("/create")
    public DTOUser adminCreateUSer(@RequestBody DTOUser user) {
        //  System.out.println("прилетело "+ user.toString());
        userService.saveUser(user);
        return user;
    }

}
