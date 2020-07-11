package dao;

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
        String hql="Select e.longDescription, e.avatar From "
                +Meeting.class.getName()+" e where e.meetingId=:meetingId";
        Query query=session.createQuery(hql);
        query.setParameter("meetingId", meetingId);
        List<Object[]> meetings=query.list();
        session.close();
        return meetings.get(0);
    }
}
