package com.study.soju.repository;

import com.study.soju.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository <Member, Object> {
    Member findByEmailId(String emailId);
    Member findByNickname(String nickname);
    Member findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT * FROM Member m WHERE m.name = :name AND m.phoneNumber = :phoneNumber", nativeQuery = true)
    Member findEmailId(@Param("name") String name, @Param("phoneNumber") String phoneNumber);

    @Query("SELECT m.emailId FROM Member m WHERE m.emailId = :emailId AND m.phoneNumber = :phoneNumber AND  m.name = :name")
    String findPwd(@Param("emailId") String emailId, @Param("name") String name, @Param("phoneNumber") String phoneNumber);

    @Query("UPDATE Member m SET m.pwd = :pwd WHERE m.emailId = :emailId")
    @Modifying // INSERT / UPDATE / DELETE 를 사용할 때 필요한 어노테이션
    @Transactional // UPDATE / DELETE 를 사용할 때 필요한 어노테이션
    int findChangePwd(@Param("emailId") String emailId, @Param("pwd") String pwd);
}
