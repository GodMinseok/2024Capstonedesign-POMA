package goinmul.sportsmanage.controller;


import goinmul.sportsmanage.domain.*;
import goinmul.sportsmanage.domain.form.ReservationForm;
import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatch;
import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchTeamMercenaryMaxSize;
import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchUser;
import goinmul.sportsmanage.domain.mercenaryMatch.UserStatus;
import goinmul.sportsmanage.domain.socialMatch.SocialMatch;
import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import goinmul.sportsmanage.repository.GroundRepository;
import goinmul.sportsmanage.repository.ReservationRepository;
import goinmul.sportsmanage.service.ReservationService;
import goinmul.sportsmanage.service.SportsService;
import goinmul.sportsmanage.service.UserService;
import goinmul.sportsmanage.service.mercenaryMatch.MercenaryMatchService;
import goinmul.sportsmanage.service.socialMatch.SocialMatchService;
import goinmul.sportsmanage.service.teamMatch.TeamMatchService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final SportsService sportsService;
    private final UserService userService;
    private final SocialMatchService socialMatchService;
    private final TeamMatchService teamMatchService;
    private final MercenaryMatchService mercenaryMatchService;
    private final GroundRepository groundRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    @GetMapping("/reservation/{sports}")
    public String reservationForm(@PathVariable String sports, Model model, HttpServletRequest request) {
        SportsInfo sportsInfo = sportsService.getSportsInfo("futsal");
        List<Ground> grounds = groundRepository.findAll();
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            return "pleaseLogin";
        }
        List<User> teamMembers = null;
        if (sessionUser.getTeam() != null)
            teamMembers = userService.findUsersByTeamId(sessionUser.getTeam().getId());

        model.addAttribute("teamMembers", teamMembers);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("sportsInfo", sportsInfo);
        model.addAttribute("grounds", grounds);
        model.addAttribute("MALE", Gender.MALE);
        model.addAttribute("FEMALE", Gender.FEMALE);
        model.addAttribute("currentUrl", request.getRequestURL());
        return "groundReservationForm";

    }

    @PostMapping("/reservation/{sports}")
    public ResponseEntity<String> reservation(@Valid @RequestBody ReservationForm reservationForm, @PathVariable String sports,
                                              HttpServletRequest request, Model model, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>("필요한 데이터가 입력되지 않았습니다", HttpStatus.CONFLICT);
        }

        Ground ground = groundRepository.findOne(reservationForm.getGroundId());
        SportsInfo sportsInfo = sportsService.getSportsInfo("futsal");
        model.addAttribute("sportsInfo", sportsInfo);
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            return new ResponseEntity<>("로그인 해주세요!", HttpStatus.CONFLICT);
        }

        if (ground.getPrice() != 0) {
            boolean result = userService.useMoney(sessionUser, ground.getPrice());
            if (result) sessionUser.minusMoney(ground.getPrice());
            else {
                return new ResponseEntity<>("구입할 돈이 부족합니다", HttpStatus.CONFLICT);
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 문자열 형식 지정
        LocalDate parseDate = LocalDate.parse(reservationForm.getDate(), formatter);
        Gender genderEnum = sportsService.createGenderEnum(reservationForm.getGender());
        Sports sportsEnum = sportsService.createSportsEnum(sports);
        Match matchEnum = sportsService.createMatchEnum(reservationForm.getMatch());
        SocialUser socialUser = SocialUser.createSocialUser(sessionUser);
        List<User> users = userService.findUserWithTeamByUserIdIn(reservationForm.getUserIdList());

        //성별 체크하는 코드
        for (User user : users) {
            if (!genderEnum.equals(Gender.BOTH) && user.getGender() != genderEnum) {
                return new ResponseEntity<>("모집 성별과 안 맞는 멤버를 선택했어요", HttpStatus.CONFLICT);
            }
        }

        int maxSize = reservationForm.getMaxSize();
        List<Long> userIdList = reservationForm.getUserIdList();
        Integer mercenarySize = reservationForm.getMercenarySize();


        Reservation reservation = Reservation.createGroundReservation(ground, sessionUser, parseDate, reservationForm.getTime());
        boolean result;
        switch (matchEnum) {
            case SOCIAL:
                result = reservationService.saveReservation(reservation);
                if (!result) {
                    return new ResponseEntity<>("해당 시간은 이미 예약됐습니다", HttpStatus.CONFLICT);
                }
                SocialMatch socialMatch = SocialMatch.createSocialMatch(reservationForm.getMaxSize(), genderEnum, sportsEnum, socialUser, reservation);
                socialMatchService.saveSocialMatch(socialMatch);
                return new ResponseEntity<>("소셜 매치 생성 완료!", HttpStatus.CREATED);
            case TEAM:
                if (sessionUser.getTeam() == null) {
                    return new ResponseEntity<>("팀에 가입해 주세요", HttpStatus.CONFLICT);
                }
                if (userIdList == null || maxSize/2 != userIdList.size()) {
                    String message = maxSize/2 + "명을 뽑아주세요";
                    return new ResponseEntity<>(message, HttpStatus.CONFLICT);
                }
                List<TeamUser> teamUsers = new ArrayList<>();
                for (User user : users) {
                    TeamUser teamUser = TeamUser.createTeamUser(user, user.getTeam());
                    teamUsers.add(teamUser);
                }
                result = reservationService.saveReservation(reservation);
                if (!result) {
                    return new ResponseEntity<>("해당 시간은 이미 예약됐습니다", HttpStatus.CONFLICT);
                }
                TeamMatch teamMatch = TeamMatch.createTeamMatch(maxSize, genderEnum, sportsEnum, reservation, teamUsers);
                teamMatchService.saveTeamMatch(teamMatch);
                return new ResponseEntity<>("팀 매치 생성 완료!", HttpStatus.CREATED);
            case MERCENARY:
                if (sessionUser.getTeam() == null) {
                    return new ResponseEntity<>("팀에 가입해 주세요", HttpStatus.CONFLICT);
                }
                if (userIdList == null) {
                    return new ResponseEntity<>("팀원은 최소 한명은 있어야 해요", HttpStatus.CONFLICT);
                }
                if (mercenarySize == null) {
                    return new ResponseEntity<>("모집할 용병 수를 정하세요", HttpStatus.CONFLICT);
                }
                if (mercenarySize < 1) {
                    return new ResponseEntity<>("용병은 최소 한명은 있어야 해요", HttpStatus.CONFLICT);
                }
                if (mercenarySize + userIdList.size() != maxSize/2) {
                    String message = "용병과 팀원을 포함해 " + maxSize/2 + "명을 뽑아주세요";
                    return new ResponseEntity<>(message, HttpStatus.CONFLICT);
                }
                result = reservationService.saveReservation(reservation);
                if (!result) {
                    return new ResponseEntity<>("해당 시간은 이미 예약됐습니다", HttpStatus.CONFLICT);
                }
                List<MercenaryMatchUser> mercenaryUsers = new ArrayList<>();
                for (User user : users) {
                    MercenaryMatchUser mercenaryUser = MercenaryMatchUser.createMercenaryUser(user, user.getTeam(), UserStatus.TEAM);
                    mercenaryUsers.add(mercenaryUser);
                }
                MercenaryMatchTeamMercenaryMaxSize mercenaryTeam = MercenaryMatchTeamMercenaryMaxSize.createMercenaryTeam(sessionUser.getTeam(), mercenarySize);
                MercenaryMatch mercenaryMatch = MercenaryMatch.createMercenaryMatch(maxSize, genderEnum, sportsEnum, reservation, mercenaryUsers, mercenaryTeam);
                mercenaryMatchService.saveMercenaryMatch(mercenaryMatch);
                return new ResponseEntity<>("용병 매치 생성 완료!", HttpStatus.CREATED);
            default:
                return new ResponseEntity<>("에러가 발생했습니다", HttpStatus.CONFLICT);
        }
    }
}
