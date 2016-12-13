package pl.edu.agh.ed.dao;

import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import pl.edu.agh.ed.objects.Author;

/**
 * Klasa wspomagajaca dzialania na tabeli z autorami
 */
public class AuthorManipulate extends Manipulate {

	/**
	 * Konstruktor
	 * 
	 * @param factory
	 *            fabryka polaczen
	 */
	public AuthorManipulate(SessionFactory factory) {
		super(factory);
	}

	/**
	 * Dodaje autora do tabeli
	 * 
	 * @param author
	 *            autor
	 */
	public void addAuthor(Author author) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.persist(author);
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
	 * Dodaje autora do tabeli
	 * 
	 * @param link
	 *            link
	 * @param name
	 *            imie
	 */
	public void addAuthor(String link, String name) {
		Author author = new Author();
		author.setLink(link);
		author.setName(name);
		addAuthor(author);
	}

	/**
	 * Sprawdza czy autor istnieje w bazie danych
	 * 
	 * @param authorToSearch
	 *            autor
	 * @return jesli autor znaleziony, to zwraca go, inaczej null
	 */
	public Author checkIfExistsAuthor(Author authorToSearch) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Author.class);
			cr.add(Restrictions.eq("name", authorToSearch.getName()));
			cr.add(Restrictions.eq("link", authorToSearch.getLink()));

			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				Author author = (Author) iterator.next();
				return author;
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
	 * Wyszukuje autora po imieniu
	 * 
	 * @param name
	 *            imie
	 * @return znaleziony autor lub null
	 */
	public Author getAuthorByName(String name) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Author.class);
			cr.add(Restrictions.eq("name", name));

			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				Author author = (Author) iterator.next();
				return author;
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
	 * Wyszukuje autora, ktorego imie zaczyna sie z podanego tekstu
	 * 
	 * @param text
	 *            tekst do wyszukiwania
	 * @return znaleziony autor lub null
	 */
	public Author getAuthorNameStartsWith(String text) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Author.class);
			cr.add(Restrictions.like("name", text, MatchMode.START));

			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				Author author = (Author) iterator.next();
				return author;
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
