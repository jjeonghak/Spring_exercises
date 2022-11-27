package spring.jdbc.service;

import java.sql.SQLException;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import spring.jdbc.domain.Member;
import spring.jdbc.repository.MemberRepositoryV3;

/*
 * 트랜잭션 - @Transactional AOP
 */
@Slf4j
public class MemberServiceV3_3 {

	//private final TransactionTemplate txTemplate;
	private final MemberRepositoryV3 memberRepository;

	public MemberServiceV3_3(MemberRepositoryV3 memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Transactional
	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		bizLogic(fromId, toId, money);
	}

	private void bizLogic(String fromId, String toId, int money) throws SQLException {
		Member fromMember = memberRepository.findById(fromId);
		Member toMember = memberRepository.findById(toId);

		memberRepository.update(fromId, fromMember.getMoney() - money);
		validation(toMember);
		memberRepository.update(toId, toMember.getMoney() + money);
	}

	private void validation(Member toMember) {
		if (toMember.getMemberId().equals("ex")) {
			throw new IllegalStateException("이체 중 예외 발생");
		}
	}
}
