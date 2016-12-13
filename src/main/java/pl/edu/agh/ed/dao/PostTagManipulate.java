package pl.edu.agh.ed.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import pl.edu.agh.ed.objects.Post;
import pl.edu.agh.ed.objects.PostTag;
import pl.edu.agh.ed.objects.Tag;

/**
 * Klasa wspomagajaca dzialania na tabeli lacznikowej post a tag
 */
public class PostTagManipulate extends Manipulate {

	/**
	 * Konstruktor
	 * 
	 * @param factory
	 *            fabryka polaczen
	 */
	public PostTagManipulate(SessionFactory factory) {
		super(factory);
	}

	/**
	 * Dodanie wiersza
	 * 
	 * @param post
	 *            post
	 * @param tag
	 *            tag
	 * @return dodany wiersz
	 */
	public PostTag addPostTag(Post post, Tag tag) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			PostTag postTag = new PostTag();
			postTag.setPost_id(post);
			postTag.setTag_id(tag);
			session.persist(postTag);
			tx.commit();
			return postTag;
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
	 * @param postTag
	 *            wiersz
	 */
	public void addPostTag(PostTag postTag) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.persist(postTag);
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
