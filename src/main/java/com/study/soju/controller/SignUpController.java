package com.study.soju.controller;

import com.study.soju.entity.Member;
import com.study.soju.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SignUpController {
    @Autowired
    SignUpService signUpService;

    @Autowired
    PasswordEncoder passwordEncoder;

    //회원가입 페이지 이동
    @GetMapping("/joinform")
    public String joinform(Model model) {
        //바인딩
        model.addAttribute("memberDTO", new Member.rqJoinMember());
        return "SignUp/JoinForm";
    }

    //회원가입 진행
    @PostMapping("/joinform/join")
    public String join(Member.rqJoinMember rqJoinMember, Model model) {
        //Member DTO / 비밀번호 암호화 메서드 전송
       signUpService.joinMember(rqJoinMember, passwordEncoder);
        return "SignUp/LoginForm";  //로그인으로 이동
    }

    //로그인
    @PostMapping("/loginform/login")
    public String login(@RequestParam(value = "emailId") String emailId) {
        signUpService.loadUserByUsername(emailId);
        return "Main";
    }

    //이메일 중복체크 & 이메일 전송 SMTP
    @PostMapping("/joinform/emailcheck")
    @ResponseBody
    public String emailCheck(String emailId) {
        String checkEmailId =  signUpService.checkEmailId(emailId);
        return checkEmailId;
    }

    //닉네임 중복체크
    @PostMapping("/joinform/nicknamecheck")
    @ResponseBody
    public String nicknameCheck(String nickname) {
        String checkNickname = signUpService.checkNickname(nickname);
        return checkNickname;
    }
}
