package spring.servlet.web.springmvc.old;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 1. 핸들러 매핑으로 핸들러 조회
 *  BeanNameUrlHandlerMapping 실행 및 성공
 *  OldController 반환
 *
 * 2. 핸들러 어댑터 조회
 *  HandlerAdapter.supports() 호출
 *  SimpleControllerHandlerAdapter 통해서 Controller 인터페이스 지원
 *
 * 3. 핸들러 어댑터 실행
 *  DispatcherServlet에 의해 조회된 핸들러 어댑터에 핸들러 정보 넘겨줌
 *  SimpleControllerHandlerAdapter 내부에서 OldController 실행 및 결과 반환
 *
 * 4. ViewResolver 호출
 *  new-form이라는 뷰 이름으로 viewResolver 순서대로 호출
 *  InternalResourceViewResolver 호출
 */
@Component("/springmvc/old-controller")
public class OldController implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return new ModelAndView("new-form");
    }
}
