<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="spring.servlet.domain.member.Member" %>
<%@ page import="spring.servlet.domain.member.MemberRepository" %>
<%@ page import="java.util.List" %>
<%
    MemberRepository memberRepository = MemberRepository.getInstance();

    List<Member> members = memberRepository.findAll();
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<a href="/index.html">main</a>
<table>
    <thead>
        <th>id</th>
        <th>username</th>
        <th>age</th>
    </thead>
    <tbody>
    <%
        for (Member member : members) {
            out.write("     <tr>\n");
            out.write("         <td>" + member.getId() + "</td>\n");
            out.write("         <td>" + member.getUsername() + "</td>\n");
            out.write("         <td>" + member.getAge() + "</td>\n");
            out.write("     </tr>\n");
        }
    %>
    </tbody>
</table>
</body>
</html>