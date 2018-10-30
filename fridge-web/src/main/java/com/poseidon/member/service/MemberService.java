package com.poseidon.member.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Base64;

import javax.mail.MessagingException;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.poseidon.member.model.MemberRequest;
import com.poseidon.notification.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberClient client;
    private final NotificationService notification;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${noti.sender}")
    private String sender;
    
    @Value("${noti.token-expire-time}")
    private long tokenExpireTime;
    
    @Value("${noti.token-try-limit}")
    private int tokenTryLimit;
    
    public static final int MAX_TOKEN_SIZE = 20;
    
    public void sendVerificationMailForForgotPassword(MemberRequest memberRequest) throws MessagingException, UnsupportedEncodingException {
        String passwordToken = generatePasswordToken();
        memberRequest.setPasswordToken(passwordToken);
        memberRequest.setPasswordTokenExpireTime(setPasswordTokenExpireTime());
        memberRequest.setPasswordTokenTry(0);
        
        client.changeMember(memberRequest.getId(), memberRequest);
        
        String token = encodeToken(memberRequest.getUsername(), passwordToken);
        
        final Context context = new Context();
        context.setVariable("token", token);
        notification.sendNotificationForgotPasswordVerification(sender, memberRequest.getUsername(), context);
    }
    
    private String generatePasswordToken() {
        return RandomStringUtils.randomAlphanumeric(MAX_TOKEN_SIZE);
    }
    
    private LocalDateTime setPasswordTokenExpireTime() {
        return LocalDateTime.now().plusMinutes(tokenExpireTime);
    }
    
    private String encodeToken(String username, String passwordToken) throws UnsupportedEncodingException {
        String source = username + ":" + passwordToken;
        byte[] src = source.getBytes("UTF-8");
        String token = Base64.getUrlEncoder().encodeToString(src);
        
        log.info("source: " + source);
        return token;
    }
    
    public boolean verifyTokenForgotPassword(String token) throws UnsupportedEncodingException {
        String[] sources = decodeToken(token);
        if(sources.length == 2) {
            String username = sources[0];
            String passwordToken = sources[1];
            MemberRequest memberRequest = client.loadByUsername(username);
            int tryCount = memberRequest.getPasswordTokenTry();
            if(tryCount < tokenTryLimit) {
                memberRequest.setPasswordTokenTry(tryCount + 1);
                client.changeMember(memberRequest.getId(), memberRequest);
                return isValidToken(memberRequest, passwordToken);
            }
        }
        return false;
    }
    
    private String[] decodeToken(String token) throws UnsupportedEncodingException {
        byte[] decoded = Base64.getUrlDecoder().decode(token);
        String source = new String(decoded, "UTF-8");
        String[] sources = source.split(":");
        if(sources.length == 2) {
            log.info("username: " + sources[0] + ", passwordToken: " + sources[1]);
            return sources;
        }
        return null;
    }
    
    private boolean isValidToken(MemberRequest memberRequest, String passwordToken) {
        return memberRequest.getPasswordToken().equals(passwordToken) && 
                memberRequest.getPasswordTokenExpireTime().isAfter(LocalDateTime.now());
    }
    
    public boolean passwordChangeWithToken(MemberRequest memberRequest) throws UnsupportedEncodingException {
        String[] sources = decodeToken(memberRequest.getToken());
        if(sources.length == 2) {
            String username = sources[0];
            String passwordToken = sources[1];
            MemberRequest updatedMember = client.loadByUsername(username);
            if(isValidToken(updatedMember, passwordToken)) {
                updatedMember.setPasswordTokenTry(tokenTryLimit);
                updatedMember.setPassword(passwordEncoder.encode(memberRequest.getPassword()));
                client.changeMember(updatedMember.getId(), updatedMember);
                return true;
            }
        }
        return false;
    }

}
