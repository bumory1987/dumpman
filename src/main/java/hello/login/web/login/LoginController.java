package hello.login.web.login;


import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String checkForm(@Valid @ModelAttribute("loginForm") LoginForm form
            , BindingResult bindingResult
            , HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "/login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 패스워가 일치하지 않습니다");
            return "/login/loginForm";
        }


        Cookie cookieId = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(cookieId);

        return "redirect:/";
    }


    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        Cookie memberId = new Cookie("memberId", null);
        memberId.setMaxAge(0);
        response.addCookie(memberId);
        return "redirect:/";
    }

}
