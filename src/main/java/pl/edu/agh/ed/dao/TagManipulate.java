package pl.edu.agh.ed.dao;

import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import pl.edu.agh.ed.objects.Tag;

public class TagManipulate {

	private SessionFactory factory;

	public TagManipulate(SessionFactory factory) {
		this.factory = factory;
	}

	public Tag addComment(String name) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Tag tag = new Tag();
			tag.setName(name);
			session.persist(tag);
			tx.commit();
			return tag;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public void addTag(Tag tag) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.persist(tag);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public Tag getTagByName(Tag tag) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Tag.class);
			// Add restriction.
			cr.add(Restrictions.eq("name", tag.getName()));

			for (Iterator iterator = cr.list().iterator(); iterator.hasNext();) {
				Tag tagFinded = (Tag) iterator.next();
				return tagFinded;
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
