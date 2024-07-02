package goinmul.sportsmanage.controller;



import goinmul.sportsmanage.domain.mercenaryMatch.UserStatus;
import goinmul.sportsmanage.repository.socialMatch.SocialMatchRepository;
import goinmul.sportsmanage.service.SportsService;
import goinmul.sportsmanage.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SportsService sportsService;
    private final UserService userService;
    private final SocialMatchRepository socialMatchRepository;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        return "mainPage";
    }



}
