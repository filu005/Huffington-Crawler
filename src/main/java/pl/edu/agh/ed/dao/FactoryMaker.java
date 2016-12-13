package pl.edu.agh.ed.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Pomaga w tworzenia fabryki polaczen do okreslonej tabeli
 */
public final class FactoryMaker {
	
	private static ServiceRegistry serviceRegistry;
	private static SessionFactory sessionFactory;
	/**
	 * Tworzy fabryke polaczen do okreslonej tabeli
	 * 
	 * @param clazz
	 *            klasa reprezentujaca tabele
	 * @return fabryka polaczen
	 */

	static {
        try {
            Logger.getLogger("org.hibernate").setLevel(Level.ALL);
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (HibernateException exception) {
            System.out.println("Problem creating session factory");
            System.out.println(exception.getMessage());
        }
    }

	
	public static SessionFactory getSessionFactory(Class clazz) {
//		Configuration configuration = new Configuration();
//		configuration.configure();
//		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
//	            configuration.getProperties()).build();
//		
//		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//		
		return sessionFactory;
	}
}
