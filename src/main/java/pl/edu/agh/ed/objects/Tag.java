package pl.edu.agh.ed.objects;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Klasa reprezentujaca tag w bazie danych
 */
@Entity
@Table(name = "tags")
public class Tag {

	/**
	 * ID taga
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * Nazwa taga
	 */
	@Column(name = "name")
	private String name;

	/**
	 * Posty taga
	 */
//	@OneToMany
//	private Set<PostTag> postTags;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

//	public Set<PostTag> getPostTags() {
//		return postTags;
//	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public void setPostTags(Set<PostTag> postTags) {
//		this.postTags = postTags;
//	}
}
