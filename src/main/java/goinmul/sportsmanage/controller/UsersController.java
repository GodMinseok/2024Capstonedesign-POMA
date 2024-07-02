package goinmul.sportsmanage.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import goinmul.sportsmanage.domain.Reservation;
import goinmul.sportsmanage.domain.SportsInfo;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.dto.JoinedMatchesDto;
import goinmul.sportsmanage.repository.ReservationRepository;
import goinmul.sportsmanage.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;
    private final TeamService teamService;
    private final SportsService sportsService;
    private final ReservationRepository reservationRepository;
    private final GroundService groundService;

    @GetMapping("/user/signup")
    public String createUserForm() {
        return "user/signup";
    }

    //USER 테이블 authority 컬럼 기본값 'USER'
    @PostMapping("/user/signup")
    @ResponseBody
    public boolean createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/user/signin")
    public String loginForm(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null) {
            model.addAttribute("message", "로그인 상태입니다.");
            return "alertPage";
        }

        boolean loginResult = true; // 모델 빈 껍데기 심으려고 작성했어요.
        model.addAttribute("loginResult", loginResult);


        return "user/signin";
    }

    @PostMapping("/user/signin")
    public String login(@ModelAttribute User user, HttpServletRequest request, Model model) {
        boolean loginResult = userService.validateLoginMember(user.getLoginId(), user.getPassword());

        if (loginResult) {
            User findUser = userService.findUserWithTeamByLoginId(user.getLoginId());
            request.getSession().setAttribute("user", findUser);
            model.addAttribute("loginResult", loginResult);
            return "redirect:/";
        } else {
            String loginFailMessage = "로그인 실패";
            model.addAttribute("loginResult", loginResult);
            return "user/signin";
        }
    }

    @GetMapping("/user/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        return "redirect:/";
    }

    @GetMapping("/user/myPage")
    public String myPage(@RequestParam(required = false) String sports, Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "pleaseLogin";
        }
        SportsInfo sportsInfo = sportsService.getSportsInfo(sports);
        model.addAttribute("sportsInfo", sportsInfo);
        model.addAttribute("user", sessionUser);
        model.addAttribute("sports", sports);
        return "user/myPage";
    }

    @GetMapping("/user/matches")
    public String joinMatchList(@RequestParam(required = false) String sports, Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "pleaseLogin";
        }
        SportsInfo sportsInfo = sportsService.getSportsInfo(sports);
        JoinedMatchesDto joinedMatchesDto = userService.findJoinedMatchesDtoByUserId(sessionUser.getId());
        model.addAttribute("joinedMatchesDto", joinedMatchesDto);
        model.addAttribute("sportsInfo", sportsInfo);
        model.addAttribute("user", sessionUser);
        model.addAttribute("sports", sports);
        return "user/joinedMatches";
    }

    @GetMapping("/user/reservations")
    public String reservationList(@RequestParam(required = false) String sports, Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "pleaseLogin";
        }
        SportsInfo sportsInfo = sportsService.getSportsInfo(sports);
        List<Reservation> reservations = reservationRepository.findWithGroundByUserId(sessionUser.getId());

        model.addAttribute("reservations", reservations);
        model.addAttribute("sportsInfo", sportsInfo);
        model.addAttribute("user", sessionUser);
        model.addAttribute("sports", sports);
        return "user/reservations";
    }

    @PostMapping("/payment")
    @ResponseBody
    public boolean addMoney(@RequestBody String imp_uid, HttpSession session) throws JsonProcessingException {
        
        User sessionUser = (User) session.getAttribute("user");
        if(sessionUser == null)
            return false;

        //외부 노출 하면 안되서 외부 파일로 분리했습니다.
        PortOneKeySecret portOneKeySecret = new PortOneKeySecret();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(portOneKeySecret);

        //1단계 - 엑세스 토큰 발급
        Map accessTokenResponse = RestClient.create().post()
                .uri("https://api.iamport.kr/users/getToken")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(Map.class);
        String accessToken = (String) ((Map<?, ?>) accessTokenResponse.get("response")).get("access_token");

        //2단계 - 결제내역 단건 조회 (클라이언트 위변조 검증)
        ResponseEntity<Map> paymentResponse = RestClient.create().get()
                .uri("https://api.iamport.kr/payments/{imp_uid}", imp_uid)
                .header("Authorization", accessToken)
                .retrieve()
                .toEntity(Map.class);
        Integer amount = (Integer) ((Map<?, ?>) paymentResponse.getBody().get("response")).get("amount")  ;

        //3단계 - DB 반영 & 세션 반영
        userService.chargeMoney(sessionUser, amount);
        sessionUser.plusMoney(amount);

        //포트원에서 단건 조회하면, 굳이 위변조 됐는지 확인 안해도 됨
        return true;
    }

    @GetMapping("/user/findId")
    public String findAccountForm(Model model) {
        return "user/findId";
    }

    @PostMapping("/user/findId")
    @ResponseBody
    public boolean findAccount(@RequestBody String email) {
        if(email == null) return false;
        else{
            List<String> list = userService.changePassword(email);
            if(list == null) return false;
            else userService.sendNewPasswordByMail(email, list.get(0), list.get(1));
        }
        return true;
    }

}
