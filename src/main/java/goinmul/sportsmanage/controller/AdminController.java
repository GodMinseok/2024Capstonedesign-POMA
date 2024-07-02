package goinmul.sportsmanage.controller;



import goinmul.sportsmanage.domain.Authority;
import goinmul.sportsmanage.domain.Ground;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.form.AuthorityForm;
import goinmul.sportsmanage.domain.form.GroundForm;
import goinmul.sportsmanage.repository.AdminRepository;
import goinmul.sportsmanage.repository.GroundRepository;
import goinmul.sportsmanage.repository.UserRepository;
import goinmul.sportsmanage.repository.socialMatch.SocialMatchRepository;
import goinmul.sportsmanage.service.GroundService;
import goinmul.sportsmanage.service.SportsService;
import goinmul.sportsmanage.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final SportsService sportsService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final SocialMatchRepository socialMatchRepository;
    private final AdminRepository adminRepository;
    private final GroundRepository groundRepository;
    private final GroundService groundService;

    @GetMapping("/admin")
    public String adminPage(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER)){
            model.addAttribute("message", "관리자만 접근할 수 있어요");
            return "alertPage";
        }
        return "admin/adminPage";
    }

    @GetMapping("/admins")
    public String authorityList(Model model,HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER)){
            model.addAttribute("message", "관리자만 접근할 수 있어요");
            return "alertPage";
        }

        List<User> users = userRepository.findExcludeSuperAdmin();
        model.addAttribute("users", users);

        return "admin/userAuthorityList";
    }

    //관리자로 승급 또는 유저로 강등
    @PostMapping("/admins")
    @ResponseBody
    public boolean grantAuthority(@RequestBody AuthorityForm authorityForm, Model model, HttpSession session) {

        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || !sessionUser.getAuthority().equals(Authority.SUPER_ADMIN))
            return false;

        User user = User.createUser(authorityForm.getId());
        Authority authority = authorityForm.getAuthority();
        return adminRepository.changeAuthority(user, authority);
    }

    @GetMapping("/grounds")
    public String groundList(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER)){
            model.addAttribute("message", "관리자만 접근할 수 있어요");
            return "alertPage";
        }
        List<Ground> grounds = groundRepository.findAll();
        model.addAttribute("grounds", grounds);
        return "admin/groundList";
    }

    @GetMapping("/ground")
    public String groundFrom(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER)){
            model.addAttribute("message", "관리자만 접근할 수 있어요");
            return "alertPage";
        }
        return "admin/createGroundForm";
    }

    @PostMapping("/ground")
    @ResponseBody
    public boolean createGround(@Valid @RequestBody GroundForm groundForm, Model model,
                                HttpSession session, BindingResult result) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || !sessionUser.getAuthority().equals(Authority.SUPER_ADMIN))
            return false;

        if(result.hasErrors()) return false;

        Ground ground = Ground.createGround(groundForm.getName(), groundForm.getLocation(), groundForm.getPrice());
        return groundService.saveGround(ground);
    }


    @GetMapping("/ground/{id}")
    public String updateGroundFrom(@PathVariable Long id, Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER)){
            model.addAttribute("message", "관리자만 접근할 수 있어요");
            return "alertPage";
        }

        Ground ground = groundRepository.findOne(id);
        model.addAttribute("ground", ground);

        return "admin/updateGroundForm";
    }

    @PostMapping("/ground/{id}")
    @ResponseBody
    public boolean updateGround(@Valid @RequestBody GroundForm groundForm,BindingResult bindingResult,
                                 HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null || sessionUser.getAuthority().equals(Authority.USER))
            return false;

        if(bindingResult.hasErrors()) return false;

        return groundService.updateGround(groundForm.getId(), groundForm.getLocation(), groundForm.getName(), groundForm.getPrice());
    }

}
