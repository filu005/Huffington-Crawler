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
import pl.edu.agh.ed.objects.Comment;
import pl.edu.agh.ed.objects.Post;

/**
 * Klasa wspomagajaca dzialania na tabeli z komentarzami
 */
public class CommentManipulate extends Manipulate {

	/**
	 * Konstruktor
	 * 
	 * @param factory
	 *            fabryka polaczen
	 */
	public CommentManipulate(SessionFactory factory) {
		super(factory);
	}

	/**
	 * Dodanie komentarza do tabeli
	 * 
	 * @param comment
	 *            komentarz
	 */
	public void addComment(Comment comment) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.persist(comment);
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
	 * Dodanie komentarza
	 * 
	 * @param content
	 *            tresc
	 * @param date
	 *            data
	 * @param title
	 *            tytul
	 * @param author
	 *            autor
	 * @param parentComment
	 *            nadrzedny komentarz
	 * @param post
	 *            post
	 * @return dodany komentarz
	 */
	public Comment addComment(String content, Date date, String title,
			Author author, Comment parentComment, Post post) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Comment comment = new Comment();
			comment.setContent(content);
			comment.setDate(date);
			comment.setTitle(title);
			comment.setAuthor(author);
			comment.setComment(comment);
			comment.setPost(post);
			session.persist(comment);
			tx.commit();
			return comment;
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
	 * Usuwanie html-tagow z tresci
	 */
	public void deleteTags() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Comment.class);
			// Add restriction.
			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				Comment comment = (Comment) iterator.next();
				comment.setContent(comment.getContent() == null ? null
						: comment.getContent().replaceAll(
								Consts.PATTERN_DELETE_TAGS, ""));
				session.update(comment);
			}
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
	 * Usuwanie html-tagow z tresci z okreslonym offsetem
	 * 
	 * @param firstResult
	 *            id skad zaczac
	 * @param lastResult
	 *            id gdzie skonczyc
	 * @return czy sa jeszcze elementy
	 */
	public boolean deleteTags(int firstResult, int lastResult) {
		Session session = factory.openSession();
		Transaction tx = null;
		int i = 0;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Comment.class);
			cr.add(Restrictions.ge("id", firstResult));
			cr.add(Restrictions.le("id", lastResult));

			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				Comment comment = (Comment) iterator.next();
				comment.setContent(comment.getContent().replaceAll(
						Consts.PATTERN_DELETE_TAGS, ""));
				session.update(comment);
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
	 * Wyszukiwanie komentarza po tresci oraz czasie
	 * 
	 * @param comment
	 *            koment
	 * @return znaleziony koment lub null
	 */
	public Comment getCommentByName(Comment comment) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Comment.class);
			cr.add(Restrictions.eq("content", comment.getContent()));
			cr.add(Restrictions.eq("date", comment.getDate()));

			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				Comment commentFinded = (Comment) iterator.next();
				return commentFinded;
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
	 * Wyszukiwanie wszystkich komentarzy posta
	 * 
	 * @param post
	 *            post
	 * @return lista komentarzy
	 */
	public List<Comment> getCommentByPost(Post post) {
		Session session = factory.openSession();
		Transaction tx = null;
		List<Comment> comments = new ArrayList<Comment>();
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Comment.class);
			cr.add(Restrictions.eq("post", post));
			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				comments.add((Comment) iterator.next());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return comments;
	}
}
