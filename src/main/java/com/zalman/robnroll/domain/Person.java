package com.zalman.robnroll.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Person implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER) //, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "brigade_id", nullable = true)
    private Brigade brigade;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "person_role", joinColumns = @JoinColumn(name = "person_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    @Email(message = "Введите корректный почтовый адрес")

    @Length(min = 1, max = 50, message = "Максимальное количество символов 50")
    @NotBlank(message = "Данное поле не должно быть пустым")
    private String email;

    @Length(min = 1, max = 50, message = "Максимальное количество символов 50")
    @NotBlank(message = "Данное поле не должно быть пустым")
    private String username;

    @Length(min = 1, max = 20, message = "Максимальное количество символов 20   ")
    @NotBlank(message = "Данное поле не должно быть пустым")
    private String first_name;

    @Length(min = 1, max = 20, message = "Максимальное количество символов 20")
    @NotBlank(message = "Данное поле не должно быть пустым")
    private String last_name;

    //@NotBlank(message = "Данное поле не должно быть пустым")
    //@Min(value = 8, message = "Пароль должен содержать не менее 8 символов")
    private String password;

    @Transient
    private String password_1;
    @Transient
    private String password_2;

    private Boolean active;
    private String profile_pic;
    private String activationCode;

    public String getPassword_1() {
        return password_1;
    }

    public void setPassword_1(String password_2) {
        this.password_1 = password_2;
    }

    public String getPassword_2() {
        return password_2;
    }

    public void setPassword_2(String password_2) {
        this.password_2 = password_2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_pic() {
        if(profile_pic == null) {
            return "";
        }
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public Brigade getBrigade() {
        return brigade;
    }

    public void setBrigade(Brigade brigade) {
        this.brigade = brigade;
    }

    public String getBrigadeName() {
        return brigade != null ? brigade.getName() : "Нет";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getInitials() {
        if(last_name.isEmpty() || first_name.isEmpty()) {
            return "";
        }
        return String.valueOf(Character.toString(last_name.charAt(0)) + Character.toString(first_name.charAt(0))).toUpperCase();
    }

    public String getFull_name() {
        return last_name + " " + first_name;
    }

    public boolean haveRoles() {
        for (Role role : Role.values()) {
            if (this.roles.contains(role)) {
                return true;
            }
        }
        return false;
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
        return getActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }



    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", brigade=" + brigade +
                ", roles=" + roles +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", password='" + password + '\'' +
                ", password_1='" + password_1 + '\'' +
                ", password_2='" + password_2 + '\'' +
                ", active=" + active +
                ", profile_pic='" + profile_pic + '\'' +
                ", activationCode='" + activationCode + '\'' +
                '}';
    }
}
