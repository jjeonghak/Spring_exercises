import jpql.Address;
import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class AppMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpql");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team a = new Team("A", null);
            em.persist(a);
            Team b = new Team("B", null);
            em.persist(b);

            Member member = new Member();
            member.setName("member1");
            member.setAge(26);
            member.setTeam(a);
            em.persist(member);

            Member member1 = new Member();
            member1.setName("member2");
            member1.setAge(20);
            member1.setTeam(b);
            em.persist(member1);

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m from Member m join fetch m.team", Member.class)
                    .getResultList();

            for (Member member2 : result) {
                System.out.println("member2 = " + member2.getName() + " " + member2.getTeam().getName());
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
