package com.study.soju.service;

import com.study.soju.dto.MailKeyDTO;
import com.study.soju.entity.Member;
import com.study.soju.repository.MemberRepository;
import lombok.Builder;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Builder
@Service
public class SignUpService implements UserDetailsService {
    @Autowired
    MemberRepository memberRepository;

    //////////////////////회원가입//////////////////////
    public void joinMember(Member.rqJoinMember rqJoinMember, PasswordEncoder passwordEncoder) {
        //DTO > Entity + 암호화 메서드
        Member joinMember = rqJoinMember.toEntity(passwordEncoder);
        //변환된 Entity DB에 저장 & 받아옴
        memberRepository.save(joinMember);
    }

//    //소셜 통합 인증
//    public void joinMemberNaver(Member.rqJoinSocial rqJoinSocial) {
//        Member joinMemberNaver = rqJoinSocial.toEntity();
//    }

    //아이디 중복체크
    public String checkEmailId(String emailId) {
        Member member = memberRepository.findByEmailId(emailId);
        if( member != null ) {
            return "no";
        } else {
            //MailKeyDTO불러와서 사용
            String mailKey = new MailKeyDTO().getKey(7, false);

            //Mail Server 설정
            String charSet = "UTF-8"; // 사용할 언어셋
            String hostSMTP = "smtp.naver.com"; // 사용할 SMTP
            String hostSMTPid = ""; // 사용할 SMTP에 해당하는 ID - 이메일 형식
            String hostSMTPpwd = ""; // 사용할 ID에 해당하는 PWD

            // 가장 중요한 TLS설정 - 이것이 없으면 신뢰성 에러가 나온다
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            // 보내는 사람 E-Mail, 제목, 내용
            String fromEmail = ""; // 보내는 사람 email - - hostSMTPid와 동일하게 작성
            String fromName = "관리자"; // 보내는 사람 이름
            String subject = "[Study with me] 이메일 인증번호 발송 안내입니다."; // 제목

            // 받는 사람 E-Mail 주소
            String mail = emailId; // 받는 사람 email

            try {
                HtmlEmail email = new HtmlEmail(); // Email 생성
                email.setDebug(true);
                email.setCharset(charSet); // 언어셋 사용
                email.setSSL(true);
                email.setHostName(hostSMTP); // SMTP 사용
                email.setSmtpPort(587);	// SMTP 포트 번호 입력

                email.setAuthentication(hostSMTPid, hostSMTPpwd); // 메일 ID, PWD
                email.setTLS(true);
                email.addTo(mail); // 받는 사람
                email.setFrom(fromEmail, fromName, charSet); // 보내는 사람
                email.setSubject(subject); // 제목
                email.setHtmlMsg(
                        "<p>" + "[메일 인증 안내입니다.]" + "</p>" +
                                "<p>" + "Study with me를 사용해 주셔서 감사드립니다." + "</p>" +
                                "<p>" + "아래 인증 코드를 '인증번호'란에 입력해 주세요." + "</p>" +
                                "<p>" + mailKey + "</p>"); // 본문 내용
                email.send(); // 메일 보내기
                // 메일 보내기가 성공하면 메일로 보낸 랜덤키를 콜백 메소드에도 전달
                return mailKey;
            } catch (Exception e) {
                System.out.println(e);
                // 메일 보내기가 실패하면 "no"를 콜백 메소드에 전달
                return "no";
            }
        }
    }

    //닉네임 중복체크
    public String checkNickname (String nickname) {
        Member member = memberRepository.findByNickname(nickname);
        if ( member != null ) {
            return "no";
        } else {
            return nickname;
        }
    }

    //핸드폰번호 중복체크
    public String checkPhone (String phoneNumber) {
        Member member = memberRepository.findByPhoneNumber(phoneNumber);
        if ( member != null ) {
            return "no";
        } else {
            return phoneNumber;
        }
    }

