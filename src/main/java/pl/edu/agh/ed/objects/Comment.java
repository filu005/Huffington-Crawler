package pl.edu.agh.ed.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Klasa reprezentujaca komentarz w bazie danych
 */
@Entity
@Table(name = "comments")
public class Comment implements Serializable {

	/**
	 * Autor komentarza
	 */
	@ManyToOne
	@JoinColumn(name = "author_id")
	private Author author;

	/**
	 * Nadrzedny komentarz
	 */
	@ManyToOne
	@JoinColumn(name = "parent_comment")
	private Comment comment;

	/**
	 * Topici komentarza
	 */
//	@OneToMany
//	private Set<CommentTopic> commentTopics;

	/**
	 * Tresc
	 */
	@Column(name = "content", columnDefinition = "text")
	private String content;

	/**
	 * Data napisania komentarza
	 */
	@Column(name = "date")
	private Date date;

	/**
	 * ID komentarza
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * Post, w ktorym komentarz zostal napisany
	 */
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	/**
	 * Tytul komentarza
	 */
	@Column(name = "title")
	private String title;
	
	/**
	 * ID komentarza z FB
	 */
	@Column(name = "FB_id")
	private String FB_id;

	public Author getAuthor() {
		return author;
	}

	public Comment getComment() {
		return comment;
	}

//	public Set<CommentTopic> getCommentTopics() {
//		return commentTopics;
//	}

	public String getContent() {
		return content;
	}

	public Date getDate() {
		return date;
	}

	public int getId() {
		return id;
	}
	
	public String getFBid() {
		return FB_id;
	}

	public Post getPost() {
		return post;
	}

	public Post getPost_id() {
		return post;
	}

	public String getTitle() {
		return title;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

//	public void setCommentTopics(Set<CommentTopic> commentTopics) {
//		this.commentTopics = commentTopics;
//	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setFBid(String id) {
		this.FB_id = id;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public void setPost_id(Post post) {
		this.post = post;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
