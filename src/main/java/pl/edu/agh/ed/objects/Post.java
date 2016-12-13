package pl.edu.agh.ed.objects;

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
 * Klasa reprezentujaca post w bazie danych
 */
@Entity
@Table(name = "posts")
public class Post {

	/**
	 * Autor posta
	 */
	@ManyToOne
	@JoinColumn(name = "author_id")
	private Author author;

	/**
	 * Kategoria posta
	 */
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	/**
	 * Komentarze do posta
	 */
	@OneToMany
	private Set<Comment> comments;

	/**
	 * Tresc posta; columnDefinition albo Lob
	 */
	@Column(name = "content", columnDefinition = "text")
	private String content;

	/**
	 * Data napisania posta
	 */
	@Column(name = "date")
	private Date date;

	/**
	 * ID posta
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * Link do posta
	 */
	@Column(name = "link")
	private String link;

	/**
	 * Tagi posta
	 */
	@OneToMany
	private Set<PostTag> postTags;

	/**
	 * Topici posta
	 */
	@OneToMany
	private Set<PostTopic> postTopics;

	/**
	 * Unikatowe id posta
	 */
	@Column(name = "site_id")
	private String site;

	/**
	 * Tytul
	 */
	@Column(name = "title")
	private String title;

	/**
	 * Czy opracowany post
	 */
	@Column(name = "zrobiona")
	private Boolean zrobiona;

	public Post() {
	}

	public Author getAuthor() {
		return author;
	}

	public Category getCategory() {
		return category;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public String getContent() {
		return content;
	}

	public Date getDate() {
		return date;
	}

	public int getId() {
		return id;
	}

	public String getLink() {
		return link;
	}

	public Set<PostTag> getPostTags() {
		return postTags;
	}

	public Set<PostTopic> getPostTopics() {
		return postTopics;
	}

	public String getSite() {
		return site;
	}

	public String getTitle() {
		return title;
	}

	public Boolean getZrobiona() {
		return zrobiona;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setPostTags(Set<PostTag> postTags) {
		this.postTags = postTags;
	}

	public void setPostTopics(Set<PostTopic> postTopics) {
		this.postTopics = postTopics;
	}

	public void setSite(String string) {
		this.site = string;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setZrobiona(Boolean zrobiona) {
		this.zrobiona = zrobiona;
	}

}
