package com.study.soju.entity;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Map;

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

    @Column(length = 255)
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

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    //소셜 request
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class rqJoinSocial {
        private String emailId;
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
        public Member toEntity() {

//            String enPassword = passwordEncoder.encode(emailId);

            //변환된 Entity반환
            return Member.builder()
                    .emailId(emailId)
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

    //ID 찾기 request
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class rqFindId {
        private String name;
        private String phoneNumber;

        public Member toEntity() {
            return Member.builder()
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class rpFindId {
        private String emailId;
        private String platform;

        public rpFindId (String emailId) {
            this.emailId = getEmailId();
            this.platform = getPlatform();
        }
    }

    //PWD 찾기 request
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class rqFindPwd {
        private String emailId;
        private String name;
        private String phoneNumber;

        public Member toEntity() {
            return Member.builder()
                    .emailId(emailId)
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .build();
        }
    }

    //PWD reset
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class rqResetPwd {
        private String emailId;
        private String pwd;

        public Member toEntity(PasswordEncoder passwordEncoder){
            String enPassword = passwordEncoder.encode(pwd);
            //비밀번호 암호화
            return Member.builder()
                    .emailId(emailId)
                    .pwd(enPassword)
                    .build();
        }
    }

    // OAuth - Google DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class oAuthAttributes {
        private Map<String, Object> attributes;
        private String nameAttributeKey;
        private String emailId;
        private String platform;
        private String name;
        private String birthday;
        private String phoneNumber;
        private String gender;

        public Member toEntity() {
            return Member.builder()
                    .emailId(emailId)
                    .platform(platform)
                    .roleName("USER")
                    .profileImage("noImage.jpeg")
                    .build();
        }

        @Builder
        public oAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String emailId, String name, String gender, String birthyear, String birthday, String phoneNumber, String platform) {
            if ( "naver".equals(platform) ) {
                this.birthday = birthyear + "-" + birthday;
                this.gender = gender;
                this.phoneNumber = phoneNumber;
                this.attributes = attributes;
                this.nameAttributeKey = nameAttributeKey;
                this.emailId = emailId;
                this.platform = platform;
                this.name = name;
            } else {
                this.attributes = attributes;
                this.nameAttributeKey = nameAttributeKey;
                this.emailId = emailId;
                this.platform = platform;
            }
        }

        public static oAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
            if ( "naver".equals(registrationId) ) {
                return ofNaver(registrationId, userNameAttributeName, attributes);
            }
            return ofGoogle(registrationId, userNameAttributeName, attributes);
        }

        @SuppressWarnings("unchecked")
        public static oAuthAttributes ofNaver(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return oAuthAttributes.builder()
                    .emailId((String) response.get("email"))
                    .name((String) response.get("name"))
                    .gender((String) response.get("gender"))
                    .birthyear((String) response.get("birthyear"))
                    .birthday((String) response.get("birthday"))
                    .phoneNumber((String) response.get("mobile"))
                    .platform(registrationId)
                    .attributes(response)
                    .nameAttributeKey(userNameAttributeName)
                    .build();
        }

        public static oAuthAttributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
            return oAuthAttributes.builder()
                    .emailId((String) attributes.get("email"))
                    .platform(registrationId)
                    .attributes(attributes)
                    .nameAttributeKey(userNameAttributeName)
                    .build();
        }
    }
}
