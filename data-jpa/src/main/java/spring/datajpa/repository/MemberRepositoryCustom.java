package spring.datajpa.repository;

import spring.datajpa.enitity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
