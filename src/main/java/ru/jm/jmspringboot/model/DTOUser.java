package ru.jm.jmspringboot.model;

public class DTOUser {
    private Long id;
    private String name;
    private String lastName;
    private String eMail;
    private String username;
    private String password;
    private boolean isAdmin;
    private boolean isUser;

    public DTOUser(){}
    public DTOUser(User user){
        id = user.getId();
        name = user.getName();
        lastName = user.getLastName();
        eMail = user.getEMail();
        username = user.getUsername();
        password = "";
        isAdmin = user.isRoleAdmin();
        isUser = user.isRoleUser();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean getIsUser() {
        return isUser;
    }

    public void setIsUser(boolean user) {
        isUser = user;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(name).append(" ");
        stringBuilder.append(lastName).append(" ");
        stringBuilder.append(username).append(" ");
        stringBuilder.append(password).append(" ");
        stringBuilder.append(id).append(" with roles:");
        if (isAdmin) {
            stringBuilder.append(" ADMIN");
        }
        if (isUser) {
            stringBuilder.append(" USER");
        }
        return stringBuilder.toString();
    }
}
