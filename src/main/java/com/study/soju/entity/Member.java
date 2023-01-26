package com.study.soju.entity;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "Member")
public class Member {
    @Id
    @Column(length = 50)
    private String emailId;

    @Column(length = 255, nullable = false)
    private String pwd;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 20, unique = true, nullable = false)
    private String nickname;

    @Column(length = 10, nullable = false)
    private String birthday;

    @Column(length = 1, nullable = false)
    private String gender;

    @Column(length = 15, unique = true, nullable = false)
    private String phoneNumber;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 10, nullable = false)
    private String studyType;

    @Column(length = 10, nullable = false)
    private String platform;

    @Column(length = 100, nullable = false)
    private String roleName;

    @Column(length = 100, nullable = false)
    private String profileImage;

    @Column(length = 255)
    private String selfIntro;

    @Column(length = 100)
    private String achievement;

////////////////////////////////////////////////////////////////////////////////////////////

    //DTO

    //회원가입 request
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class rqJoinMember {
        private String emailId;
        private String pwd;
        private String name;
        private String nickname;
        private String birthday;
        private String gender;
        private String phoneNumber;
        private String address;
        private String studyType;
        private String platform;
        private String roleName;
        private String detailAddress;


        //DTO를 Entity로 변환
        public Member toEntity(PasswordEncoder passwordEncoder) {
            //비밀번호 암호화
            String enPassword = passwordEncoder.encode(pwd);
            //변환된 Entity반환
            return Member.builder()
                    .emailId(emailId)
                    .pwd(enPassword)
                    .name(name)
                    .nickname(nickname)
                    .birthday(birthday)
                    .gender(gender)
                    .phoneNumber(phoneNumber)
                    .address(address + " " + detailAddress)
                    .studyType(studyType)
                    .platform(platform)
                    .roleName(roleName)
                    .profileImage("no_file")
                    .achievement("신입생")
                    .build();
        }
    }
}
