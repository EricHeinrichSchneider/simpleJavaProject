package com.erichschneider;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import com.erichschneider.pojo.Event;

import java.util.Date;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App
{
    private SessionFactory sessionFactory;

    public static void main( String[] args )
    {
        App app = new App();
        app.run();
    }

    public void run(){
      final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
      .configure() // configures settings from hibernate.cfg.xml
      .build();
      try {
        sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        Session session = sessionFactory.openSession();
    		session.beginTransaction();
        for(int i=0;i<100000;i++){
          session.save( new Event( "Event: " + i , new Date() ) );
        }
    		session.getTransaction().commit();
    		session.close();

    		// now lets pull events from the database and list them
    		session = sessionFactory.openSession();
            session.beginTransaction();
            List result = session.createQuery( "from Event" ).list();
    		for ( Event event : (List<Event>) result ) {
    			System.out.println( "Event (" + event.getDate() + ") : " + event.getTitle() );
    		}
        session.getTransaction().commit();
        session.close();

      }
      catch (Exception e) {
        // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
        // so destroy it manually.
        StandardServiceRegistryBuilder.destroy( registry );
      }

      if ( sessionFactory != null ) {
			sessionFactory.close();
		  }

    }
}
