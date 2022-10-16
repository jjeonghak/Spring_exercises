package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

//DB는 커밋(자동커밋)을 해야 실제로 반영
//테스트 케이스에 붙은 @Transactional 어노테이션을 통해 커밋하기 전에 롤백
@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void join() {
        //given
        Member member = new Member();
        member.setName("sibal");
        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        org.assertj.core.api.Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void validName() {
        //given
        Member member1 = new Member();
        member1.setName("sibal");
        Member member2 = new Member();
        member2.setName("sibal");
        //when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        org.assertj.core.api.Assertions.assertThat(e.getMessage()).isEqualTo("중복된 이름입니다.");

        /*
        try {
            memberService.join(member2);
            fail();
        } catch (IllegalStateException e) {
            org.assertj.core.api.Assertions.assertThat(e.getMessage()).isEqualTo("중복된 이름입니다.");
        }
         */
    }

    @Test
    public void findMembers() {
    }

    @Test
    public void findOne() {
    }
}