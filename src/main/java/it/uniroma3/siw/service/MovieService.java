package it.uniroma3.siw.service;

import java.time.Year;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.MovieRepository;
import jakarta.transaction.Transactional;

@Component
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;
	@Autowired 
	private ArtistService artistService;
	@Autowired
	private ReviewService reviewService; 

	@Transactional
	public Iterable<Movie> getAllMovies() {
		return this.movieRepository.findAll(); 
	}

	/**
	 * salva un nuovo film nel sistema
	 * @param artist
	 * @return
	 */
	@Transactional
	public Movie newMovie(Movie movie) {
		return (this.movieRepository.existsByTitleAndYear(movie.getTitle(),movie.getYear())) ? null : this.movieRepository.save(movie); 
	}

	/**
	 * salve un film già esistente
	 * @param movie
	 * @return
	 */
	public Movie saveMovie(Movie movie) {
		return this.movieRepository.save(movie);
	}
	/**
	 * restituisce un film a partire dall'id
	 * @param movieId
	 * @return
	 */
	@Transactional
	public Movie getMovie(Long movieId) {
		return this.movieRepository.findById(movieId).orElse(null); 
	}

	/**
	 * rimuove il direttore dal film 
	 * @param movieId
	 * @return
	 */
	@Transactional
	public Movie removeDirectorFromMovie(Long movieId) {
		Movie movie = this.getMovie(movieId);
		if(movie == null) return movie; 
		//altrimenti rimozione del regista
		movie.setDirector(null);
		return this.saveMovie(movie); 
	}
	
	/**
	 * setta il direttore dato al film
	 * @param movieId
	 * @param directorId
	 * @return
	 */
	@Transactional
	public Movie setDirectorToMovie(Long movieId, Long directorId) {
		Movie movie = this.getMovie(movieId); 
		Artist director = this.artistService.getArtist(directorId); 
		//controllo se ci sono entrambi
		if(movie == null || director == null) return null; 
		//altrimenti eseguo l'aggiornamento
		movie.setDirector(director);
		return this.saveMovie(movie); 
	}
	
	/**
	 * rimuove l'attore selezionato dal movie
	 * @param movieId
	 * @param actorId
	 * @return
	 */
	@Transactional
	public Movie removeActorFromMovie(Long movieId, Long actorId) {
		Movie movie = this.getMovie(movieId); 
		Artist actor = this.artistService.getArtist(actorId); 
		if(movie == null || actor == null) return null; 
		//altrimenti aggiorno
		List<Artist> actors = movie.getActors();
		actors.remove(actor); 
		//salvo anche sul db
		return this.saveMovie(movie); 
	}
	
	@Transactional
	public Movie addActorToMovie(Long movieId, Long actorId) {
		//controllo esistenza
		Movie movie = this.getMovie(movieId); 
		Artist actor = this.artistService.getArtist(actorId); 
		if(movie == null || actor == null) return null; 
		//altrimenti aggiorno
		Set<Artist> actors = new HashSet<>(movie.getActors()); 
		actors.add(actor); 
		return this.saveMovie(movie); 
	}

	public Movie changeInfo(Long movieId, String newTitle, Year newYear) {
		//controllo 
		Movie movie = this.getMovie(movieId); 
		if(movie == null || newTitle == null || newYear == null) return null; 
		//successo...provo ad modificare 
		//se le nuove info non sono esattamente le precedenti e non comportano ad in film già presente nel sistema
		if((!movie.getTitle().equals(newTitle) || !movie.getYear().equals(newYear)) &&
			!(this.movieRepository.existsByTitleAndYear(newTitle, newYear))) {
			movie.setTitle(newTitle);
			movie.setYear(newYear);
			//salvo le modifiche
			this.saveMovie(movie);
		}
		return movie; 
	}

	/**
	 * metodo per trovare tutti i film usciti in una certa data
	 * @param year
	 * @return
	 */
	@Transactional
	public List<Movie> findMoviesByYear(Year year) {
		List<Movie> movies = new LinkedList<>();
		Iterable<Movie> movieFounded = this.movieRepository.findByYear(year); 
		if(year == null) return movies; 
		for(Movie movie : movieFounded) movies.add(movie); 
		return movies; 
	}
	
	private String capitalize(String string) {
		StringBuilder sb = new StringBuilder(Character.toUpperCase(string.charAt(0)));
		sb.append(string.substring(1)); 
		return sb.toString(); 
	}

	/**
	 * metodo per trovare tutti i film usciti in una certa data
	 * @param year
	 * @return
	 */
	@Transactional
	public List<Movie> findMoviesByTitle(String title) {
		List<Movie> movies = new LinkedList<>();
		Iterable<Movie> movieFounded = this.movieRepository.findByTitle(title); 
		if(title == null) return movies; 
		for(Movie movie : movieFounded) movies.add(movie); 
		return movies; 
	}

	/**
	 * metodo per rimuovere una recensione da un film
	 * @param movieId
	 * @param reviewId
	 * @return
	 */
	public Movie deleteReviewFromMovie(Long movieId, Long reviewId) {
		Movie movie = this.getMovie(movieId); 
		Review review = this.reviewService.getReview(reviewId); 
		if(movie == null || review == null) return null; 
		//altrimenti aggiorno
		List<Review> reviews = movie.getReviews();
		reviews.remove(review); 
		//salvo anche sul db
		return this.saveMovie(movie); 
	}

}