# SignUp & Login

### 로그인 및 회원가입 구현 (01/26)

##### 기존에 로그인 & 회원가입 폼은 제작해놨으며 DB와 연동을 하여 회원가입을 하면 Member 테이블에 저장되고, 로그인시에 이 테이블에 저장된 정보와 비교하여 로그인을 할 수 있도록 만들었다.

#### Member Table
	CREATE TABLE Member (
	##################회원가입 전 입력##################
		emailId VARCHAR(50) PRIMARY KEY, #이메일 형식 아이디
		pwd VARCHAR(255) NOT NULL, #비밀번호
        name VARCHAR(10) NOT NULL, #이름
        nickname VARCHAR(20) UNIQUE NOT NULL, #닉네임
        birthday DATE NOT NULL, #생년월일
        gender VARCHAR(1) NOT NULL, #성별
        phoneNumber VARCHAR(15) UNIQUE NOT NULL, #핸드폰 번호
        address VARCHAR(100) NOT NULL, #주소
        studyType VARCHAR(10) NOT NULL, #관심있는 분야
        platform VARCHAR(10) NOT NULL, #플랫폼
        roleName VARCHAR(100) NOT NULL, #Spring Security 권한   
      
        ##################회원가입 후 입력##################
        profileImage VARCHAR(100), #프로필 사진
        selfIntro VARCHAR(255), #자기소개
        achievement VARCHAR(100) #칭호
    );

#### 01/25 회원가입시 아이디 중복체크 & 이메일 인증 번호 전송

##### 이메일 인증 번호 전송
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
        email.setSmtpPort(587);   // SMTP 포트 번호 입력
      
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
         
##### 중복확인은 hEmailCheck도 hidden으로 지정해주어 비교 후 결과값을 저장 > nickname도 같은 방식으로 중복체크 진행
     function resultCheckNickname() {
        if ( xhr.readyState == 4 && xhr.status == 200 ) {
           let data = xhr.responseText;
           let hNickname = document.getElementById("hNickname");
         
           if ( data == "no" ) {
              alert("이미 존재하는 닉네임입니다.");
              hNickname.value = "false";
              return;
           } else {
              alert("사용 가능한 닉네임입니다.");
              hNickname.value = data;
              return;
           }
        }
    }
