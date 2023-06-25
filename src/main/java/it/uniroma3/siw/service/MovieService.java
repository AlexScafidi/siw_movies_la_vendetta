package it.uniroma3.siw.service;

import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.MovieRepository;
import jakarta.transaction.Transactional;

@Component
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;
	@Autowired 
	private ArtistService artistService; 

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
		List<Artist> actors = movie.getActors(); 
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

}