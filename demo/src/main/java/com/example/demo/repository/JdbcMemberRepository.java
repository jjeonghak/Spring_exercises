package com.example.demo.repository;

import com.example.demo.domain.Member;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    //DB에 연결하려면 DataSource 필요, javax.sql.DataSource
    //DataSource는 나중에 spring-boot에게 주입 받아야함
    private final DataSource dataSource;

    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    @Override
    public Member save(Member member) {
        //파라미터 바인딩을 위한 ?
        String sql = "insert into member(name) values(?)";

        //connetion과 close는 DataSourceUtils를 통해서만 가져와야 동일한 커넥션으로 작업가능
        /*
        private void getConnection() {
            return DataSourceUtils.getConnection(dataSource);
        }

        private void close(Connection conn) theows SQLException {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
         */

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //parameterIndex 1 : 첫번째 ?와 매칭
            pstmt.setString(1, member.getName);

            //실제 쿼리가 이때 DB로 전달, 업데이트
            pstmt.executeUpdate();

            //RETURN_GENERATED_KEYS와 매칭되어 반환
            rs = pstmt.getGeneratedKeys();

            //next의 어떠한 값이 들어있다면 꺼내어 사용
            if (rs.next()) {
                member.setId(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            //외부 연결이므로 사용 후 릴리즈 해제
            close(conn, pstmt, rs);
        }
        return null;
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            //조회
            rs = pstmt.excuteQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Member> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.excuteQuery();

            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getName("name"));
                members.add(member);
            }
            return members;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
        return null;
    }
}
