package dao;

import entity.Attendance;
import entity.Meeting;
import entity.User;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AttendanceDAO {
    Session session=null;
    
    //Đếm số người đăng ký tham dự
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
    
    //Đếm số người đã tham dự
    public Integer countParticipanted(Integer meetingID)
    {
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="Select count(*) From "+Attendance.class.getName()+" e where e.meeting.meetingId=:meetingID and e.status=1";
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
            return (int)result.get(0);
        }
    }
    
    //Lây danh sách những người đăng ký tham gia hội nghị
    public List<Object[]> getUserJoinAttendance(int meetingId){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        
        String hql="Select e.attendanceId, u.name, u.username, u.email From "+Attendance.class.getName()+" e,"+User.class.getName()+" u WHERE e.user.userId=u.userId and e.meeting.meetingId=:meetingId and e.status=2";
        Query query=session.createQuery(hql);
        query.setParameter("meetingId", meetingId);
        List<Object[]> list=query.list();
        session.close();
        return list;
    }
    
    //Cài đặt trạng thái
    public void setStatus(int id, int status){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="Update "+Attendance.class.getName()+" e set e.status=:status Where e.attendanceId=:id";
        Query query=session.createQuery(hql);
        query.setParameter("status", status);
        query.setParameter("id", id);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    
    //Từ chối tất cả yêu cầu tham dự hội nghị còn lại
    public void rejectAll(int meetingId){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="Update "+Attendance.class.getName()+" e set e.status=0 Where e.meeting.meetingId=:meetingId";
        Query query=session.createQuery(hql);
        query.setParameter("meetingId", meetingId);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    
    //Đếm số hội nghị mà một user tham dự
    public Integer countConference(int userId){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="Select count(*) From "+Attendance.class.getName()+" e where e.user.userId=:userId and e.status=1";
        Query query=session.createQuery(hql);
        query.setParameter("userId", userId);
        Integer result=Integer.valueOf(query.uniqueResult().toString());
        session.close();
        return result;
    }
    
    //Hủy tất cả các hội nghị chưa diễn ra đã tham gia.
    public void cancelIfLocked(int userId){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="Update "+Attendance.class.getName()+" e set e.status=0 Where e.user.userId=:userId and e.meeting.meetingId IN ( Select m.meetingId From "+
                Meeting.class.getName()+" m Where m.time>:time)";
        Query query=session.createQuery(hql);
        query.setParameter("userId", userId);
        Date date=new Date();        
        query.setParameter("time", date);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
