package com.study.soju.service;

import com.study.soju.entity.Member;
import com.study.soju.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class SignUpOAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    MemberRepository memberRepository;

    //이메일 존재 여부 확인
    public Member findSocialUser(String emailId) {
        Member member = memberRepository.findByEmailId(emailId);
        return member;
    }

    //
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId(); // 플랫폼 이름값
//        String userNameAttributeName = userRequest.getClientRegistration()
//                .getProviderDetails()
//                .getUserInfoEndpoint()
//                .getUserNameAttributeName(); // oAuth2User에서 sub값에 해당하는 키값
        String userNameAttributeName = "email"; // oAuth2User에서 email값에 해당하는 키값

        Member.oAuthAttributes oAuthAttributes = Member.oAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Member entityMember = oAuthAttributes.toEntity();
        Member member = memberRepository.findByEmailId(entityMember.getEmailId());
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRoleName())),
                oAuthAttributes.getAttributes(),
                oAuthAttributes.getNameAttributeKey());
    }

    //소셜용 회원가입
    public void JoinSocial(Member.rqJoinSocial rqJoinSocial) {
        Member joinMember = rqJoinSocial.toEntity();
        memberRepository.save(joinMember);
    }
}
