package spring.querydsl.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import spring.querydsl.dto.MemberSearchCondition;
import spring.querydsl.dto.MemberTeamDto;
import spring.querydsl.dto.QMemberTeamDto;
import spring.querydsl.entity.Member;
import spring.querydsl.entity.QMember;
import spring.querydsl.entity.QTeam;

import javax.persistence.EntityManager;
import java.util.List;

//extends QuerydslRepositorySupport
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final QMember m;
    private final QTeam t;

//    QuerydslRepositorySupport 사용
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        this.m = QMember.member;
        this.t = QTeam.team;
    }

//    public MemberRepositoryImpl() {
//        super(Member.class);
//    }

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {

//        return from(m)
//                .leftJoin(m.team, t)
//                .where(
//                        usernameEq(condition.getUsername()),
//                        teamNameEq(condition.getTeamName()),
//                        ageGoe(condition.getAgeGoe()),
//                        ageLoe(condition.getAgeLoe())
//                )
//                .select(new QMemberTeamDto(
//                        m.id.as("memberId"), m.username, m.age,
//                        t.id.as("teamId"), t.name.as("teamName")
//                ))
//                .fetch();

        return queryFactory
                .select(new QMemberTeamDto(
                        m.id.as("memberId"), m.username, m.age,
                        t.id.as("teamId"), t.name.as("teamName")
                ))
                .from(m)
                .leftJoin(m.team, t)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? m.username.eq(username) : null;
    }

    private Predicate teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? t.name.eq(teamName) : null;
    }

    private Predicate ageGoe(Integer ageGoe) {
        return ageGoe != null ? m.age.goe(ageGoe) : null;
    }

    private Predicate ageLoe(Integer ageLoe) {
        return ageLoe != null ? m.age.loe(ageLoe) : null;
    }

    @Override
    public Page<MemberTeamDto> searchPage(MemberSearchCondition condition, Pageable pageable) {

//        JPQLQuery<MemberTeamDto> jpaQuery = from(m)
//                .leftJoin(m.team, t)
//                .where(
//                        usernameEq(condition.getUsername()),
//                        teamNameEq(condition.getTeamName()),
//                        ageGoe(condition.getAgeGoe()),
//                        ageLoe(condition.getAgeLoe())
//                )
//                .select(new QMemberTeamDto(
//                        m.id.as("memberId"), m.username, m.age,
//                        t.id.as("teamId"), t.name.as("teamName")
//                ))
//        JPQLQuery<MemberTeamDto> query = getQuerydsl().applyPagination(pageable, jpaQuery);
//        query.fetch();


        JPAQuery<Long> countQuery = queryFactory
                .select(m.count())
                .from(m)
                .leftJoin(m.team, t)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                );

        List<MemberTeamDto> content = queryFactory
                .select(new QMemberTeamDto(
                        m.id.as("memberId"), m.username, m.age,
                        t.id.as("teamId"), t.name.as("teamName")
                ))
                .from(m)
                .leftJoin(m.team, t)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //return new PageImpl<>(content, pageable, countQuery);
        return PageableExecutionUtils
                .getPage(content, pageable, () -> countQuery.fetchOne());
    }

}
