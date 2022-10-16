package spring.servlet.web.springmvc.old;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 1. 핸들러 매핑으로 핸들러 조회
 *  BeanNameUrlHandlerMapping 실행 및 성공
 *  MyHttpRequestHandler 반환
 *
 * 2. 핸들러 어댑터 조회
 *  HandlerAdapter.supports() 호출
 *  HttpRequestHandlerAdapter 통해서 HttpRequestHandler 인터페이스 지원
 *
 * 3. 핸들러 어댑터 실행
 *  DispatcherServlet에 의해 조회된 핸들러 어댑터에 핸들러 정보 넘겨줌
 *  HttpRequestHandlerAdapter 내부에서 MyHttpRequestHandler 실행 및 결과 반환
 */
@Component("/springmvc/request-handler")
public class MyHttpRequestHandler implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MyHttpRequestHandler.handleRequest");
    }
}
