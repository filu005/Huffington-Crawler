package pl.edu.agh.ed.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import pl.edu.agh.ed.objects.Comment;
import pl.edu.agh.ed.objects.CommentTopic;
import pl.edu.agh.ed.objects.Topic;

/**
 * Klasa wspomagajaca dzialania na tabeli lacznikowej komentarz a topic
 */
public class CommentTopicManipulate extends Manipulate {

	/**
	 * Konstruktor
	 * 
	 * @param factory
	 *            fabryka polaczen
	 */
	public CommentTopicManipulate(SessionFactory factory) {
		super(factory);
	}

	/**
	 * Dodanie wiersza
	 * 
	 * @param comment
	 *            komentarz
	 * @param topic
	 *            topic
	 * @param prob
	 *            prawdopodobienstwo
	 * @return dodany wiersz
	 */
	public CommentTopic addCommentTopic(Comment comment, Topic topic,
			double prob) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			CommentTopic commentTopic = new CommentTopic();
			commentTopic.setComment_id(comment);
			commentTopic.setTopic_id(topic);
			commentTopic.setProbability(prob);
			session.persist(commentTopic);
			tx.commit();
			return commentTopic;
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
	 * Dodanie wiersza
	 * 
	 * @param commentTopic
	 *            wiersz
	 */
	public void addCommentTopic(CommentTopic commentTopic) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.persist(commentTopic);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}
