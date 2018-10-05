package com.poseidon.member.controller;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poseidon.member.model.Member;
import com.poseidon.member.model.MemberRequest;
import com.poseidon.member.service.MemberRestService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberRestService service;
    private final PasswordEncoder passwordEncoder;
    
    @GetMapping("/signup")
    public String signupForm(MemberRequest memberRequest, Model model) {
        model.addAttribute("memberRequest", memberRequest);
        return "members/signup";
    }
    
    @PostMapping("/signup")
    public String signupProcessing(@ModelAttribute @Valid MemberRequest memberRequest, Errors errors) {
        if(errors.hasErrors()) {
            return "members/signup";
        }
        Member existsMember = service.loadByUsername(memberRequest.getUsername());
        if(existsMember != null) {
            errors.rejectValue("username", "field.exists.member.username");
            return "members/signup";
        }
        
        memberRequest.generateNewUser(passwordEncoder);
        service.register(memberRequest);
        return "redirect:/signin";
    }
    
    @GetMapping("/signin")
    public String signinForm() {
        return "members/signin";
    }
    
    @GetMapping("/changePassword")
    public String changePassword(MemberRequest memberRequest, Model model) {
        model.addAttribute("memberRequest", memberRequest);
        return "members/changePassword";
    }
    
    @PostMapping("/changePassword")
    public String changePasswordProcessing(@AuthenticationPrincipal Member member, 
            @ModelAttribute @Valid MemberRequest memberRequest, Errors errors, RedirectAttributes redirectAttributes) {
        if(errors.hasErrors()) {
            return "members/changePassword";
        }
        if(memberRequest.getCurrentPassword().equals(memberRequest.getPassword())) {
            errors.rejectValue("password", "field.duplicated.member.password");
            return "members/changePassword";
        }
        if(!passwordEncoder.matches(memberRequest.getCurrentPassword(), member.getPassword())) {
            errors.rejectValue("currentPassword", "field.not_same.member.password");
            return "members/changePassword";
        }
        service.changePassword(memberRequest);
        
        redirectAttributes.addFlashAttribute("changePasswordDone", true);
        return "redirect:/changePassword";
    }
    
    @GetMapping("/withdraw")
    public String withdraw(MemberRequest memberRequest, Model model) {
        model.addAttribute("memberRequest", memberRequest);
        return "members/withdraw";
    }
    
    @PostMapping("/withdraw")
    public String withdrawProcessing(@AuthenticationPrincipal Member member, 
            @ModelAttribute @Valid MemberRequest memberRequest, Errors errors) {
        if(errors.hasErrors()) {
            return "members/withdraw";
        }
        if(!passwordEncoder.matches(memberRequest.getPassword(), member.getPassword())) {
            errors.rejectValue("password", "field.not_same.member.password");
            return "members/withdraw";
        }
        
        service.withdraw(member.getId());
        SecurityContextHolder.clearContext();
        return "members/withdrawDone";
    }
    
}
