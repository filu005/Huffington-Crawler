package pl.edu.agh.ed.dao;

import org.hibernate.SessionFactory;

/**
 * Klasa z fabryka polaczen
 */
public class Manipulate {

	/**
	 * Fabryka polaczen
	 */
	protected SessionFactory factory;

	/**
	 * Konstruktor
	 * 
	 * @param factory
	 *            fabryka polaczen
	 */
	public Manipulate(SessionFactory factory) {
		this.factory = factory;
	}

}
