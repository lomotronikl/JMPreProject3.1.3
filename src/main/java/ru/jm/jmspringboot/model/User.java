package ru.jm.jmspringboot.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@NamedNativeQuery(name = "get_all_users", query = "select * from users",resultClass=User.class)

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails, Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid", nullable = false)
    private Long id;

    @Column (name = "name")
    private String name;

    @Column (name = "lastName")
    private String lastName;

    @Column (name = "eMail")
    private String eMail;

    @Column (name = "username")
    private String username;

    @Column (name = "password")
    private String password;

    @Transient
    private String securePassword;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "userid"), inverseJoinColumns = @JoinColumn(name = "roleid"))
    private Set<Role> roles;

    @Transient
    private boolean roleUser;
    @Transient
    private boolean roleAdmin;

    @Transient
    private String[] userRoles; //Используется для получения ролей из формы



    public void fromDTOUser(DTOUser user) {
        this.lastName = user.getLastName();
        this.name = user.getName();
        this.password = user.getPassword();
        this.username = user.getUsername();
        this.roleUser = user.getIsUser();
        this.roleAdmin = user.getIsAdmin();
    }

    @Override
    public String toString(){
        /*
        StringBuilder stringBuilder = new StringBuilder("id=");
        stringBuilder.append(id).append(", ");
        stringBuilder.append(" name=").append(name).append(", ");
        stringBuilder.append(" last name=").append(lastName).append(", ");
        stringBuilder.append(" username=").append(username).append(", ");
        stringBuilder.append(" adm=").append(roleAdmin).append(", ");
        stringBuilder.append(" usr=").append(roleUser).append(", ");
          if (userRoles != null) {

            for (String s : userRoles) {
                stringBuilder.append("userRole=").append(s);

            }
        }
        */

        StringBuilder stringBuilder = new StringBuilder().append(name).append(" ");
        stringBuilder.append(lastName).append(" with roles:");
        if (roleAdmin) {
            stringBuilder.append(" ADMIN");
        }
        if (roleUser) {
            stringBuilder.append(" USER");
        }
        return stringBuilder.toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void emptyPassword(){
        securePassword = password;
        password = "";
    }
    public void restorePassword(){
        password = securePassword;
    }
}
