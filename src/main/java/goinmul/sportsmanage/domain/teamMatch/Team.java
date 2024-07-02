package goinmul.sportsmanage.domain.teamMatch;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String name;

    //용병 신청 post에서 사용험
    public static Team createTeam(Long id){
        Team team = new Team();
        team.setId(id);
        return team;
    }

    public static Team createTeam(String name){
        Team team = new Team();
        team.setName(name);
        return team;
    }

    /*
    public static Team createTeam(String name, User user){
        Team team = new Team();
        team.setName(name);
        team.addUser(user);
        return team;
    }

    private void addUser(User user){
        users.add(user);
        user.setTeam(this);
    }
    */


}
