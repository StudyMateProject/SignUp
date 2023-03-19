package com.study.soju.entity;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

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

    @Column(length = 100)
    private String detailAddress;

    @Column(length = 10, nullable = false)
    private String studyType;

    @Column(length = 10, nullable = false)
    private String platform;

    @Column(length = 100, nullable = false)
    private String roleName;

    @Column(length = 100)
    private String profileImage;

    @Column(length = 255)
    private String selfIntro;

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
                    .address(address)
                    .detailAddress(detailAddress)
                    .studyType(studyType)
                    .platform(platform)
                    .roleName(roleName)
                    .profileImage("no_file")
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
                    .address(address)
                    .detailAddress(detailAddress)
                    .studyType(studyType)
                    .platform(platform)
                    .roleName(roleName)
                    .profileImage("no_file")
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class rpProfile {
        private String emailId;
        private String nickname;
        private String selfIntro;
        private String studyType;
        private String profileImage;

        public rpProfile (Member member) {
            this.emailId = member.getEmailId();
            this.nickname = member.getNickname();
            this.selfIntro = member.getSelfIntro();
            this.studyType = member.getStudyType();
            this.profileImage = member.getProfileImage();
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

    //회원정보 수정
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class rqModifyMember {
        private String emailId;
        private String nickname;
        private String phoneNumber;
        private String address;
        private String detailAddress;
        private String studyType;
        private String birthday;
        private String name;
        private String gender;
        private String selfIntro;
        private String platform;
        private String roleName;
        private String profileImage;
        private MultipartFile imageFile;

        public Member toEntity() {
            //변환된 Entity반환
            return Member.builder()
                         .emailId(emailId)
                         .name(name)
                         .nickname(nickname)
                         .birthday(birthday)
                         .gender(gender)
                         .phoneNumber(phoneNumber)
                         .address(address)
                         .detailAddress(detailAddress)
                         .studyType(studyType)
                         .platform(platform)
                         .roleName(roleName)
                         .selfIntro(selfIntro)
                         .profileImage(profileImage)
                         .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class rpModifyMember {
        private String emailId;
        private String nickname;
        private String phoneNumber;
        private String address;
        private String detailAddress;
        private String studyType;
        private String birthday;
        private String name;
        private String gender;
        private String selfIntro;
        private String platform;
        private String roleName;
        private String profileImage;

        public rpModifyMember(Member member){
            this.emailId = member.getEmailId();
            this.nickname = member.getNickname();
            this.phoneNumber = member.getPhoneNumber();
            this.address = member.getAddress();
            this.detailAddress = member.getDetailAddress();
            this.studyType = member.getStudyType();
            this.birthday = member.getBirthday();
            this.name = member.getName();
            this.gender = member.getGender();
            this.selfIntro = member.getSelfIntro();
            this.platform = member.getPlatform();
            this.roleName = member.getRoleName();
            this.profileImage = member.getProfileImage();
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

    //개인정보 수정

}
