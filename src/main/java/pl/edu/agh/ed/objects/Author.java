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
 * Klasa reprezentujaca tabele authors w bazie danych
 */
@Entity
@Table(name = "authors")
public class Author implements Serializable {

	private static final long serialVersionUID = 2752471707053114715L;

	/**
	 * Komentarze autora
	 */
//	@OneToMany
//	private Set<Comment> comments;

	/**
	 * ID autora
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * Link do autora
	 */
	@Column(name = "link")
	private String link;

	/**
	 * Imie autora
	 */
	@Column(name = "name")
	private String name;

	/**
	 * Posty autora
	 */
//	@OneToMany
//	private Set<Post> posts;
	
	/**
	 * ID autora z FB
	 */
	@Column(name = "FB_id")
	private String FB_id;

	public Author() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param link
	 *            link
	 * @param name
	 *            imie
	 */
	public Author(String link, String name) {
		this.link = link;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public String getFBid() {
		return FB_id;
	}

	public String getLink() {
		return link;
	}

	public String getName() {
		return name;
	}

//	public Set<Post> getPosts() {
//		return posts;
//	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setFBid(String id) {
		this.FB_id = id;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public void setPosts(Set<Post> posts) {
//		this.posts = posts;
//	}

}
