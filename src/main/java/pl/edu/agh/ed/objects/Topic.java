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
 * Klasa reprezentujaca topik w bazie danych
 */
@Entity
@Table(name = "topics")
public class Topic implements Serializable {

	/**
	 * Komentarze topika
	 */
//	@OneToMany
//	private Set<CommentTopic> commentTopics;

	/**
	 * ID topika
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * Keywordsy topika
	 */
	@Column(name = "keywords")
	private String keywords;

	/**
	 * Posty topika
	 */
//	@OneToMany
//	private Set<PostTopic> postTopics;

//	public Set<CommentTopic> getCommentTopics() {
//		return commentTopics;
//	}

	public int getId() {
		return id;
	}

	public String getKeywords() {
		return keywords;
	}

//	public Set<PostTopic> getPostTopics() {
//		return postTopics;
//	}

//	public void setCommentTopics(Set<CommentTopic> commentTopics) {
//		this.commentTopics = commentTopics;
//	}

	public void setId(int id) {
		this.id = id;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

//	public void setPostTopics(Set<PostTopic> postTopics) {
//		this.postTopics = postTopics;
//	}
}
