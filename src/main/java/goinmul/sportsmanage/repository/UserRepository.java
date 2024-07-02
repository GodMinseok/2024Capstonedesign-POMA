package goinmul.sportsmanage.repository;


import goinmul.sportsmanage.domain.Authority;
import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.teamMatch.Team;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    //영속성 컨텍스면 변경감지로 처리하고, 영속성 아니면 업데이트 쿼리(조회 안하고 업데이트만 하고싶은 경우가 있음)
    public void updateTeam(User user, Team team) {
        if (em.contains(user))
            user.changeTeam(team);
        else {
            em.createQuery("update User u set u.team.id = :teamId where u.id = :userId")
                    .setParameter("teamId", team.getId())
                    .setParameter("userId", user.getId())
                    .executeUpdate();
        }
    }

    //영속성 컨텍스트에 존재하는 상황이 없음
    public void plusMoney(User user, Integer money) {
        em.createQuery("update User u set u.money = u.money+:money where u.id = :userId")
                .setParameter("money", money)
                .setParameter("userId", user.getId())
                .executeUpdate();
    }

    public void minusMoney(User user, Integer money) {
        em.createQuery("update User u set u.money = u.money-:money where u.id = :userId")
                .setParameter("money", money)
                .setParameter("userId", user.getId())
                .executeUpdate();
    }

    public void save(User user) {
        em.persist(user);
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    public void updatePassword(String email, String password) {
        em.createQuery("update User u set u.password = :password where u.email = :email")
                .setParameter("password", password)
                .setParameter("email", email)
                .executeUpdate();

    }

    public User validateEmail(String email) {
        HashMap<String, String> map = new HashMap<>();
        List<User> user = em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();

        return user.isEmpty() ? null : user.get(0);

    }


    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    public List<User> findExcludeSuperAdmin() {
        return em.createQuery("select u from User u where u.authority != : authority", User.class)
                .setParameter("authority", Authority.SUPER_ADMIN)
                .getResultList();
    }

    public List<User> findWithTeamByUserIdIn(List<Long> userIdList) {
        return em.createQuery("select u from User u join fetch u.team where u.id IN :userIdList", User.class)
                .setParameter("userIdList", userIdList)
                .getResultList();
    }

    public List<User> findByTeamId(Long teamId) {
        return em.createQuery("select u from User u where u.team.id = :teamId", User.class)
                .setParameter("teamId", teamId)
                .getResultList();
    }

    public List<User> findByTeamIdAndUserIdNotIn(Long teamId, List<Long> userIdList) {
        return em.createQuery("select u from User u where u.team.id = :teamId and u.id not in :userIdList", User.class)
                .setParameter("teamId", teamId)
                .setParameter("userIdList", userIdList)
                .getResultList();
    }

    public List<User> findByTeamIdAndGender(Long teamId, Gender gender) {
        return em.createQuery("select u from User u join fetch u.team where u.team.id = :teamId AND u.gender =:gender", User.class)
                .setParameter("teamId", teamId)
                .setParameter("gender", gender)
                .getResultList();
    }

    public List<User> findByTeamIdAndGenderUserIdNotIn(Long teamId, Gender gender, List<Long> userIdList) {
        return em.createQuery("select u from User u join fetch u.team where u.team.id = :teamId AND u.gender =:gender and u.id not in :userIdList", User.class)
                .setParameter("teamId", teamId)
                .setParameter("gender", gender)
                .setParameter("userIdList", userIdList)
                .getResultList();
    }

    //외부 조인으로 해야함
    public User findUserWithTeamByLoginId(String loginId) {
        List<User> users = em.createQuery("select u from User u left join fetch u.team where u.loginId = :loginId", User.class)
                .setParameter("loginId", loginId)
                .getResultList();

        User user = null;
        if (!users.isEmpty())
            user = users.get(0);

        return user;
    }

    //외부 조인으로 해야함
    public User findUserWithTeamByLoginIdAndName(String loginId, String name) {
        List<User> users = em.createQuery("select u from User u left join fetch u.team where u.loginId = :loginId or u.name = :name", User.class)
                .setParameter("loginId", loginId)
                .setParameter("name", name)
                .getResultList();

        User user = null;
        if (!users.isEmpty())
            user = users.get(0);

        return user;
    }

}