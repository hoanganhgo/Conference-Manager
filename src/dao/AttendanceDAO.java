package dao;

import entity.Attendance;
import entity.Meeting;
import entity.User;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AttendanceDAO {
    Session session=null;
    
    public Integer countParticipants(Integer meetingID)
    {
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="Select count(*) From "+Attendance.class.getName()+" e where e.meeting.meetingId=:meetingID";
        Query query=session.createQuery(hql);
        query.setParameter("meetingID", meetingID);
        Object list=query.uniqueResult();
        return Integer.valueOf(list.toString());
    }
    
    public void attendConference(int meetingId, int userId)
    {
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        
        Attendance attendance=new Attendance(meetingId, userId);
        session.save(attendance);
        session.getTransaction().commit();
        session.close();
    }
    
    public int getStatus(int meetingId, int userId){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        
        String hql="Select e.status From "+Attendance.class.getName()+" e Where meeting.meetingId=:meetingId and user.userId=:userId";
        Query query=session.createQuery(hql);
        query.setParameter("meetingId", meetingId);
        query.setParameter("userId", userId);
        
        List<Object> result=query.list();
        session.close();
        
        if (result.isEmpty()){
            return -1;  //Chưa đăng ký tham gia
        }else{
            return Byte.toUnsignedInt((byte)result.get(0));
        }
    }
}
