package it.uniroma3.siw.model;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@jakarta.persistence.Entity
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	@NotBlank
	private String title;
	@Column(nullable = false)
	@NotBlank

	@Length(max = 1000) // al max 1000 caratteri
	private String text;
	@Min(value = 1)
	@Max(value = 5)
	@NotNull
	private Integer vote;

	@ManyToOne
	private Movie reviewedMovie;
	@ManyToOne
	private User owner;

	public Review() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getVote() {
		return vote;
	}

	public void setVote(Integer vote) {
		this.vote = vote;
	}

	public Movie getReviewedMovie() {
		return reviewedMovie;
	}

	public void setReviewedMovie(Movie reviewedMovie) {
		this.reviewedMovie = reviewedMovie;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

}
