package spring.querydsl.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.querydsl.dto.MemberSearchCondition;
import spring.querydsl.dto.MemberTeamDto;
import spring.querydsl.entity.Member;
import spring.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired EntityManager em;
    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void basicTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        Assertions.assertThat(findMember).isEqualTo(member);

        List<Member> all = memberJpaRepository.findAll();
        Assertions.assertThat(all).containsExactly(member);

        List<Member> all2 = memberJpaRepository.findByUsername("member1");
        Assertions.assertThat(all2).containsExactly(member);
    }

    @Test
    public void querydslTest() {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        List<Member> all = memberJpaRepository.findAll_Querydsl();
        Assertions.assertThat(all).containsExactly(member);

        List<Member> all2 = memberJpaRepository.findByUsername_Querydsl("member1");
        Assertions.assertThat(all2).containsExactly(member);
    }

    @Test
    public void searchTest() {
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

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(10);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);
        for (MemberTeamDto memberTeamDto : result) {
            System.out.println("memberTeamDto = " + memberTeamDto);
        }

        List<MemberTeamDto> result1 = memberJpaRepository.searchByWhere(condition);
        for (MemberTeamDto memberTeamDto : result1) {
            System.out.println("memberTeamDto = " + memberTeamDto);
        }
    }
}