package deprecated;
 
import java.util.Date;
 
import org.hibernate.Session;

import deprecated.HibernateUtil;
import deprecated.User;
 
public class Test {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
 
        session.beginTransaction();
        User user = new User();
 
        user.setUserId(1);
        user.setUsername("Mukesh");
        user.setCreatedBy("Google");
        user.setCreatedDate(new Date());
 
        session.save(user);
        session.getTransaction().commit();
 
    }
 
}