package pl.edu.agh.ed.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Klasa reprezentujaca wiersz w tabeli lacznikowej pomiedzy komentarzem a
 * topikiem
 */
@Entity
@Table(name = "comment_topic")
public class CommentTopic implements Serializable {

	/**
	 * ID komentarza
	 */
	@EmbeddedId
	@ManyToOne
	@JoinColumn(name = "comment_id")
	private Comment comment_id;

	/**
	 * Prawdopodobienstwo
	 */
	@Column(name = "probability")
	private double probability;

	/**
	 * ID topika
	 */
	@EmbeddedId
	@ManyToOne
	@JoinColumn(name = "topic_id")
	private Topic topic_id;

	public Comment getComment_id() {
		return comment_id;
	}

	public double getProbability() {
		return probability;
	}

	public Topic getTopic_id() {
		return topic_id;
	}

	public void setComment_id(Comment comment_id) {
		this.comment_id = comment_id;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public void setTopic_id(Topic topic_id) {
		this.topic_id = topic_id;
	}
}
