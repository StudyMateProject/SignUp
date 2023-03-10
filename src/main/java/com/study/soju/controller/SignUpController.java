package com.study.soju.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.study.soju.config.GoogleLogin;
import com.study.soju.config.IamPortPass;
import com.study.soju.entity.Member;
import com.study.soju.service.SignUpOAuthService;
import com.study.soju.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SignUpController {
    @Autowired
    SignUpService signUpService;

    @Autowired
    SignUpOAuthService signUpOAuthService;

    @Autowired
    PasswordEncoder passwordEncoder;

    ////////////////////////////////////////////////회원가입////////////////////////////////////////////////
    //회원가입 페이지 이동
    @GetMapping("/joinform")
    public String joinform(Model model) {
        model.addAttribute("memberDTO", new Member.rqJoinMember());
        return "/SignUp/JoinForm";
    }

    //회원가입 진행 (회원정보 저장)
    @PostMapping("/joinform/join")
    public String join(Member.rqJoinMember rqJoinMember) {
        //Member DTO / 비밀번호 암호화 메서드(passwordEncoder) 전송
        signUpService.joinMember(rqJoinMember, passwordEncoder);
        return "/SignUp/LoginForm";  //로그인으로 이동
    }

    //이메일 중복체크 & 이메일 전송 SMTP
    @PostMapping("/joinform/emailcheck")
    @ResponseBody
    public String emailCheck(String emailId) {
        String checkEmailId =  signUpService.checkEmailId(emailId); //이메일 존재 여부
        return checkEmailId;
    }

    //닉네임 중복체크
    @PostMapping("/joinform/nicknamecheck")
    @ResponseBody
    public String nicknameCheck(String nickname) {
        String checkNickname = signUpService.checkNickname(nickname); //닉네임 존재 여부
        return checkNickname;
    }

    //핸드폰 번호 중복체크
    @PostMapping("/joinform/phonecheck")
    @ResponseBody
    public String checkPhone(String phoneNumber) {
        String checkPhone = signUpService.checkPhone(phoneNumber); //핸드폰 번호 존재 여부
        return checkPhone;
    }

    //본인인증 IamPort서버 통신
    @PostMapping("/joinform/certifications")
    @ResponseBody
    public HashMap<String, String> certifications(String impUid) {
        JsonNode jsonToken = IamPortPass.getToken(); //서버로 부터 토큰값 받아옴 (Json 형식)
        String accessToken = jsonToken.get("response").get("access_token").asText(); //서버로 부터 토큰값 받아옴 (Text)

        JsonNode userInfo = IamPortPass.getUserInfo(impUid, accessToken); //유저정보 가져옴
        String birthday = userInfo.get("response").get("birthday").asText(); //userInfo에 들어 있는 생년월일
        String name = userInfo.get("response").get("name").asText(); //userInfo에 들어 있는 이름
        String phoneNumber = userInfo.get("response").get("phone").asText(); //userInfo에 들어 있는 핸드폰 번호

        HashMap<String, String> map = new HashMap<>(); //생년월일 / 이름 / 핸드폰 번호 HashMap으로 만들어 전송
        map.put("birthday", birthday);
        map.put("name", name);
        map.put("phoneNumber", phoneNumber);
        return map; //map에 정보 저장 후 전송
    }

    //네이버 로그인 추가정보 입력 페이지 이동
    @PostMapping("/loginform/naverauthentication")
    public String loginAuthentication(Member.rqJoinSocial rqJoinSocial, Model model) {
        Member member = signUpOAuthService.findSocialUser(rqJoinSocial.getEmailId());
        if( member != null ) {
            return "redirect:/oauth2/authorization/naver";
        } else {
            model.addAttribute("memberDTO", rqJoinSocial);
            return "/SignUp/NaverJoin";
        }
    }

    //소셜 회원가입
    @PostMapping("/loginform/socialjoin")
    public String JoinSocial(Member.rqJoinSocial rqJoinSocial) {
        signUpOAuthService.JoinSocial(rqJoinSocial);
        return "/SignUp/Loginform";
    }

    ////////////////////////////////////////////////로그인////////////////////////////////////////////////
    //로그인
    @PostMapping("/loginform/login")
    public String login(@RequestParam(value = "emailId") String emailId) {
        signUpService.loadUserByUsername(emailId);
        return "Main";
    }

    //네이버 로그인 API 콜백
    @GetMapping("/loginform/naverlogincallback")
    public String naverCallback(Model model) {
        model.addAttribute("memberDTO", new Member.rqJoinSocial());
        return "/SignUp/NaverLoginCallback";
    }

    //구글 서버 통신
    @GetMapping("/loginform/googleauthentication")
    public String googleToken(String code, Model model) {
        JsonNode jsonToken = GoogleLogin.getAccessToken(code);
        String accessToken = jsonToken.get("access_token").asText();

        JsonNode userInfo = GoogleLogin.getUserInfo(accessToken);
        String emailId = userInfo.get("email").asText();

        Member member = signUpOAuthService.findSocialUser(emailId);
        if( member != null ) {
            return "redirect:/oauth2/authorization/google";
        }
        model.addAttribute("emailId", emailId);
        model.addAttribute("memberDTO", new Member.rqJoinSocial());
        return "/SignUp/GoogleJoin";
    }

    ////////////////////////////////////////////////ID찾기////////////////////////////////////////////////
    //ID 찾기 페이지 이동
    @GetMapping("/loginform/findidform")
    public String findIdForm(Model model) {
        //바인딩
        model.addAttribute("memberDTO", new Member.rqFindId());
        return "/SignUp/FindId";
    }

    //ID 찾기
    @PostMapping("/loginform/findidform/findid")
    @ResponseBody
    public Member.rpFindId findId(Member.rqFindId rqFindId){
        Member.rpFindId rpFindId = signUpService.findIdSearch(rqFindId);
        return rpFindId;
    }

    //ID 찾기 결과 확인 페이지 이동
    @GetMapping("/loginform/findidform/checkid")
    public String checkId(String emailId, String platform, Model model) {
        model.addAttribute("emailId", emailId);
        model.addAttribute("platform", platform);
        return "/SignUp/FindIdResult";
    }

    ////////////////////////////////////////////////PWD찾기(재설정)////////////////////////////////////////////////

    //PWD 찾기 이동
    @GetMapping("/loginform/findpwdform")
    public String findPwdForm(Model model) {
        //바인딩
        model.addAttribute("memberDTO", new Member.rqFindPwd());
        return "/SignUp/FindPwd";
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
        return "/SignUp/ResetPwd";
    }

    //PWD 재설정
    @PostMapping("/loginform/findpwdform/resetpwdform/resetpwd")
    public String resetPwd(Member.rqResetPwd rqResetPwd){
        signUpService.resetPwd(rqResetPwd, passwordEncoder);
        return "/Main";
    }

}
