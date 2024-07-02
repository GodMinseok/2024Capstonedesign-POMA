package goinmul.sportsmanage.controller;



import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.SportsInfo;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.dto.TeamMatchDto;
import goinmul.sportsmanage.domain.form.JoinMercenaryForm;
import goinmul.sportsmanage.domain.form.JoinMercenaryMatchForm;
import goinmul.sportsmanage.domain.mercenaryMatch.*;
import goinmul.sportsmanage.domain.socialMatch.SocialMatch;
import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.domain.teamMatch.Team;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchRepository;
import goinmul.sportsmanage.repository.socialMatch.SocialMatchRepository;
import goinmul.sportsmanage.repository.teamMatch.TeamMatchRepository;
import goinmul.sportsmanage.repository.teamMatch.TeamUserRepository;
import goinmul.sportsmanage.service.SportsService;
import goinmul.sportsmanage.service.UserService;
import goinmul.sportsmanage.service.mercenaryMatch.MercenaryMatchService;
import goinmul.sportsmanage.service.mercenaryMatch.MercenaryMatchTeamMercenaryMaxSizeService;
import goinmul.sportsmanage.service.mercenaryMatch.MercenaryUserService;
import goinmul.sportsmanage.service.socialMatch.SocialMatchService;
import goinmul.sportsmanage.service.socialMatch.SocialUserService;
import goinmul.sportsmanage.service.teamMatch.TeamMatchService;
import goinmul.sportsmanage.service.teamMatch.TeamUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MatchController {

    private static final Logger log = LoggerFactory.getLogger(MatchController.class);
    private final SportsService sportsService;
    private final UserService userService;
    private final SocialUserService socialUserService;
    private final SocialMatchService socialMatchService;
    private final SocialMatchRepository socialMatchRepository;
    private final TeamMatchService teamMatchService;
    private final TeamUserService teamUserService;
    private final MercenaryMatchService mercenaryMatchService;
    private final MercenaryUserService mercenaryUserService;
    private final MercenaryMatchTeamMercenaryMaxSizeService mercenaryMatchTeamMercenaryMaxSizeService;
    private final TeamMatchRepository teamMatchRepository;
    private final MercenaryMatchRepository mercenaryMatchRepository;
    private final TeamUserRepository teamUserRepository;

    @GetMapping("/social/{sports}")
    public String socialMatchList(@PathVariable String sports, @RequestParam(required = false) String location,
                                  @RequestParam(required = false) String date, @RequestParam(required = false) String gender, Model model,
                                  HttpServletRequest request) {

        if (location == null) location = "전체";
        if (date == null) date = LocalDate.now().toString();
        if (gender == null) gender = "ALL";

        Gender genderEnum = sportsService.createGenderEnum(gender);
        Sports sportsEnum = sportsService.createSportsEnum(sports);
        SportsInfo sportsInfo = sportsService.getSportsInfo(sports);

        List<SocialMatch> socialMatchWithReservationAndGround = socialMatchRepository.
                findWithReservationAndGroundByDateAndLocationAndGender(LocalDate.parse(date), location, genderEnum, sportsEnum);

        model.addAttribute("sportsInfo", sportsInfo);
        model.addAttribute("location", location);
        model.addAttribute("date", date);
        model.addAttribute("gender", gender);
        model.addAttribute("socialMatchWithReservationAndGround", socialMatchWithReservationAndGround);
        model.addAttribute("sports", sports); //안써도 자동으로 넣어주긴 해요

        return "match/socialMatch";
    }

    @PostMapping("/social/{sports}")
    @ResponseBody
    //매치 참가 메서드입니다. 매치 생성은 ReservationController에서 예약 성공하면 매치를 함께 생성합니다.
    public ResponseEntity<String> joinSocialMatch(@RequestBody Long socialMatchId, Model model, HttpServletRequest request) {

        //Long socialMatchId = Long.parseLong(socialMatchIdParameter);
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            return new ResponseEntity<>("로그인 해주세요!", HttpStatus.CONFLICT);
        }
        //나중에 saveSocialUser안에 검증 로직으로 넣을 예정입니다.
        SocialMatch socialMatch = socialMatchService.findSocialMatchWithSocialUserBySocialMatchId(socialMatchId);

        for (SocialUser socialUser : socialMatch.getSocialUsers()) {
            if(socialUser.getUser().getId().equals(sessionUser.getId())){
                return new ResponseEntity<>("이미 신청했습니다", HttpStatus.CONFLICT);
            }
        }
        if (socialMatch.getMaxSize() == socialMatch.getSocialUsers().size()) {
            return new ResponseEntity<>("마감된 매치입니다", HttpStatus.CONFLICT);
        }
        if (!socialMatch.getGender().equals(Gender.BOTH) && !sessionUser.getGender().equals(socialMatch.getGender())) {
            return new ResponseEntity<>("모집 성별과 불일치 합니다", HttpStatus.CONFLICT);
        }

        socialUserService.saveSocialUser(SocialUser.createSocialUser(socialMatch, sessionUser));
        return new ResponseEntity<>("매치 신청 완료!", HttpStatus.CREATED);
    }

    @GetMapping("/team/{sports}")
    public String teamMatchList(@PathVariable String sports, @RequestParam(required = false) String location,
                                @RequestParam(required = false) String date, @RequestParam(required = false) String gender, Model model,
                                HttpServletRequest request) {

        if (location == null) location = "전체";
        if (date == null) date = LocalDate.now().toString();
        if (gender == null) gender = "ALL";

        Gender genderEnum = sportsService.createGenderEnum(gender);
        Sports sportsEnum = sportsService.createSportsEnum(sports);
        SportsInfo sportsInfo = sportsService.getSportsInfo(sports);

        //JPA 구조상, 리스트 탐색 못해서 select 쿼리 두번으로 나눠서 만듬
        List<TeamMatchDto> teamMatches = teamMatchService.getTeamMatches(LocalDate.parse(date), location, genderEnum, sportsEnum);

        model.addAttribute("teamMatches", teamMatches);
        model.addAttribute("sportsInfo", sportsInfo);
        model.addAttribute("location", location);
        model.addAttribute("date", date);
        model.addAttribute("gender", gender);
        model.addAttribute("sports", sports); //안써도 자동으로 넣어주긴 해요
        return "match/teamMatch";
    }

    @GetMapping("/teamMatch/{teamMatchId}/user")
    @ResponseBody
    public Object choiceTeamUserList(@PathVariable Long teamMatchId, HttpSession session)  {

        User sessionUser = (User) session.getAttribute("user");
        //select 쿼리 두번 나가게 설계했어요.
        TeamMatch teamMatch = teamMatchService.findOneWithTeamUserAndUser(teamMatchId);

        if (sessionUser == null) {
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.CONFLICT);
        }
        if (teamMatch.getMaxSize() == teamMatch.getTransientTeamUsers().size()) {
            return new ResponseEntity<>("마감된 매치입니다", HttpStatus.CONFLICT);
        }
        if (sessionUser.getTeam() == null) {
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);
        }

        //주의! getTeam()은 내용물은 같더라도, 만든 곳이 달라서 다른 객체이다
        for (TeamUser teamUser : teamMatch.getTransientTeamUsers()) {
            if(teamUser.getTeam().getId().equals(sessionUser.getTeam().getId())) {
                return new ResponseEntity<>("이미 팀이 신청한 경기입니다", HttpStatus.CONFLICT);
            }
        }

        List<User> users = userService.findUsersByTeamIdAndGender(sessionUser.getTeam().getId(), teamMatch.getGender());

        //HttpStatus 200 자동으로 됨
        ModelAndView mav = new ModelAndView("match/choiceTeamMemberForm");
        mav.addObject("users", users);
        mav.addObject("teamMatch", teamMatch);
        return mav;
    }

    @PostMapping("/teamMatch/{teamMatchId}/user")
    @ResponseBody
    public ResponseEntity<String> choiceTeamUser(@PathVariable Long teamMatchId, @RequestBody List<Long> userIdList,
                                 HttpSession session) {

        TeamMatch teamMatch = teamMatchRepository.findWithTeamUserByTeamMatchId(teamMatchId);

        if(teamMatch.getMaxSize().equals(teamMatch.getTeamUsers().size())){
            return new ResponseEntity<>("마감된 매치입니다", HttpStatus.CONFLICT);
        }

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.CONFLICT);
        }
        if (sessionUser.getTeam() == null) {
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);
        }
        if (userIdList == null) {
            String message = teamMatch.getMaxSize()/2 + "명을 뽑아주세요";
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        if (teamMatch.getMaxSize() / 2 != userIdList.size()) {
            String message = teamMatch.getMaxSize()/2 + "명을 뽑아주세요";
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        //같은 팀 멤버를 신청하는거니까, 로그인 유저의 팀으로 퉁쳤습니다
        List<TeamUser> teamUsers = new ArrayList<>();
        for (Long userId : userIdList) {
            teamUsers.add(TeamUser.createTeamUser(teamMatch, User.createUser(userId), sessionUser.getTeam()));
        }
        teamUserService.saveTeamUsers(teamUsers);
        return new ResponseEntity<>("팀 매치 참가 완료!", HttpStatus.CREATED);
    }

    @GetMapping("/mercenary/{sports}")
    public String mercenaryMatchList(@PathVariable String sports, @RequestParam(required = false) String location,
                                     @RequestParam(required = false) String date, @RequestParam(required = false) String gender, Model model,
                                     HttpServletRequest request) {

        if (location == null) location = "전체";
        if (date == null) date = LocalDate.now().toString();
        if (gender == null) gender = "ALL";

        Gender genderEnum = sportsService.createGenderEnum(gender);
        Sports sportsEnum = sportsService.createSportsEnum(sports);
        SportsInfo sportsInfo = sportsService.getSportsInfo(sports);

        List<MercenaryMatch> mercenaryMatchWithReservation = mercenaryMatchService.
                findMercenaryMatchWithAllEntityAndTeamListByDateAndLocationAndGender(LocalDate.parse(date), location, genderEnum, sportsEnum);

        model.addAttribute("mercenaryMatchWithReservation", mercenaryMatchWithReservation);
        model.addAttribute("sportsInfo", sportsInfo);
        model.addAttribute("location", location);
        model.addAttribute("date", date);
        model.addAttribute("gender", gender);
        model.addAttribute("sports", sports); //안써도 자동으로 넣어주긴 해요
        model.addAttribute("currentUrl", request.getRequestURL()); //현재페이지 밑줄 표시
        return "match/mercenaryMatch";
    }

    //용병 신청 Post
    @PostMapping("/mercenary/{sports}")
    @ResponseBody
    public ResponseEntity<String> joinMercenaryInMercenaryMatch(@RequestBody JoinMercenaryForm joinMercenaryForm, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.CONFLICT);
        }
        MercenaryMatch mercenaryMatch = mercenaryMatchService.findWithMercenaryMaxSizeAndMercenaryUser(joinMercenaryForm.getMercenaryMatchId());

        if (!sessionUser.getGender().equals(mercenaryMatch.getGender())) {
            return new ResponseEntity<>("모집 성별과 불일치 합니다", HttpStatus.CONFLICT);
        }

        //용병이 됐든, 팀 멤버가 됐든 매치에 중복 신청 불가  - 리스트에서 본인 계정 PK값 있으면 RETURN (어느 팀에 이미 신청했는지 알려주려먼 조인 해야해서 안했음)
        for (MercenaryMatchUser mercenaryMatchUser : mercenaryMatch.getMercenaryMatchUsers()) {
            if(mercenaryMatchUser.getUser().getId().equals(sessionUser.getId())){
                return new ResponseEntity<>("이미 매치에 참가했습니다", HttpStatus.CONFLICT);
            }
        }

        //같은 팀엔 용병 신청불가
        if(sessionUser.getTeam() != null){
            if(sessionUser.getTeam().getId().equals(joinMercenaryForm.getTeamId())){
                return new ResponseEntity<>("자기 팀에 용병 신청 못합니다", HttpStatus.CONFLICT);
            }
        }

        //용병 모집 마감되면 신청 반려
        for (TeamWithMercenary teamWithMercenary : mercenaryMatch.getTeamsWithMercenary()) {
            if(teamWithMercenary.getTeam().getId().equals(joinMercenaryForm.getTeamId()) && teamWithMercenary.getTeamMercenaryMaxSize().equals(teamWithMercenary.getMercenaries().size()) ){
                return new ResponseEntity<>("용병 모집이 마감됐어요", HttpStatus.CONFLICT);
            }
        }

        if (mercenaryMatch.getMaxSize() == mercenaryMatch.getMercenaryMatchUsers().size()) {
            return new ResponseEntity<>("마감된 매치입니다", HttpStatus.CONFLICT);
        }

        Team team = Team.createTeam(joinMercenaryForm.getTeamId());
        mercenaryUserService.saveMercenaryUser(MercenaryMatchUser.createMercenaryUser(mercenaryMatch, sessionUser, team, UserStatus.MERCENARY));
        return new ResponseEntity<>("용병 신청 완료!", HttpStatus.CREATED);
    }

    @GetMapping("/mercenaryMatch/{mercenaryMatchId}/user")
    @ResponseBody
    public Object choiceMercenaryUserList(@PathVariable Long mercenaryMatchId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");

        MercenaryMatch mercenaryMatch = mercenaryMatchService.findWithMercenaryMaxSizeAndMercenaryUser(mercenaryMatchId);
        List<Long> findUserIdList = new ArrayList<>();

        for (MercenaryMatchUser findUser : mercenaryMatch.getMercenaryMatchUsers()) {
            findUserIdList.add(findUser.getUser().getId());
        }
        if (sessionUser == null) {
            return new ResponseEntity<>("로그인 해주세요", HttpStatus.CONFLICT);
        }
        if (mercenaryMatch.getMaxSize() == mercenaryMatch.getMercenaryMatchUsers().size()) {
            return new ResponseEntity<>("마감된 매치입니다", HttpStatus.CONFLICT);
        }
        if (sessionUser.getTeam() == null) {
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);
        }

        for (MercenaryMatchUser mercenaryMatchUser : mercenaryMatch.getMercenaryMatchUsers()) {
            if(mercenaryMatchUser.getStatus().equals(UserStatus.TEAM)){
                if(mercenaryMatchUser.getTeam().getId().equals(sessionUser.getTeam().getId())){
                    return new ResponseEntity<>("이미 팀이 경기 신청했어요", HttpStatus.CONFLICT);
                }
            }
        }

        if (mercenaryMatch.getTeamsWithMercenary().size() >= 2) {
            return new ResponseEntity<>("이미 두 팀이 참여했어요", HttpStatus.CONFLICT);
        }

        //상대 팀에 용병으로 참가한 팀 멤버는 제외하고 보여준다.
        List<User> users = userService.findUsersByTeamIdAndGenderAndUserIdNotIn(sessionUser.getTeam().getId(), mercenaryMatch.getGender(), findUserIdList);

        //HttpStatus 200 자동으로 됨
        ModelAndView mav = new ModelAndView("match/choiceTeamMemberAndMercenaryForm");
        mav.addObject("users", users);
        mav.addObject("match", mercenaryMatch);
        return mav;
    }

    @PostMapping("/mercenaryMatch/{mercenaryMatchId}/user")
    @ResponseBody
    public ResponseEntity<String> choiceMercenaryUser(@PathVariable Long mercenaryMatchId, @RequestBody JoinMercenaryMatchForm joinMercenaryMatchForm,
                                      HttpSession session) {

        List<Long> userIdList = joinMercenaryMatchForm.getUserIdList();
        Integer mercenaryMaxSize = joinMercenaryMatchForm.getMercenaryMaxSize();

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser.getTeam() == null) {
            return new ResponseEntity<>("팀에 가입해주세요", HttpStatus.CONFLICT);
        }
        if (userIdList == null) {
            return new ResponseEntity<>("팀원은 최소 한명은 있어야 해요", HttpStatus.CONFLICT);
        }
        if (mercenaryMaxSize == null) {
            return new ResponseEntity<>("모집할 용병 수를 정하세요 (용병은 최소 한명은 있어야 해요)", HttpStatus.CONFLICT);
        }

        MercenaryMatch mercenaryMatch = mercenaryMatchRepository.findWithMercenaryUserByMercenaryMatchId(mercenaryMatchId);

        if (mercenaryMaxSize + userIdList.size() != mercenaryMatch.getMaxSize()/2) {
            String message = "용병과 팀원을 포함해 " + mercenaryMatch.getMaxSize() / 2 + "명을 뽑아주세요";
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        List<User> users = User.createUsers(userIdList);
        List<MercenaryMatchUser> mercenaryUsers = new ArrayList<>();
        for (User user : users) {
            mercenaryUsers.add(MercenaryMatchUser.createMercenaryUser(mercenaryMatch, user, sessionUser.getTeam(), UserStatus.TEAM));
        }

        //GET에서 필터링되기는하는데, 이중으로 막아놨어요.
        for (MercenaryMatchUser findMercenaryMatchUser : mercenaryMatch.getMercenaryMatchUsers()) {
            if(userIdList.contains(findMercenaryMatchUser.getUser().getId())){
                String message = "상대 팀에 용병으로 참가한 멤버가 포함됐습니다";
                return new ResponseEntity<>(message, HttpStatus.CONFLICT);
            }
        }

        mercenaryUserService.saveMercenaryUsers(mercenaryUsers);

        MercenaryMatchTeamMercenaryMaxSize mmtmms = MercenaryMatchTeamMercenaryMaxSize.createMercenaryTeam(mercenaryMatch, sessionUser.getTeam(), mercenaryMaxSize);
        mercenaryMatchTeamMercenaryMaxSizeService.save(mmtmms);
        return new ResponseEntity<>("용병 매치 참가 완료!", HttpStatus.CREATED);
    }


}
