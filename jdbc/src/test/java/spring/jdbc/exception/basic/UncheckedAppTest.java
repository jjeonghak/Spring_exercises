package spring.jdbc.exception.basic;

import java.net.ConnectException;
import java.sql.SQLException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UncheckedAppTest {

	@Test
	void unchecked() {
		Controller controller = new Controller();
		Assertions.assertThatThrownBy(() -> controller.request())
			.isInstanceOf(Exception.class);
	}

	static class Controller {
		Service service = new Service();

		public void request() {
			service.logic();
		}
	}

	static class Service {
		Repository repository = new Repository();
		NetworkClient networkClient = new NetworkClient();

		public void logic() {
			repository.call();
			networkClient.call();
		}
	}

	static class NetworkClient {
		public void call() {
			try {
				connectNetwork();
			} catch (ConnectException e) {
				throw new RuntimeConnectException(e);
			}
		}

		private void connectNetwork() throws ConnectException {
			throw new ConnectException("연결 실패");
		}
	}

	static class Repository {
		public void call() {
			try {
				runSQL();
			} catch (SQLException e) {
				throw new RuntimeSQLException(e);
			}
		}

		private void runSQL() throws SQLException {
			throw new SQLException("데이터베이스 오류");
		}
	}

	static class RuntimeConnectException extends RuntimeException {
		public RuntimeConnectException(Throwable cause) {
			super(cause);
		}
	}

	static class RuntimeSQLException extends RuntimeException {
		public RuntimeSQLException(Throwable cause) {
			super(cause);
		}
	}
}
