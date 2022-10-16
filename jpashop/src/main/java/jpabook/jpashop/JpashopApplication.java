package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {
	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	//지연로딩에 의한 프록시 객체(ByteBuddyInterceptor) 조회 오류 해결
	//엔티티를 직접 전달하는 방식이므로 지양
	@Bean
	Hibernate5Module hibernate5Module() {
		return new Hibernate5Module();
	}
}
