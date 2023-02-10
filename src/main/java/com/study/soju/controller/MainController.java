package com.study.soju.controller;

import com.study.soju.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class MainController {
    @Autowired
    SignUpService signUpService;

    @GetMapping("/")
    public String main(Principal principal) {
        if ( principal == null ) {
            return "redirect:/n";
        }
        return "Main";
    }

    @GetMapping("/n")
    public String nmain() {
        return "Main";
    }

    @GetMapping("/loginform")
    public String loginform(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "errorMsg", required = false) String errorMsg,
                            Model model) {
        String googleUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                            "scope=" + "email" + //가져올 정보들
                            "&access_type=" + "offline" +  //액세스 토큰을 새로고침 할 수 있는지
                            "&include_granted_scopes=" + "true" + //위에서 설정한 스코프를 요청
                            "&response_type=" + "code" +  // 요청한 스코프 값들을 받기 위해 승인 코드를 발급
                            "&state=" + "security_token%3D138r5719ru3e1%26url%3Dhttps://oauth2.example.com/token" + //통신을 유지하는 문자열 값
                            "&redirect_uri=" + "http://localhost:8888/loginform/googleauthentication" + //리디렉션 uri
                            "&client_id=" + "826440518994-6t8ghsdrabmgh8vfsnimpofjnmmgcocn.apps.googleusercontent.com"; //client ID
        model.addAttribute("googleUrl", googleUrl);
        model.addAttribute("error", error);
        model.addAttribute("errorMsg", errorMsg);
        return "SignUp/LoginForm";
    }


}
