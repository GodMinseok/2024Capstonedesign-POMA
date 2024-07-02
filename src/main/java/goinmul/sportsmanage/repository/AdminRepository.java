package goinmul.sportsmanage.repository;


import goinmul.sportsmanage.domain.Authority;
import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.teamMatch.Team;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminRepository {

    private final EntityManager em;

    //영속성 컨텍스면 변경감지로 처리하고, 영속성 아니면 업데이트 쿼리(조회 안하고 업데이트만 하고싶은 경우가 있음)
    @Transactional
    public boolean changeAuthority(User user, Authority authority) {
        boolean result = true;
        try {
            if(em.contains(user))
                user.changeAuthority(authority);
            else {
                em.createQuery("update User u set u.authority = :authority where u.id = :userId")
                        .setParameter("authority", authority)
                        .setParameter("userId", user.getId())
                        .executeUpdate();
            }
        } catch (Exception e){
            result = false;
        }
        return result;
    }

}
