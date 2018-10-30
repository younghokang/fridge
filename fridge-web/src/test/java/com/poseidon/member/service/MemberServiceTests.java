package com.poseidon.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Base64;

import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;

import com.poseidon.member.model.MemberRequest;
import com.poseidon.notification.NotificationService;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK, properties= {"eureka.client.enabled:false"})
@Slf4j
public class MemberServiceTests {
    
    @Autowired
    MemberService memberService;
    
    @MockBean
    MemberClient client;
    
    @MockBean
    NotificationService notification;
    
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Value("${noti.token-expire-time}")
    private long tokenExpireTime;
    
    @Value("${noti.token-try-limit}")
    private int tokenTryLimit;
    
    @Test
    public void sendVerificationMail() throws UnsupportedEncodingException, MessagingException {
        MemberRequest memberRequest = MemberRequest.builder()
                .username("user")
                .password("password")
                .build();
        memberRequest.setId(1L);
        given(client.changeMember(anyLong(), any(MemberRequest.class))).willReturn(memberRequest);
        willDoNothing()
            .given(notification).sendNotificationForgotPasswordVerification(anyString(), anyString(), any(Context.class));
        
        memberService.sendVerificationMailForForgotPassword(memberRequest);
        assertThat(memberRequest.getPasswordToken().length()).isEqualTo(MemberService.MAX_TOKEN_SIZE);
        assertThat(memberRequest.getPasswordTokenExpireTime().getMinute()).isEqualTo(LocalDateTime.now().plusMinutes(tokenExpireTime).getMinute());
    }
    
    @Test
    public void givenPasswordTokenWithUsernameThenBase64Encoded() throws UnsupportedEncodingException {
        String username = "user";
        String passwordToken = "abcd";
        String source = username + ":" + passwordToken;
        byte[] src = source.getBytes("UTF-8");
        String token = Base64.getUrlEncoder().encodeToString(src);
        log.info("token: " + token);
        assertThat(token).isEqualTo("dXNlcjphYmNk");
    }
    
    @Test
    public void givenTokenThenBase64Decode() throws UnsupportedEncodingException {
        String token = "dXNlcjphYmNk";
        byte[] decoded = Base64.getUrlDecoder().decode(token);
        String source = new String(decoded, "UTF-8");
        String[] sources = source.split(":");
        assertThat(sources.length).isEqualTo(2);
        assertThat(sources[0]).isEqualTo("user");
        assertThat(sources[1]).isEqualTo("abcd");
        log.info("username: " + sources[0] + ", passwordToken: " + sources[1]);
    }
    
    @Test(expected=UsernameNotFoundException.class)
    public void whenCallVerifyTokenWithNotExistUserThenThrowUsernameNotFoundException() throws UnsupportedEncodingException {
        String token = "dXNlcjphYmNk";
        given(client.loadByUsername(anyString())).willReturn(null);
        memberService.verifyTokenForgotPassword(token);
    }
    
    @Test
    public void whenCallVerifyTokenWithInvalidTokenThenReturnFalse() throws UnsupportedEncodingException {
        String token = "dXNlcjphYmNk";
        MemberRequest memberRequest = MemberRequest.builder()
                .username("user")
                .password("password")
                .build();
        memberRequest.setId(1L);
        memberRequest.setPasswordToken("qefqef");
        given(client.loadByUsername(anyString())).willReturn(memberRequest);
        given(client.changeMember(memberRequest.getId(), memberRequest)).willReturn(memberRequest);
        assertThat(memberService.verifyTokenForgotPassword(token)).isFalse();
    }
    
    @Test
    public void whenCallVerifyTokenAfterExpireTimeThenReturnFalse() throws UnsupportedEncodingException {
        String token = "dXNlcjphYmNk";
        MemberRequest memberRequest = MemberRequest.builder()
                .username("user")
                .password("password")
                .build();
        memberRequest.setId(1L);
        memberRequest.setPasswordToken("dXNlcjphYmNk");
        memberRequest.setPasswordTokenExpireTime(LocalDateTime.now().minusMinutes(tokenExpireTime));
        given(client.loadByUsername(anyString())).willReturn(memberRequest);
        given(client.changeMember(memberRequest.getId(), memberRequest)).willReturn(memberRequest);
        assertThat(memberService.verifyTokenForgotPassword(token)).isFalse();
    }
    
    @Test
    public void whenCallVerifyTokenOverTryLimitThenReturnFalse() throws UnsupportedEncodingException {
        String token = "dXNlcjphYmNk";
        MemberRequest memberRequest = MemberRequest.builder()
                .username("user")
                .password("password")
                .build();
        memberRequest.setId(1L);
        memberRequest.setPasswordToken("abcd");
        memberRequest.setPasswordTokenExpireTime(LocalDateTime.now().plusMinutes(tokenExpireTime));
        memberRequest.setPasswordTokenTry(tokenTryLimit);
        given(client.loadByUsername(anyString())).willReturn(memberRequest);
        given(client.changeMember(memberRequest.getId(), memberRequest)).willReturn(memberRequest);
        assertThat(memberService.verifyTokenForgotPassword(token)).isFalse();
    }
    
    @Test
    public void whenCallPasswordChangeWithInvalidTokenThenReturnFalse() throws UnsupportedEncodingException {
        MemberRequest memberRequest = MemberRequest.builder()
                .username("user")
                .password("password")
                .build();
        memberRequest.setToken("dXNlcjphYmNk");
        memberRequest.setPasswordToken("abcde");
        memberRequest.setPasswordTokenExpireTime(LocalDateTime.now().plusMinutes(tokenExpireTime));
        given(client.loadByUsername(anyString())).willReturn(memberRequest);
        assertThat(memberService.passwordChangeWithToken(memberRequest)).isFalse();
    }
    
    @Test
    public void whenCallPasswordChangeWithTokenOverExpireTimeThenReturnFalse() throws UnsupportedEncodingException {
        MemberRequest memberRequest = MemberRequest.builder()
                .username("user")
                .password("password")
                .build();
        memberRequest.setToken("dXNlcjphYmNk");
        memberRequest.setPasswordToken("abcd");
        memberRequest.setPasswordTokenExpireTime(LocalDateTime.now().minusMinutes(tokenExpireTime));
        given(client.loadByUsername(anyString())).willReturn(memberRequest);
        assertThat(memberService.passwordChangeWithToken(memberRequest)).isFalse();
    }
    
}
