package dao;

import entity.Location;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LocationDAO {
    Session session=null;
    
    public List<Location> getAllLocation(){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        String hql="From "+Location.class.getName();
        Query query=session.createQuery(hql);
        List<Location> locations=query.list();
        session.close();
        return locations;
    }

    public int addLocation(Location location){
        this.session=HibernateUtil.getSessionFactory().openSession();
        Transaction tx=session.beginTransaction();
        session.save(location);
        session.getTransaction().commit();
        session.close();
        return location.getLocationId();
    }
}
