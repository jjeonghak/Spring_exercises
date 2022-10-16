package spring.datajpa.enitity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import spring.datajpa.dto.MemberDto;
import spring.datajpa.repository.MemberJpaRepository;
import spring.datajpa.repository.MemberRepository;
import spring.datajpa.repository.TeamRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext EntityManager em;
    @Autowired public MemberJpaRepository memberJpaRepository;
    @Autowired public MemberRepository memberRepository;
    @Autowired public TeamRepository teamRepository;

    @Test
    public void testEntity() {
        Team teamA = new Team("A");
        Team teamB = new Team("B");
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

        member3.changeTeam(teamA);
        org.assertj.core.api.Assertions.assertThat(teamA.getMembers().size()).isEqualTo(3);
        org.assertj.core.api.Assertions.assertThat(teamB.getMembers().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(member3.getTeam()).isEqualTo(teamA);
        org.assertj.core.api.Assertions.assertThat(teamB.getMembers().contains(member3)).isFalse();
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        org.assertj.core.api.Assertions.assertThat(findMember1).isEqualTo(member1);
        org.assertj.core.api.Assertions.assertThat(findMember2).isEqualTo(member2);

        org.assertj.core.api.Assertions.assertThat(memberJpaRepository.findAll().size()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(memberJpaRepository.count()).isEqualTo(2);
        memberJpaRepository.delete(member2);
        org.assertj.core.api.Assertions.assertThat(memberJpaRepository.count()).isEqualTo(1);
    }

    @Test
    public void dataJpaCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        org.assertj.core.api.Assertions.assertThat(findMember1).isEqualTo(member1);
        org.assertj.core.api.Assertions.assertThat(findMember2).isEqualTo(member2);

        org.assertj.core.api.Assertions.assertThat(memberRepository.findAll().size()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(memberRepository.count()).isEqualTo(2);
        memberRepository.delete(member2);
        org.assertj.core.api.Assertions.assertThat(memberRepository.count()).isEqualTo(1);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("A", 10);
        Member m2 = new Member("A", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("A", 15);

        org.assertj.core.api.Assertions.assertThat(result.size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(result.get(0).getUsername()).isEqualTo("A");
    }

    @Test
    public void findMemberDto() {
        Team teamA = new Team("A");
        Team teamB = new Team("B");
        teamRepository.save(teamA);
        teamRepository.save(teamB);


        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<MemberDto> result = memberRepository.findMemberDto();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findByNames() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 30);
        Member member4 = new Member("member4", 40);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<Member> result = memberRepository.findByUsernameIn(Arrays.asList("member1", "member4"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 30);
        Member member4 = new Member("member4", 40);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<Member> listByUsername = memberRepository.findListByUsername("member2");
        Member memberByUsername = memberRepository.findMemberByUsername("member1");
        Optional<Member> optionalByUsername = memberRepository.findOptionalByUsername("member4");

        System.out.println("listByUsername = " + listByUsername);
        System.out.println("memberByUsername = " + memberByUsername);
        System.out.println("optionalByUsername = " + optionalByUsername);
    }

    @Test
    public void paging() {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> result = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        for (Member member : result) {
            System.out.println("member = " + member);
        }
        System.out.println("totalCount = " + totalCount);
    }

    @Test
    public void bulkUpdate() {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 14));
        memberJpaRepository.save(new Member("member3", 19));
        memberJpaRepository.save(new Member("member4", 24));

        int result = memberJpaRepository.bulkAgePlus(15);
        em.flush();
        em.clear();
        List<Member> all = memberJpaRepository.findAll();
        for (Member member : all) {
            System.out.println("member = " + member);
        }
        System.out.println("result : "+ result);
    }

    @Test
    public void JpaEventBaseEntity() throws InterruptedException {
        Member member  = new Member("member1");
        memberRepository.save(member);

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush();
        em.clear();

        System.out.println("member = " + member.getCreateDate());
        System.out.println("member = " + member.getLastModifiedDate());
        System.out.println("member = " + member.getCreatedBy());
        System.out.println("member = " + member.getLastModifiedBy());
    }
}