//package com.example.demo.appuser;
//
//import jakarta.persistence.*;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Collections;
//
//@Entity
//public class Manager implements UserDetails {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String username;
//    private String password;
//    @Enumerated(EnumType.STRING)
//    private AppUserRole appUserRole;
//    private Boolean locked;
//    private Boolean enabled;
//
//    public Manager() {
//    }
//
//    public Manager(String username, String password, AppUserRole appUserRole, Boolean locked, Boolean enabled) {
//        this.username = username;
//        this.password = password;
//        this.appUserRole = appUserRole;
//        this.locked = locked;
//        this.enabled = enabled;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
//        return Collections.singletonList(authority);
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public AppUserRole getAppUserRole() {
//        return appUserRole;
//    }
//
//    public void setAppUserRole(AppUserRole appUserRole) {
//        this.appUserRole = appUserRole;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return !locked;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    public void setLocked(Boolean locked) {
//        this.locked = locked;
//    }
//
//    public void setEnabled(Boolean enabled) {
//        this.enabled = enabled;
//    }
//}
