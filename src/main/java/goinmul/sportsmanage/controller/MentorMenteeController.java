package goinmul.sportsmanage.controller;


import goinmul.sportsmanage.domain.MentorReview;
import goinmul.sportsmanage.domain.SportsInfo;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.dto.MentorReviewDto;
import goinmul.sportsmanage.repository.MentorReviewRepository;
import goinmul.sportsmanage.repository.UserRepository;
import goinmul.sportsmanage.repository.socialMatch.SocialMatchRepository;
import goinmul.sportsmanage.service.SportsService;
import goinmul.sportsmanage.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MentorMenteeController {

    private final SportsService sportsService;
    private final UserService userService;
    private final SocialMatchRepository socialMatchRepository;
    private final UserRepository userRepository;
    private final MentorReviewRepository mentorReviewRepository;

    @GetMapping("/mentor/review")
    public String reviewList(@RequestParam String sports, Model model) {

        SportsInfo sportsInfo = sportsService.getSportsInfo(sports);
        List<MentorReviewDto> mentorReviewsDto = mentorReviewRepository.findAllGroupByMentorId();
        model.addAttribute("mentorReviewsDto", mentorReviewsDto);
        model.addAttribute("sportsInfo", sportsInfo);
        model.addAttribute("sports", sports);

        return "mentorMentee/reviewList";
    }

    @GetMapping("/mentor/review/{mentorId}")
    public String reviewDetail(@PathVariable Long mentorId, @RequestParam String sports, HttpServletRequest request, Model model) {
        System.out.println("mentorId = " + mentorId);
        SportsInfo sportsInfo = sportsService.getSportsInfo(sports);
        List<MentorReview> mentorReviews = mentorReviewRepository.findByMentorId(mentorId);
        model.addAttribute("mentorReviews", mentorReviews);
        model.addAttribute("sportsInfo", sportsInfo);
        model.addAttribute("sports", sports);
        return "mentorMentee/reviewDetail";
    }


    @GetMapping("/mentor/review/form")
    public String createReviewForm(@RequestParam String sports, @RequestParam(required = false) Long mentorId, HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "pleaseLogin";
        }
        SportsInfo sportsInfo = sportsService.getSportsInfo(sports);
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("sportsInfo", sportsInfo);
        model.addAttribute("sports", sports);
        model.addAttribute("mentorId", mentorId);
        return "mentorMentee/createMentorReviewForm";
    }

    @PostMapping("/mentor/review/form")
    public String createReview(@RequestParam Long mentorId, @RequestParam Integer score, @RequestParam String description,
                               @RequestParam String sports, HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "pleaseLogin";
        }

        SportsInfo sportsInfo = sportsService.getSportsInfo(sports);
        User mentor = User.createUser(mentorId);
        MentorReview mentorMentee = MentorReview.createMentorMentee(mentor, sessionUser, score, description);
        mentorReviewRepository.save(mentorMentee);

        model.addAttribute("sportsInfo", sportsInfo);
        model.addAttribute("sports", sports);
        String nextPage = "redirect:/mentor/review?sports="+sports;
        return nextPage;
    }

}
