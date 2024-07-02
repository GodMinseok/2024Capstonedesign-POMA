package goinmul.sportsmanage.service;

import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.dto.JoinedMatchesDto;
import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchUser;
import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.domain.teamMatch.Team;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import goinmul.sportsmanage.repository.UserRepository;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchUserRepository;
import goinmul.sportsmanage.repository.socialMatch.SocialUserRepository;
import goinmul.sportsmanage.repository.teamMatch.TeamUserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final JavaMailSender javaMailSender;

    private final UserRepository userRepository;
    private final SocialUserRepository socialUserRepository;
    private final TeamUserRepository teamUserRepository;
    private final MercenaryMatchUserRepository mercenaryMatchUserRepository;
    @Transactional
    public boolean saveUser(User user) {
        user.setRegdate(LocalDateTime.now());
        boolean result = validateDuplicateMember(user.getLoginId(), user.getName());
        if (result) userRepository.save(user);
        return result;
    }

    @Transactional
    public void changeTeam(User user, Team team) {
        userRepository.updateTeam(user, team);
    }

    @Transactional
    public void chargeMoney(User user, Integer money) {
        userRepository.plusMoney(user, money);
    }

    @Transactional
    public boolean useMoney(User user, Integer money) {
        User findUser = userRepository.findOne(user.getId());
        if(findUser.getMoney() < money) return false;
        userRepository.minusMoney(user, money);
        return true;
    }

    @Transactional
    public List<String> changePassword(String email) {
        User user = userRepository.validateEmail(email);
        if(user != null){
            String newPassword = createNewPassword();
            userRepository.updatePassword(email, newPassword);
            List<String> list = new ArrayList<>();
            list.add(user.getLoginId());
            list.add(newPassword);
            return list;
        }

        return null;

    }

    public List<User> findUserWithTeamByUserIdIn(List<Long> userIdList) {
        return userRepository.findWithTeamByUserIdIn(userIdList);
    }


    //팀 매치 멤버 선택 GET
    public List<User> findUsersByTeamIdAndGender(Long teamId, Gender gender) {
        List<User> users;
        if(gender.equals(Gender.BOTH)) users = userRepository.findByTeamId(teamId); //BOTH면 남,여 데이터 다 가져옴
        else users = userRepository.findByTeamIdAndGender(teamId, gender);
        return users;
    }

    //용병 매치 멤버 선택 GET
    public List<User> findUsersByTeamIdAndGenderAndUserIdNotIn(Long teamId, Gender gender, List<Long> userIdList) {
        List<User> users;
        if(gender.equals(Gender.BOTH)) users = userRepository.findByTeamIdAndUserIdNotIn(teamId, userIdList); //BOTH면 남,여 데이터 다 가져옴
        else users = userRepository.findByTeamIdAndGenderUserIdNotIn(teamId, gender, userIdList);
        return users;
    }

    public User findUserWithTeamByLoginId(String loginId) {
        return userRepository.findUserWithTeamByLoginId(loginId);
    }

    public List<User> findUsersByTeamId(Long teamId) {
        return userRepository.findByTeamId(teamId);
    }

    //회원 가입 검증 true 성공, false 실패(이미 사용중인 아이디)
    private boolean validateDuplicateMember(String loginId, String name) {
        User user = userRepository.findUserWithTeamByLoginIdAndName(loginId, name);
        return (user == null);
    }

    //로그인 검증
    public Boolean validateLoginMember(String loginId, String password) {
        User findMember = userRepository.findUserWithTeamByLoginId(loginId);
        boolean result = false;
        if (findMember != null) {
            if (findMember.getPassword().equals(password))
                result = true;
        }
        return result;
    }

    public JoinedMatchesDto findJoinedMatchesDtoByUserId(Long userId) {
        List<SocialUser> socialUsers = socialUserRepository.findWithSocialMatchWithReservationWithGroundByUserId(userId);
        List<TeamUser> teamUsers = teamUserRepository.findWithTeamWithTeamMatchWithReservationWithGroundByUserId(userId);
        List<MercenaryMatchUser> mercenaryMatchUsers = mercenaryMatchUserRepository.findWithTeamWithMercenaryMatchWithReservationWithGroundByUserId(userId);
        return JoinedMatchesDto.createJoinedMatchesDto(socialUsers, teamUsers, mercenaryMatchUsers);
    }

    private String createNewPassword() {
        char[] chars = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'
        };

        StringBuffer stringBuffer = new StringBuffer();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(new Date().getTime());

        int index = 0;
        int length = chars.length;
        for (int i = 0; i < 8; i++) {
            index = secureRandom.nextInt(length);

            if (index % 2 == 0)
                stringBuffer.append(String.valueOf(chars[index]).toUpperCase());
            else
                stringBuffer.append(String.valueOf(chars[index]).toLowerCase());

        }

        return stringBuffer.toString();

    }

    public void sendNewPasswordByMail(String toMailAddr, String loginId, String newPassword) {

        final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(toMailAddr);
                mimeMessageHelper.setSubject("[POMA] 아이디와 새 비밀번호 안내입니다.");
                mimeMessageHelper.setText("아이디 : " +loginId + ", 새 비밀번호 : " + newPassword, true);
            }

        };
        javaMailSender.send(mimeMessagePreparator);

    }


}
