package spring.querydsl.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import spring.querydsl.dto.MemberSearchCondition;
import spring.querydsl.dto.MemberTeamDto;
import spring.querydsl.entity.Member;
import spring.querydsl.entity.QMember;
import spring.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    QMember m = QMember.member;

    @BeforeEach
    public void init() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void searchTest() {


        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(10);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<MemberTeamDto> result = memberRepository.search(condition);
        for (MemberTeamDto memberTeamDto : result) {
            System.out.println("memberTeamDto = " + memberTeamDto);
        }

        List<MemberTeamDto> result1 = memberRepository.search(condition);
        for (MemberTeamDto memberTeamDto : result1) {
            System.out.println("memberTeamDto = " + memberTeamDto);
        }
    }

    @Test
    public void searchPage() {
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(20);
        condition.setAgeLoe(40);
        PageRequest pageRequest = PageRequest.of(0, 2);

        Page<MemberTeamDto> result = memberRepository.searchPage(condition, pageRequest);

        Assertions.assertThat(result.getSize()).isEqualTo(2);
        Assertions.assertThat(result.getContent()).extracting("username").containsExactly("member2", "member3");

    }

    @Test
    public void querydslPredicateExecutorTest() {
        Iterable<Member> result = memberRepository.findAll(
                m.age.between(20, 40).and(m.username.in("member1", "member2", "member3")
                ));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }



}