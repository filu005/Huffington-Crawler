package pl.edu.agh.ed.dao;

import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import pl.edu.agh.ed.objects.Topic;

/**
 * Klasa wspomagajaca dzialania na tabeli z topikami
 */
public class TopicManipulate extends Manipulate {

	/**
	 * Konstruktor
	 * 
	 * @param factory
	 *            fabryka polaczen
	 */
	public TopicManipulate(SessionFactory factory) {
		super(factory);
	}

	/**
	 * Dodanie topika
	 * 
	 * @param keywords
	 *            keywordsy
	 * @return dodany topik
	 */
	public Topic addTopic(String keywords) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Topic topic = new Topic();
			topic.setKeywords(keywords);
			session.persist(topic);
			tx.commit();
			return topic;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * Dodanie topika
	 * 
	 * @param topic
	 *            topik
	 */
	public void addTopic(Topic topic) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.persist(topic);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/**
	 * Wyszukiwanie topika po keywordsach
	 * 
	 * @param keywords
	 *            keywordsy
	 * @return znaleziony topik lub null
	 */
	public Topic getTopicByName(String keywords) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Topic.class);
			// Add restriction.
			cr.add(Restrictions.eq("keywords", keywords));

			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				Topic topic = (Topic) iterator.next();
				return topic;
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}
}
