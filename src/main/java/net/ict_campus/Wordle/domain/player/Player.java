package net.ict_campus.Wordle.domain.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "player")
@Getter
@Setter
public class Player implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_player;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size.List({
            @Size(min = 6, message = "password must be atleast 6 characters long."),
            @Size(max = 255, message = "password must be at max 255 characters long.")
    })
    private String password;

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    //method chaining -> AuthenticationService
    public Player setUsername(String username) {
        this.username = username;
        return this;
    }

    //method chaining -> AuthenticationService
    public Player setPassword(String password) {
        this.password = password;
        return this;
    }

    private int score;

}
