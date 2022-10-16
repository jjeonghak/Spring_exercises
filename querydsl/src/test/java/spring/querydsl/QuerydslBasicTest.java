package spring.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import spring.querydsl.dto.MemberDto;
import spring.querydsl.dto.QMemberDto;
import spring.querydsl.dto.UserDto;
import spring.querydsl.entity.Member;
import spring.querydsl.entity.QMember;
import spring.querydsl.entity.QTeam;
import spring.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @PersistenceContext
    EntityManager em;

    @PersistenceUnit
    EntityManagerFactory emf;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

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
    @DisplayName(value = "search member1 by JPQL")
    public void startJpql() {
        Member findByJpql = em.createQuery("select m from Member m " +
                        "where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        Assertions.assertThat(findByJpql.getUsername()).isEqualTo("member1");
    }

    @Test
    @DisplayName(value = "search member1 by QueryDSL")
    public void startQuerydsl() {
        QMember m = QMember.member;
        Member findMember = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1"))
                .fetchOne();

        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search() {
        QMember m = QMember.member;
        Member findMember = queryFactory
                .selectFrom(m)
                .where(m.username.eq("member1"),
                        m.age.eq(10)
                )
                .fetchOne();
        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
        Assertions.assertThat(findMember.getAge()).isEqualTo(10);
    }

    @Test
    public void resultFetch() {
        QMember m = QMember.member;
        QueryResults<Member> results = queryFactory
                .selectFrom(m)
                .fetchResults();
        System.out.println("results.getTotal() = " + results.getTotal());
        System.out.println("results.getResults() = " + results.getResults());
    }

    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        QMember m = QMember.member;

        List<Member> result = queryFactory
                .selectFrom(m)
                .where(m.age.goe(30))
                .orderBy(m.age.desc(), m.username.asc().nullsLast())
                .fetch();
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void paging1() {
        QMember m = QMember.member;

        List<Member> result = queryFactory
                .selectFrom(m)
                .orderBy(m.username.desc())
                .offset(1)
                .limit(2)
                .fetch();
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void aggregation() {
        QMember m = QMember.member;

        List<Tuple> result = queryFactory
                .select(
                        m.count(),
                        m.age.sum(),
                        m.age.avg(),
                        m.age.max(),
                        m.age.min()
                )
                .from(m)
                .fetch();
        System.out.println("tuple = " + result.get(0).get(m.count()));
        System.out.println("tuple = " + result.get(0).get(m.age.sum()));
        System.out.println("tuple = " + result.get(0).get(m.age.avg()));
        System.out.println("tuple = " + result.get(0).get(m.age.max()));
        System.out.println("tuple = " + result.get(0).get(m.age.min()));
    }

    @Test
    public void group() {
        QTeam t = QTeam.team;
        QMember m = QMember.member;
        List<Tuple> result = queryFactory
                .select(t.name, m.age.avg())
                .from(m)
                .join(m.team, t)
                .groupBy(t.name)
                .having(t.name.in("teamA", "teamB"))
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("t.name = " + tuple.get(t.name));
            System.out.println("m.age.avg() = " + tuple.get(m.age.avg()));
        }
    }

    @Test
    public void join() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<Member> result = queryFactory
                .selectFrom(m)
                .join(m.team, t)
                .where(t.name.eq("teamA"))
                .fetch();
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void theta_join() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<Member> result = queryFactory
                .select(m)
                .from(m, t)
                .where(m.username.eq(t.name))
                .fetch();
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void join_on_filtering() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<Tuple> result = queryFactory
                .select(m, t)
                .from(m)
                .leftJoin(m.team, t)
                .on(t.name.eq("teamA"))
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void join_on_no_relation() {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<Tuple> result = queryFactory
                .select(m, t)
                .from(m)
                .leftJoin(t).on(m.username.eq(t.name))
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void NoFetchJoin() {
        em.flush();
        em.clear();

        QMember m = QMember.member;
        Member findMember = queryFactory
                .selectFrom(m)
                .where(m.username.eq("member1"))
                .fetchOne();
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        System.out.println("loaded = " + loaded);
    }

    @Test
    public void fetchJoin() {
        em.flush();
        em.clear();

        QMember m = QMember.member;
        QTeam t = QTeam.team;
        List<Member> result = queryFactory
                .selectFrom(m)
                .join(m.team, t)
                .fetchJoin()
                .where(m.username.eq("member1"))
                .fetch();
    }

    @Test
    public void subQuery() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        QMember mSub = new QMember("mSub");
        List<Member> result = queryFactory
                .selectFrom(m)
                .where(m.age.goe(
                        JPAExpressions
                                .select(mSub.age.avg())
                                .from(mSub)
                ))
                .fetch();
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void subQueryIn() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        QMember mSub = new QMember("mSub");
        List<Member> result = queryFactory
                .selectFrom(m)
                .where(m.age.in(
                        JPAExpressions
                                .select(mSub.age)
                                .from(mSub)
                                .where(mSub.age.goe(20))
                ))
                .fetch();
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void selectSubQuery() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;
        QMember mSub = new QMember("mSub");

        List<Tuple> result = queryFactory
                .select(m.username,
                        JPAExpressions
                                .select(mSub.age.avg())
                                .from(mSub)
                )
                .from(m)
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void basicCase() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<String> result = queryFactory
                .select(m.age
                        .when(10).then("ten")
                        .when(20).then("twenty")
                        .otherwise("etc")
                )
                .from(m)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void complexCase() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(m.age.between(0, 19)).then("under twenty")
                        .when(m.age.between(20, 30)).then("between twenty and thirty")
                        .otherwise("over thirty")
                )
                .from(m)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void constant() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        //{username}_{age}
        List<String> result = queryFactory
                .select(m.username.concat("_").concat(m.age.stringValue()))
                .from(m)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void tupleProjection() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<Tuple> result = queryFactory
                .select(m.username, m.age)
                .from(m)
                .fetch();
        System.out.println(result.get(0).get(m.username));
        System.out.println(result.get(0).get(m.age));
    }

    @Test
    public void querydslDtoBySetter() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class, m.username, m.age))
                .from(m)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void querydslDtoByField() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class, m.username, m.age))
                .from(m)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void querydslDtoByConstructor() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class, m.username, m.age))
                .from(m)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void querydslUserDto() {
        QMember m = QMember.member;
        QMember mSub = new QMember("mSub");
        QTeam t = QTeam.team;

        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        m.username.as("name"),
                        ExpressionUtils.as(JPAExpressions
                                .select(mSub.age.max())
                                .from(mSub), "age")
                ))
                .from(m)
                .fetch();
        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    @Test
    public void findDtoByQueryProjection() {
        QMember m = QMember.member;
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(m.username, m.age))
                .from(m)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMemberByBooleanBuilder(usernameParam, ageParam);
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    private List<Member> searchMemberByBooleanBuilder(String usernameParam, Integer ageParam) {
        QMember m = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();
        if (usernameParam != null) {
            builder.and(m.username.eq(usernameParam));
        }
        if (ageParam != null) {
            builder.and(m.age.eq(ageParam));
        }
        return queryFactory
                .selectFrom(m)
                .where(builder)
                .fetch();
    }

    @Test
    public void dynamicQuery_WhereParam() {
        String usernameParam = "member1";
        Integer ageParam = null;

        List<Member> result = searchMemberByWhereParam(usernameParam, ageParam);
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    private List<Member> searchMemberByWhereParam(String usernameParam, Integer ageParam) {
        QMember m = QMember.member;
        return queryFactory
                .selectFrom(m)
                .where(usernameEq(usernameParam), ageEq(ageParam))
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameParam) {
        QMember m = QMember.member;
        return usernameParam != null ? m.username.eq(usernameParam) : null;
    }

    private BooleanExpression ageEq(Integer ageParam) {
        QMember m = QMember.member;
        return ageParam != null ? m.age.eq(ageParam) : null;
    }

    private BooleanExpression allEq(String usernameParam, Integer ageParam) {
        return usernameEq(usernameParam).and(ageEq(ageParam));
    }

    @Test
    //@Rollback(value = false)
    public void bulkUpdate() {
        QMember m = QMember.member;
        long count = queryFactory
                .update(m)
                .set(m.username, m.username.concat("_junior"))
                .where(m.age.lt(28))
                .execute();
    }

    @Test
    public void sqlFunction() {
        QMember m = QMember.member;
        List<String> result = queryFactory
                .select(Expressions.stringTemplate(
                        "function('replace', {0}, {1}, {2})",
                        m.username, "member", "M"))
                .from(m)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }

    }
}
