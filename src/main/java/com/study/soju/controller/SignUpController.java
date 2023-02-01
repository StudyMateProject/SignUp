package com.study.soju.controller;

import com.study.soju.entity.Member;
import com.study.soju.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SignUpController {
    @Autowired
    SignUpService signUpService;

    @Autowired
    PasswordEncoder passwordEncoder;

    //////////////////////회원가입//////////////////////
    //회원가입 페이지 이동
    @GetMapping("/joinform")
    public String joinform(Model model) {
        //바인딩
        model.addAttribute("memberDTO", new Member.rqJoinMember());
        return "SignUp/JoinForm";
    }

    //회원가입 진행
    @PostMapping("/joinform/join")
    public String join(Member.rqJoinMember rqJoinMember) {
        //Member DTO / 비밀번호 암호화 메서드 전송
        signUpService.joinMember(rqJoinMember, passwordEncoder);
        return "SignUp/LoginForm";  //로그인으로 이동
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

    //핸드폰 번호 중복체크
    @PostMapping("/joinform/phonecheck")
    @ResponseBody
    public String checkPhone(String phoneNumber) {
        String checkPhone = signUpService.checkPhone(phoneNumber);
        return checkPhone;
    }

    //////////////////////로그인//////////////////////
    //로그인
    @PostMapping("/loginform/login")
    public String login(@RequestParam(value = "emailId") String emailId) {
        signUpService.loadUserByUsername(emailId);
        return "Main";
    }

    //////////////////////ID찾기//////////////////////

    //ID 찾기 페이지 이동
    @GetMapping("/loginform/findidform")
    public String findIdForm(Model model) {
        //바인딩
        model.addAttribute("memberDTO", new Member.rqFindId());
        return "SignUp/FindId";
    }

    //ID 찾기
    @PostMapping("/loginform/findidform/findid")
    @ResponseBody
    public String findId(Member.rqFindId rqFindId){
        String rpFindId = signUpService.findIdSearch(rqFindId);
        return rpFindId;
    }

    //ID 찾기 결과 확인 페이지 이동
    @GetMapping("/loginform/findidform/checkid")
    public String checkId(String emailId, Model model) {
        model.addAttribute("emailId", emailId);
        return "SignUp/FindIdResult";
    }

    //////////////////////PWD찾기(재설정)//////////////////////

    //PWD 찾기 이동
    @GetMapping("/loginform/findpwdform")
    public String findPwdForm(Model model) {
        //바인딩
        model.addAttribute("memberDTO", new Member.rqFindPwd());
        return "SignUp/FindPwd";
    }

    //비밀번호 재설정 이메일 인증
    @PostMapping("/loginform/findpwdform/emailcheck")
    @ResponseBody
    public String findPwdEmailCheck(String emailId) {
        String checkEmailId =  signUpService.pwdEmailCheck(emailId);
        return checkEmailId;
    }

    //PW 재설정을 위한 정보확인
    @PostMapping("/loginform/findpwdform/findpwd")
    @ResponseBody
    public String resetPwd(Member.rqFindPwd rqFindPwd) {
        String findPwd =  signUpService.findPwdSearch(rqFindPwd);
        return findPwd;
    }

    //PWD 재설정 페이지 이동
    @GetMapping("/loginform/findpwdform/resetpwdform")
    public String resetPwdForm(String emailId, Model model) {
        //바인딩
        model.addAttribute("emailId", emailId);
        return "SignUp/ResetPwd";
    }

    //PWD 재설정
    @PostMapping("/loginform/findpwdform/resetpwdform/resetpwd")
    public String resetPwd(Member.rqResetPwd rqResetPwd){
        signUpService.resetPwd(rqResetPwd, passwordEncoder);
        return "SignUp/LoginForm";
    }
}
