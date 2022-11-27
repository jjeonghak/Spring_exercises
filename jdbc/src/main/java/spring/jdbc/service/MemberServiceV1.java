package spring.jdbc.service;

import java.sql.SQLException;

import lombok.RequiredArgsConstructor;
import spring.jdbc.domain.Member;
import spring.jdbc.repository.MemberRepositoryV1;

@RequiredArgsConstructor
public class MemberServiceV1 {

	private final MemberRepositoryV1 memberRepository;

	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		Member fromMember = memberRepository.findById(fromId);
		Member toMember = memberRepository.findById(toId);

		memberRepository.update(fromId, fromMember.getMoney() - money);
		if (toMember.getMemberId().equals("ex")) {
			throw new IllegalStateException("이체 중 예외 발생");
		}
		memberRepository.update(toId, toMember.getMoney() + money);
	}
}
