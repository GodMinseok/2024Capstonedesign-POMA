package goinmul.sportsmanage.domain;


import goinmul.sportsmanage.domain.teamMatch.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @DynamicInsert
@Getter @Setter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String loginId;
    private String password;
    private String name;
    private String phone;
    private LocalDateTime regdate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private String email;

    @ColumnDefault("USER")
    @Enumerated(EnumType.STRING)
    private Authority authority;

    private Integer money;


    public static User createUser(Long id){
        User user = new User();
        user.setId(id);
        return user;
    }

    public static List<User> createUsers(List<Long> userIdList){
        List<User> users = new ArrayList<>();
        for (Long userId : userIdList) {
            User user = User.createUser(userId);
            users.add(user);
        }
        return users;
    }

    public static User createUser(String loginId, String password, String name, String phone, Gender gender){
        User user = new User();
        user.setLoginId(loginId);
        user.setPassword(password);
        user.setName(name);
        user.setPhone(phone);
        user.setRegdate(LocalDateTime.now());
        user.setGender(gender);
        return user;
    }

    public void changeTeam(Team team) {
        this.setTeam(team);
    }

    public void changeAuthority(Authority authority) {
        this.setAuthority(authority);
    }

    public void plusMoney(Integer money) {
        Integer totalMoney = this.money + money;
        this.setMoney(totalMoney);
    }

    public void minusMoney(Integer money) {
        Integer totalMoney = this.money - money;
        this.setMoney(totalMoney);
    }

}
