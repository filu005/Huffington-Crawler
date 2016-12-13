package pl.edu.agh.ed.objects;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Klasa reprezentujaca kategorie w bazie danych
 */
@Entity
@Table(name = "category")
public class Category implements Serializable {

	private static final long serialVersionUID = 7322920457492125266L;

	/**
	 * Nazwa kategorii
	 */
	@Column(name = "category_name")
	private String categoryName;

	/**
	 * ID kategorii
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * Posty w kategorii
	 */
	@OneToMany
	private Set<Post> posts;

	public Category() {
	}

	public String getCategoryName() {
		return categoryName;
	}

	public int getId() {
		return id;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}
}
