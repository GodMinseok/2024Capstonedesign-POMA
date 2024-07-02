package goinmul.sportsmanage.service;

import goinmul.sportsmanage.domain.CustomerSupport;
import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.dto.JoinedMatchesDto;
import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchUser;
import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.domain.teamMatch.Team;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import goinmul.sportsmanage.repository.CustomerSupportRepository;
import goinmul.sportsmanage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerSupportService {

    private final UserRepository userRepository;
    private final CustomerSupportRepository customerSupportRepository;
    @Transactional
    public void saveCustomerPost(CustomerSupport customerSupport) {
        customerSupportRepository.save(customerSupport);
    }

}
