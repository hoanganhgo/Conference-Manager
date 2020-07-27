package dao;

import entity.User;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDAO {
    Session session=null;
        
    public List<User> getAllUser()
    {
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="from "+User.class.getName();
        Query query=session.createQuery(hql);
        List<User> users=query.list();
        session.close();
        return users;
    }
    
    public boolean saveUser(User user)
    {
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        
        //Kiểm tra sự tồn tại của username
        String hql="Select e.userId From "+User.class.getName()+" e Where e.username=:username";
        Query query=session.createQuery(hql);
        query.setParameter("username", user.getUsername());
        List<Object[]> result=query.list();
        
        if (result.isEmpty()){
            session.save(user);
            session.getTransaction().commit();
            session.close();
            return true;
        }else{
            session.close();
            return false;
        }
    }
    
    public User login(String username, String password)
    {
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="Select e.userId, e.name, e.admin, e.active From "
                +User.class.getName()+" e Where e.username=:username and e.password=:password";
        Query query=session.createQuery(hql);
        query.setParameter("username", username);
        query.setParameter("password", password);

        List<Object[]> result=query.list();
        
        if (result.isEmpty()){
            session.close();
            return null;
        }else{
            session.close();
            User user=new User((int)result.get(0)[0], result.get(0)[1].toString(), (byte)result.get(0)[2], (byte)result.get(0)[3]);
            return user;
        }
    }
    
    public User getUserInfo(int userId){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        
        String hql="Select e.name, e.username, e.email From "+User.class.getName()+" e where e.userId=:userId";
        Query query=session.createQuery(hql);
        query.setParameter("userId", userId);
        List<Object[]> result=query.list();
        session.close();
        
        User user=new User(result.get(0)[0].toString(), result.get(0)[1].toString(), result.get(0)[2].toString());
        return user;        
    }
    
    public void updateName(int userId, String newName){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        
        String hql="Update "+User.class.getName()+" e set e.name=:newName where e.userId=:userId";
        Query query=session.createQuery(hql);
        query.setParameter("newName", newName);
        query.setParameter("userId", userId);
        query.executeUpdate();

        session.getTransaction().commit();
        session.close();
    }
    
    public void updateEmail(int userId, String newEmail){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        
        String hql="Update "+User.class.getName()+" e set e.email=:newEmail where e.userId=:userId";
        Query query=session.createQuery(hql);
        query.setParameter("newEmail", newEmail);
        query.setParameter("userId", userId);
        query.executeUpdate();

        session.getTransaction().commit();
        session.close();
    }
    
    public boolean updatePassword(int userId, String oldPassword, String  newPassword)
    {
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        
        String hql="Update "+User.class.getName()+" e set e.password=:newPassword where e.userId=:userId and e.password=:oldPassword";
        Query query=session.createQuery(hql);
        query.setParameter("newPassword", newPassword);
        query.setParameter("userId", userId);
        query.setParameter("oldPassword", oldPassword);
        int result = query.executeUpdate();
        
        if (result==0){
            session.close();
            return false;
        }else{
            session.getTransaction().commit();
            session.close();
            return true;
        }        
    }
    
    public void setActiveUser(int userId, int active){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="Update "+User.class.getName()+" e set e.active=:active where e.userId=:userId";
        Query query=session.createQuery(hql);
        query.setParameter("active", (byte)active);
        query.setParameter("userId", userId);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
