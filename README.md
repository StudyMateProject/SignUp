# 로그인 및 회원가입 ( SignUp & Login )

### 📍 로그인 및 회원가입시 필요한 주요 기능 (CheckList)

#### 📌 회원가입
##### ✔ 아이디는 이메일형식으로 > 이메일 인증번호 전송 & 인증번호 확인 
##### ✔ PW는 형식을 지키도록 (영문, 숫자, 특수기호(~!@#$%^&*()_+)를 포함하여 5자리 이상으로 구성) 
##### ✔ 닉네임 10자 이하 (20Byte로 설정하여 영문만 작성 시, 20글자까지 가능)                  
##### ✔ 핸드폰 번호 입력 후 본인인증 (본인인증 앱API을 활용 목표, 문자메세지 인증 - Plan B)
##### ✔ 주소 API + 상세 주소 입력 
##### ✔ 전체적인 유효성 검사

#### 📌 로그인
##### ✔ 네이버 / 카카오 로그인 API
####   

### 🧩 01/24 : DB Table 생성 및 로그인 및 회원가입 폼 제작

#### 📕 Member Table
	CREATE TABLE Member (
	################ 회원가입 전 입력 ################
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
      
		################ 회원가입 후 입력 ################
		profileImage VARCHAR(100), #프로필 사진
		selfIntro VARCHAR(255), #자기소개
		achievement VARCHAR(100) #칭호
	);
##### ✏ 회원가입을 진행하면, DB에 정보가 저장되고 저장된 정보를 바탕으로 로그인 기능 (아이디 존재 여부, 비밀번호 일치 확인) 구현 이때, 비밀번호는 Spring Framework중 하나인 PasswordEncoder를 이용하여 Hash값으로 암호화하여 DB에 저장하도록 한다. 또한, 로그인시 입력하면 이를 같은 방식으로 암호화하여 비교해 일치/불일치를 판별한다.

##### ✏ Spring Security로 회원가입 시 권한을 부여하고(USER or ADIM / 회원가입시 일반 이용자는 모두 USER 권한 부여), 접속이 가능한 경로를 지정해준다. 로그인에 대한 기능도 response 기능을 필요로 하지 않고 Security에서 처리해준다. 또한, 개인의 세션을 관리해주며, 자동로그인 등의 설정도 가능하다.
###

### 🧩 01/25 : 회원가입시 아이디 중복체크 & 이메일 인증 번호 전송

#### 📎이메일 인증 번호 전송
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
##### ✏ ID는 이메일 형식을 사용하기에 '인증번호 전송' 버튼 클릭시 중복확인을 하고, 중복되지 않았다면 SMTP를 활용하여 가입자의 이메일로 인증번호 전송

#### 📎닉네임 중복확인 JS (emailId 중복, 인증번호 확인도 같은 방식으로 진행)	
	function checkNickname(f) {
            let nickname = f.nickname.value; //입력한 닉네임 가져오기
            console.log(nickname);

            let url = "/joinform/nicknamecheck";
            let param = "nickname=" + nickname;
            sendRequest(url, param, resultCheckNickname, "POST");
        }
	
	function resultCheckNickname() {
	   if ( xhr.readyState == 4 && xhr.status == 200 ) {
		let data = xhr.responseText;  // nickname check 
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

##### ✏ 입력한 값 : nickname 을 사용 가능하다면 data값(=true) 이미 있는 닉네임 값이라면 false를 대입
###

### 🧩 01/26 : 비밀번호 & 닉네임 형식 제한, 주소 API

#### 📎 비밀번호 형식 제한 JS (영문, 숫자, 특수기호(~!@#$%^&*()_+)를 포함하여 5자리 이상으로 구성)

	function pwdCheck(f) {
		let pwd = f.pwd.value;
		let pwdText1 = document.getElementById("pwdText1");

		let pattern1 = /[0-9]/; // 숫자 입력
		let pattern2 = /[a-zA-Z]/; // 영어 소문자, 대문자 입력
		let pattern3 = /[~!@#$%^&*()_+]/; // 특수기호 입력

		if ( !pattern1.test(pwd) || !pattern2.test(pwd) || !pattern3.test(pwd) || pwd.length < 5 ) {
			pwdText1.innerHTML = "영문, 숫자, 특수기호(~!@#$%^&*()_+)를 포함하여 5자리 이상으로 구성하여야 합니다";
		} else {
			pwdText1.innerHTML = "";
		}
	}

	function pwdCheck2(f) {
		let pwd = f.pwd.value;
		let pwd2 = f.pwd2.value;
		let pwdText2 = document.getElementById("pwdText2");

		if ( pwd2 != pwd ) {
			pwdText2.innerHTML = "비밀번호가 일치하지 않습니다";
		} else {
			pwdText2.innerHTML = "";
		}
	}	
##### ✏ oninput="pwdCheck(this.form)"을 사용하여 실시간으로 형식을 지키는지 확인할 수 있도록 함. 닉네임 글자수 제한은 기존의 '중복확인' 버튼 클릭시 사용되었던 JS(01/25)에 추가

#### 📎 주소 API
	<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
##### ✏ 다음(카카오) 주소 API사용, 주소 입력 Textbox 혹은 '주소찾기' 버튼 클릭 시 주소 검색할 수 있는 팝업창 생성, 상세주소 입력시 DTO에서 DB에 '주소 + 상세주소'로 저장되도록 하게함
