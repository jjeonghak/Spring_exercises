package spring.servlet.web.frontcontroller.v5;

import spring.servlet.web.frontcontroller.ModelView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MyHandlerAdapter {

    //어댑터가 해당 컨트롤러를 처리할 수 있는지 판단
    boolean supports(Object handler);

    //어댄터는 실제 컨트롤러를 호춣하고 ModelView 반환
    //컨트롤러가 반환하지 못하면 어댑터가 생성해서 반환
    ModelView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

}
