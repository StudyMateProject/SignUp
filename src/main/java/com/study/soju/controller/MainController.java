package com.study.soju.controller;

import com.study.soju.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    SignUpService signUpService;

    @GetMapping("/")
    public String main() {
        return "Main";
    }

    @GetMapping("/loginform")
    public String loginform(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "errorMsg", required = false) String errorMsg,
                            Model model){
        model.addAttribute("error", error);
        model.addAttribute("errorMsg", errorMsg);
        return "SignUp/LoginForm";
    }
}
