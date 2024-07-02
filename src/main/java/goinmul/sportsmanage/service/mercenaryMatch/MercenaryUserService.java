package goinmul.sportsmanage.service.mercenaryMatch;

import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchUser;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MercenaryUserService {

    private final MercenaryMatchUserRepository mercenaryUserRepository;
    @Transactional
    public void saveMercenaryUser(MercenaryMatchUser mercenaryUser) {
        mercenaryUserRepository.save(mercenaryUser);
    }
    @Transactional
    public void saveMercenaryUsers(List<MercenaryMatchUser> mercenaryUsers) {
        for (MercenaryMatchUser mercenaryUser : mercenaryUsers) {
            mercenaryUserRepository.save(mercenaryUser);
        }
    }
    public MercenaryMatchUser findMercenaryUser(Long id) {
        return mercenaryUserRepository.findOne(id);
    }
    public List<MercenaryMatchUser> findMercenaryUsers() {
        return mercenaryUserRepository.findAll();
    }
    public List<MercenaryMatchUser> findMercenaryUsersByMercenaryMatchId(Long id) {
        return mercenaryUserRepository.findByMercenaryMatchId(id);
    }
    public MercenaryMatchUser finMercenaryUserByMercenaryMatchIdAndUserId(Long mercenaryMatchId, Long userId) {
        return mercenaryUserRepository.finOneByMercenaryMatchIdAndUserId(mercenaryMatchId, userId);
    }

    public MercenaryMatchUser findMercenaryUserByUserId(List<MercenaryMatchUser> mercenaryMatchUsers, Long userId) {
        ArrayList<Long> userIdList = new ArrayList<>();

        for (MercenaryMatchUser mercenaryMatchUser : mercenaryMatchUsers) {
            if(mercenaryMatchUser.getUser().getId().equals(userId))
                return mercenaryMatchUser;
        }

        return null;
    }

}