    //////////////////////로그인//////////////////////
    //로그인
    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        //아이디를 넘겨받아 조회된 값을 받아옴
        Member member = memberRepository.findByEmailId(emailId);
        //조회된 값 없는 경우
        if(member == null){
            throw new UsernameNotFoundException(emailId);
        }
        //조회됐을 경우
        return User.builder()
                .username(member.getEmailId())
                .password(member.getPwd())
                .roles(member.getRoleName())
                .build();
    }

    //////////////////////ID찾기//////////////////////
    //ID찾기
    public String findIdSearch(Member.rqFindId rqFindId){
        Member member = rqFindId.toEntity();
        String findEmailId = memberRepository.findEmailId(member.getName(), member.getPhoneNumber());
        if ( findEmailId == null ) {
            return "no";
        } else {
            return findEmailId;
        }
    }

    //////////////////////PWD찾기(재설정)//////////////////////
    //비밀번호 재설정 인증메일
    public String pwdEmailCheck(String emailId){
        Member member = memberRepository.findByEmailId(emailId);
        if( member == null ) {
            return "no";
        } else {
            //MailKeyDTO불러와서 사용
            String mailKey = new MailKeyDTO().getKey(7, false);

            //Mail Server 설정
            String charSet = "UTF-8"; // 사용할 언어셋
            String hostSMTP = "smtp.naver.com"; // 사용할 SMTP
            String hostSMTPid = ""; // 사용할 SMTP에 해당하는 ID - 이메일 형식
            String hostSMTPpwd = ""; // 사용할 ID에 해당하는 PWD

            // 가장 중요한 TLS설정 - 이것이 없으면 신뢰성 에러가 나온다
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            // 보내는 사람 E-Mail, 제목, 내용
            String fromEmail = ""; // 보내는 사람 email - - hostSMTPid와 동일하게 작성
            String fromName = "관리자"; // 보내는 사람 이름
            String subject = "[Study with me] 이메일 인증번호 발송 안내입니다."; // 제목

            // 받는 사람 E-Mail 주소
            String mail = emailId; // 받는 사람 email

            try {
                HtmlEmail email = new HtmlEmail(); // Email 생성
                email.setDebug(true);
                email.setCharset(charSet); // 언어셋 사용
                email.setSSL(true);
                email.setHostName(hostSMTP); // SMTP 사용
                email.setSmtpPort(587);	// SMTP 포트 번호 입력

                email.setAuthentication(hostSMTPid, hostSMTPpwd); // 메일 ID, PWD
                email.setTLS(true);
                email.addTo(mail); // 받는 사람
                email.setFrom(fromEmail, fromName, charSet); // 보내는 사람
                email.setSubject(subject); // 제목
                email.setHtmlMsg(
                        "<p>" + "[메일 인증 안내입니다.]" + "</p>" +
                                "<p>" + "Study with me를 사용해 주셔서 감사드립니다." + "</p>" +
                                "<p>" + "아래 인증 코드를 '인증번호'란에 입력해 주세요." + "</p>" +
                                "<p>" + mailKey + "</p>"); // 본문 내용
                email.send(); // 메일 보내기
                // 메일 보내기가 성공하면 메일로 보낸 랜덤키를 콜백 메소드에도 전달
                return mailKey;
            } catch (Exception e) {
                System.out.println(e);
                // 메일 보내기가 실패하면 "no"를 콜백 메소드에 전달
                return "no";
            }
        }
    }

    //PWD 재설정을 위한 정보확인
    public String findPwdSearch(Member.rqFindPwd rqFindPwd){
        Member member = rqFindPwd.toEntity();
        String findByFindPwd = memberRepository.findPwd(member.getEmailId(), member.getName(), member.getPhoneNumber());
        if ( findByFindPwd == null ) {
            return "no";
        } else {
            return "yes";
        }
    }

    public void resetPwd(Member.rqResetPwd rqResetPwd, PasswordEncoder passwordEncoder){
        Member member = rqResetPwd.toEntity(passwordEncoder);
        memberRepository.findChangePwd(member.getEmailId(), member.getPwd());
    }
}
