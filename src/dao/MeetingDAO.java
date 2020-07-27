package dao;

import entity.Attendance;
import entity.Meeting;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MeetingDAO {
    Session session=null;
    
    //Lấy id, tên, thời gian, miêu tả ngắn, vị trí của tất cả hội nghị
    public List<Object[]> getAllMeeting()
    {
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="Select e.meetingId, e.name, e.time, e.shortDescription, e.location From "
                +Meeting.class.getName()+" e order by e.time DESC";
        Query query=session.createQuery(hql);
        List<Object[]> meetings=query.list();
        session.close();
        
        return meetings;
    }
    
    //Lấy miêu tả tả chi tiết và link hình của 1 hội nghị cụ thể nào đó
    public Object[] getMeetingDetail(int meetingId)
    {
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="Select e.location, e.longDescription, e.avatar From "
                +Meeting.class.getName()+" e where e.meetingId=:meetingId";
        Query query=session.createQuery(hql);
        query.setParameter("meetingId", meetingId);
        List<Object[]> meetings=query.list();
        session.close();
        return meetings.get(0);
    }
    
    //Lấy danh sách hội nghị của tôi
    public List<Object[]> getMyConferences(int userId)
    {
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction(); 
        String hql="Select m.meetingId, m.name, m.time, m.shortDescription, m.location, a.status From "
                +Meeting.class.getName()+" m, "+Attendance.class.getName()+" a Where a.meeting.meetingId=m.meetingId and a.user.userId=:userId order by m.name DESC";
        Query query=session.createQuery(hql);
        query.setParameter("userId", userId);
        List<Object[]> list = query.list();
        session.close();
        return list;
    }
    
    //Tạo hội nghị mới
    public void createConference(Meeting meeting){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        session.save(meeting);
        session.getTransaction().commit();
        session.close();
    }
    
    //Cập nhật hội nghị
    public void updateConference(Meeting meeting){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        session.update(meeting);
        session.getTransaction().commit();
        session.close();
    }
    
    //Lấy hội nghị dựa vào Id
    public Meeting getMeetingByID(int meetingId)
    {
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="From "+Meeting.class.getName()+" e Where e.meetingId=:meetingId";
        Query query=session.createQuery(hql);
        query.setParameter("meetingId", meetingId);
        List<Meeting> list=query.list();
        session.close();
        return list.get(0);
    }
}
