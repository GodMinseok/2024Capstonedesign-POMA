package goinmul.sportsmanage.service.socialMatch;


import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.dto.SocialMatchDto;
import goinmul.sportsmanage.domain.dto.TeamMatchDto;
import goinmul.sportsmanage.domain.socialMatch.SocialMatch;
import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.repository.socialMatch.SocialMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialMatchService {

    private final SocialMatchRepository socialMatchRepository;

    @Transactional
    public void saveSocialMatch(SocialMatch socialMatch) {
        socialMatchRepository.save(socialMatch);
    }


    public SocialMatch findSocialMatchWithSocialUserBySocialMatchId(Long socialMatchId){
        return socialMatchRepository.findWithSocialUserBySocialMatchId(socialMatchId);
    }


}
