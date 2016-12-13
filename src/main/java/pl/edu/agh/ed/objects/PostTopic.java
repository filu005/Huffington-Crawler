package pl.edu.agh.ed.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Klasa reprezentujaca wiersz w tabeli lacznikowej pomiedzy postem a topikiem
 */
@Entity
@Table(name = "post_topic")
public class PostTopic implements Serializable {

	/**
	 * ID posta
	 */
	@EmbeddedId
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post_id;

	/**
	 * Prawdopodobienstwo
	 */
	@Column(name = "probability")
	private double precision;

	/**
	 * ID topika
	 */
	@EmbeddedId
	@ManyToOne
	@JoinColumn(name = "topic_id")
	private Topic topic_id;

	public Post getPost_id() {
		return post_id;
	}

	public double getPrecision() {
		return precision;
	}

	public Topic getTopic_id() {
		return topic_id;
	}

	public void setPost_id(Post post_id) {
		this.post_id = post_id;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public void setTopic_id(Topic topic_id) {
		this.topic_id = topic_id;
	}
}
