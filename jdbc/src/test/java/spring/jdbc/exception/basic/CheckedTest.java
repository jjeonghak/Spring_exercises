package spring.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckedTest {

	@Test
	void checked_catch() {
		Service service = new Service();
		service.callCatch();
	}

	@Test
	void checked_throw() {
		Service service = new Service();
		Assertions.assertThatThrownBy(() -> service.callThrow())
			.isInstanceOf(MyCheckException.class);
	}

	/**
	 * Exception을 상속받은 예외는 체크 예외
	 */
	static class MyCheckException extends Exception {
		public MyCheckException(String message) {
			super(message);
		}
	}

	static class Service {
		Repository repository = new Repository();

		/**
		 * 예외를 잡어서 처리
		 */
		public void callCatch() {
			try {
				repository.call();
			} catch (MyCheckException e) {
				log.info("예외 처리, message={}", e.getMessage(), e);
			}
		}

		/**
		 * 예외를 밖으로 던짐
		 * @throws MyCheckException
		 */
		public void callThrow() throws MyCheckException {
			repository.call();
		}
	}

	static class Repository {
		public void call() throws MyCheckException {
			throw new MyCheckException("ex");
		}
	}
}
