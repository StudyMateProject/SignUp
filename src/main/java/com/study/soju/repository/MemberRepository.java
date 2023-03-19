package com.study.soju.repository;

import com.study.soju.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional // UPDATE / DELETE 를 사용할 때 필요한 어노테이션
public interface MemberRepository extends JpaRepository <Member, Object> {
    Member findByEmailId(String emailId);
    Member findByNickname(String nickname);
    Member findByPhoneNumber(String phoneNumber);

    //ID찾기
    @Query(value = "SELECT * FROM Member m WHERE m.name = :name AND m.phoneNumber = :phoneNumber", nativeQuery = true)
    Member findEmailId(@Param("name") String name, @Param("phoneNumber") String phoneNumber);

    //PWD 찾기
    @Query(value = "SELECT * FROM Member m WHERE m.emailId = :emailId AND m.phoneNumber = :phoneNumber AND  m.name = :name", nativeQuery = true)
    Member findPwd(@Param("emailId") String emailId, @Param("name") String name, @Param("phoneNumber") String phoneNumber);

    //PWD 재설정
    @Query("UPDATE Member m SET m.pwd = :pwd WHERE m.emailId = :emailId")
    @Modifying // INSERT / UPDATE / DELETE 를 사용할 때 필요한 어노테이션
    int findChangePwd(@Param("emailId") String emailId, @Param("pwd") String pwd);

    @Query("UPDATE Member m SET m.emailId = :emailId, m.name = :name, m.phoneNumber = :phoneNumber, m.address = :address, m.detailAddress = :detailAddress, m.studyType = :studyType, m.birthday = :birthday, m.nickname = :nickname, m.gender = :gender, m.selfIntro = :selfIntro, m.profileImage = :profileImage WHERE m.emailId = :emailId")
    @Modifying // INSERT / UPDATE / DELETE 를 사용할 때 필요한 어노테이션
    int updateChangePwd(@Param("emailId") String emailId, @Param("name") String name, @Param("phoneNumber") String phoneNumber,
                        @Param("address") String address, @Param("detailAddress") String detailAddress, @Param("studyType") String studyType,
                        @Param("birthday") String birthday, @Param("nickname") String nickname, @Param("gender") String gender,
                        @Param("selfIntro") String selfIntro, @Param("profileImage") String profileImage);

    //회원정보 조회
    @Query(value = "SELECT m FROM Member m WHERE m.emailId = :emailId")
    Member findAll(@Param("emailId") String emailId);
}
