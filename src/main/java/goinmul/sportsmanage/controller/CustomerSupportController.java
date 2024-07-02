package goinmul.sportsmanage.controller;

import goinmul.sportsmanage.domain.CustomerSupport;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.repository.CustomerSupportRepository;
import goinmul.sportsmanage.service.CustomerSupportService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerSupportController {

    private final CustomerSupportRepository customerSupportRepository;
    private final CustomerSupportService customerSupportService;

    @GetMapping("/customer")
    public String customerPostForm() {
        return "customerSupport/createCustomerPostForm";
    }

    @PostMapping("/customer")
    public String createCustomerPost(@RequestParam String title, @RequestParam String content, @RequestParam(required = false) boolean secret,
                                     Model model, HttpSession session) {

        User sessionUser = (User) session.getAttribute("user");
        CustomerSupport customerSupport = CustomerSupport.createPost(sessionUser, title, content, secret);

        // 포스트 저장
        customerSupportService.saveCustomerPost(customerSupport);

        // 성공 페이지로 리다이렉트
        return "customerSupport/customerPostSuccess";
    }

    @GetMapping("/customers/{id}")
    public String customerPostForm(@PathVariable Long id, Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        CustomerSupport post = customerSupportRepository.findOne(id);

        if(post.isSecret()){
            if(sessionUser == null || (sessionUser!=null && !post.getUser().getId().equals(sessionUser.getId()))) {
                model.addAttribute("message", "비밀 글입니다");
                return "alertPage";
            }
        }


        model.addAttribute("post", post);
        return "customerSupport/customerPostDetail";
    }


    @GetMapping("/customers")
    public String customerPostList(Model model) {
        List<CustomerSupport> posts = customerSupportRepository.findAll();
        model.addAttribute("posts", posts);
        return "customerSupport/customerBoard";
    }

}
