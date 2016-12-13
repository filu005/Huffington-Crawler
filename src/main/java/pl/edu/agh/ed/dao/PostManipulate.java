package pl.edu.agh.ed.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import pl.edu.agh.ed.Consts;
import pl.edu.agh.ed.objects.Author;
import pl.edu.agh.ed.objects.Category;
import pl.edu.agh.ed.objects.Post;

/**
 * Klasa wspomagajaca dzialania na tabeli z postami
 */
public class PostManipulate extends Manipulate {

	/**
	 * Konstruktor
	 * 
	 * @param factory
	 *            fabryka polaczen
	 */
	public PostManipulate(SessionFactory factory) {
		super(factory);
	}

	/**
	 * Dodanie posta
	 * 
	 * @param post
	 *            post
	 */
	public void addPost(Post post) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.persist(post);
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
	 * Dodanie posta
	 * 
	 * @param content
	 *            tresc
	 * @param date
	 *            data
	 * @param link
	 *            link
	 * @param title
	 *            tytul
	 * @param author
	 *            autor
	 * @param category
	 *            kategory
	 * @param done
	 *            czy opracowany
	 * @return dodany post
	 */
	public Post addPost(String content, Date date, String link, String title,
			Author author, Category category, Boolean done) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Post post = new Post();
			post.setContent(content);
			post.setDate(date);
			post.setLink(link);
			post.setTitle(title);
			post.setAuthor(author);
			post.setCategory(category);
			post.setZrobiona(done);
			session.persist(post);
			tx.commit();
			return post;
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
	 * Sprawdzanie czy post istnieje w bazie danych
	 * 
	 * @param site
	 *            link do posta
	 * @return istnieje czy nie
	 */
	public boolean checkIfExistsLink(String site) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Post.class);
			cr.add(Restrictions.eq("link", site));

			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				Post post = (Post) iterator.next();
				return true;
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return false;
	}

	/**
	 * Usuwanie html-tagow z tresci
	 * 
	 * @param firstResult
	 *            id skad zaczac
	 * @param lastResult
	 *            id gdzie skonczyc
	 * @return czy sa jeszcze wiersze do opracowania
	 */
	public boolean deleteTags(int firstResult, int lastResult) {
		Session session = factory.openSession();
		Transaction tx = null;
		int i = 0;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Post.class);
			cr.add(Restrictions.ge("id", firstResult));
			cr.add(Restrictions.le("id", lastResult));

			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				Post post = (Post) iterator.next();
				post.setContent(post.getContent().replaceAll(
						Consts.PATTERN_DELETE_TAGS, ""));
				session.update(post);
				i++;
			}

			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (i > 1) {
			return true;
		}
		return false;
	}

	/**
	 * Wyszukiwanie postow, ktora mieszcza sie w okresie
	 * 
	 * @param d1
	 *            data od
	 * @param d2
	 *            data do
	 * @return lista postow
	 */
	public List<Post> getLinkByDate(Date d1, Date d2) {
		Session session = factory.openSession();
		Transaction tx = null;
		List<Post> posts = new ArrayList<Post>();
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Post.class);
			cr.add(Restrictions.ge("date", d1));
			cr.add(Restrictions.le("date", d2));

			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				posts.add((Post) iterator.next());
			}
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}

		return posts;
	}

	/**
	 * Wyszukiwanie postow, ktore maja pusty kontent
	 * 
	 * @return lista postow
	 */
	public List<Post> getLinksWithEmptyContent() {
		Session session = factory.openSession();
		Transaction tx = null;
		List<Post> posts = new ArrayList<Post>();
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Post.class);
			cr.add(Restrictions.ge("id", 783330));
			cr.add(Restrictions.le("id", 784285));
			cr.add(Restrictions.eqOrIsNull("content", ""));
			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				posts.add((Post) iterator.next());
			}
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}

		return posts;
	}

	/**
	 * Wyszukiwanie posta po linku
	 * 
	 * @param link
	 *            link
	 * @return znaleziony post lub null
	 */
	public Post getPostByLink(String link) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Post.class);
			cr.add(Restrictions.eq("link", link));
			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				Post postFinded = (Post) iterator.next();
				return postFinded;
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

	/**
	 * Mergeowanie tresci posta
	 * 
	 * @param postId
	 *            ID posta
	 * @param content
	 *            tresc
	 */
	public void mergePostContent(Integer postId, String content) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Post.class);
			cr.add(Restrictions.eq("id", postId));
			Post p = ((Post) cr.list().get(0));
			p.setContent(content);
			session.merge(p);
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