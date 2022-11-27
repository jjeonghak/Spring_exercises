package spring.jdbc.service;

import javax.sql.DataSource;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;
import spring.jdbc.domain.Member;
import spring.jdbc.repository.MemberRepository;
import spring.jdbc.repository.MemberRepositoryV4_1;
import spring.jdbc.repository.MemberRepositoryV4_2;
import spring.jdbc.repository.MemberRepositoryV5;

@Slf4j
@SpringBootTest
class MemberServiceV4Test {

	public static final String MEMBER_A = "memberA";
	public static final String MEMBER_B = "memberB";
	public static final String MEMBER_EX = "ex";

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemberServiceV4 memberService;

	@TestConfiguration
	static class TestConfig {

		private final DataSource dataSource;

		public TestConfig(DataSource dataSource) {
			this.dataSource = dataSource;
		}

		@Bean
		MemberRepository memberRepository() {
			// return new MemberRepositoryV4_1(dataSource);
			// return new MemberRepositoryV4_2(dataSource);
			return new MemberRepositoryV5(dataSource);
		}

		@Bean
		MemberServiceV4 memberServiceV4() {
			return new MemberServiceV4(memberRepository());
		}
	}

	@AfterEach
	void after() {
		memberRepository.delete(MEMBER_A);
		memberRepository.delete(MEMBER_B);
		memberRepository.delete(MEMBER_EX);
	}

	@Test
	void AopCheck() {
		log.info("memberService class = {}", memberService.getClass());
		log.info("memberRepository class = {}", memberRepository.getClass());
		Assertions.assertThat(AopUtils.isAopProxy(memberService)).isTrue();
		Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
	}

	@Test
	@DisplayName("정상 이체")
	void accountTransfer() {
		Member memberA = new Member(MEMBER_A, 10000);
		Member memberB = new Member(MEMBER_B, 10000);
		memberRepository.save(memberA);
		memberRepository.save(memberB);

		memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

		Member findMemberA = memberRepository.findById(memberA.getMemberId());
		Member findMemberB = memberRepository.findById(memberB.getMemberId());
		Assertions.assertThat(findMemberA.getMoney()).isEqualTo(8000);
		Assertions.assertThat(findMemberB.getMoney()).isEqualTo(12000);
	}

	@Test
	@DisplayName("이체 중 예외 발생")
	void accountTransferEx() {
		Member memberA = new Member(MEMBER_A, 10000);
		Member memberEx = new Member(MEMBER_EX, 10000);
		memberRepository.save(memberA);
		memberRepository.save(memberEx);

		Assertions.assertThatThrownBy(() ->
			memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
			.isInstanceOf(IllegalStateException.class);

		Member findMemberA = memberRepository.findById(memberA.getMemberId());
		Member findMemberEx = memberRepository.findById(memberEx.getMemberId());
		Assertions.assertThat(findMemberA.getMoney()).isEqualTo(10000);
		Assertions.assertThat(findMemberEx.getMoney()).isEqualTo(10000);
	}
}
