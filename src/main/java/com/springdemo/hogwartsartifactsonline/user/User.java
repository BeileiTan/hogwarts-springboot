package com.springdemo.hogwartsartifactsonline.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.io.Serializable;

@Entity
@Table(name = "HogwartsUser")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean enabled;

    @NotBlank(message = "roles is required")
    private String roles;

    @NotBlank(message = "name is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;


    public User() {
    }

    public User(Integer id, boolean enabled, String roles, String username, String password) {
        this.id = id;
        this.enabled = enabled;
        this.roles = roles;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
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

}
