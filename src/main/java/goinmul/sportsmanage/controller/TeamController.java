package goinmul.sportsmanage.controller;


import goinmul.sportsmanage.domain.SportsInfo;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.teamMatch.Team;
import goinmul.sportsmanage.repository.TeamRepository;
import goinmul.sportsmanage.repository.UserRepository;
import goinmul.sportsmanage.service.SportsService;
import goinmul.sportsmanage.service.TeamService;
import goinmul.sportsmanage.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TeamController {

    private final UserService userService;
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final SportsService sportsService;

    @GetMapping("/team")
    public String createTeamForm() {

        return "team/createTeamForm";
    }

    @PostMapping("/team")
    @ResponseBody
    public boolean createTeam(@RequestBody String name, HttpServletRequest request, Model model) {
        log.info("{}",name);
        Team team = Team.createTeam(name);
        return teamService.saveTeam(team);
    }

    @GetMapping("/teams")
    public String teamList(@RequestParam String sports, Model model, HttpSession session) {
        SportsInfo sportsInfo = sportsService.getSportsInfo(sports);
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "pleaseLogin";
        }
        List<Team> teams = teamRepository.findAll();
        model.addAttribute("teams", teams);
        model.addAttribute("sports", sports);
        model.addAttribute("sportsInfo", sportsInfo);
        return "team/teamList";
    }

    @PostMapping("/teams")
    public String joinTeam(Model model, HttpSession session, @RequestParam Long teamId,
                           @RequestParam String sports) {
        SportsInfo sportsInfo = sportsService.getSportsInfo("futsal");
        User sessionUser = (User) session.getAttribute("user");
        Team findTeam = teamRepository.findOne(teamId); //세션에 팀 이름을 반영하기위함
        sessionUser.changeTeam(findTeam);

        if (sessionUser == null) {
            return "pleaseLogin";
        }
        userService.changeTeam(sessionUser, Team.createTeam(teamId));
        return "redirect:/user/myPage?sports="+sports;
    }


}
