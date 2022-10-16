package spring.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("--- ALl Param - start ---");
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> System.out.println(paramName + ": " + request.getParameter(paramName)));
        System.out.println("--- All Param - end ---");
        System.out.println();

        System.out.println("--- Single Param - start ---");
        String username = request.getParameter("username");
        System.out.println("username: " + username);
        String age = request.getParameter("age");
        System.out.println("age: " + age);
        System.out.println("--- Single Param - end ---");
        System.out.println();

        System.out.println("--- Same Params - start ---");
        String[] usernames = request.getParameterValues("username");
        for (String s : usernames) {
            System.out.println("username: " + s);
        }
        System.out.println("--- Same Params - end ---");
        System.out.println();

        response.getWriter().write("ok");
    }
}
