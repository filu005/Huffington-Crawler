package pl.edu.agh.ed.dao;

import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import pl.edu.agh.ed.objects.Category;

/**
 * Klasa wspomagajaca dzialania na tabeli z kategoriami
 */
public class CategoryManipulate extends Manipulate {

	/**
	 * Konstruktor
	 * 
	 * @param factory
	 *            fabryka polaczen
	 */
	public CategoryManipulate(SessionFactory factory) {
		super(factory);
	}

	/**
	 * Dodanie kategorii do tabeli
	 * 
	 * @param category
	 *            kategoria
	 */
	public void addCategory(Category category) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.persist(category);
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
	 * Dodanie kategorii do tabeli
	 * 
	 * @param name
	 *            nazwa
	 * @return dodana kategoria
	 */
	public Category addCategory(String name) {
		Category category = new Category();
		category.setCategoryName(name);
		addCategory(category);
		return category;
	}

	/**
	 * Wyszukiwanie kategorii po nazwie
	 * 
	 * @param categoryName
	 *            nazwa kategorii
	 * @return znaleziona kategoria lub null
	 */
	public Category getCategoryByName(String categoryName) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Category.class);
			cr.add(Restrictions.eq("categoryName", categoryName));

			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				Category category = (Category) iterator.next();
				return category;
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