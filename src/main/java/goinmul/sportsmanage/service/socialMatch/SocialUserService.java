package goinmul.sportsmanage.service.socialMatch;


import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.repository.socialMatch.SocialUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialUserService {

    private final SocialUserRepository socialUserRepository;

    @Transactional
    public void saveSocialUser(SocialUser socialUser) {
        socialUserRepository.save(socialUser);
    }

    public List<SocialUser> findSocialUserBySocialMatchId(Long socialMatchId) {
        return socialUserRepository.findAllBySocialMatchId(socialMatchId);
    }


    public SocialUser finSocialUserByIdAndUserId(Long socialMatchId, Long userId) {
        return socialUserRepository.finOneBySocialMatchIdAndUserId(socialMatchId, userId);
    }


}
