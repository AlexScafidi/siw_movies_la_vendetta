package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.MovieRepository;
import jakarta.transaction.Transactional;

@Component
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;
	
	@Transactional
	public Iterable<Movie> getAllMovies() {
		return this.movieRepository.findAll(); 
	}

		/**
	 * salva un nuovo film nel sistema
	 * @param artist
	 * @return
	 */
	public boolean newMovie(Movie movie) {
		//controllo che il film già esista
		if(this.movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear())) return false; 
		//altrimenti non esiste e lo salvo
		this.movieRepository.save(movie); 
		return true; 
	}
	
	/**
	 * restituisce un film a partire dall'id
	 * @param movieId
	 * @return
	 */

		public Movie getMovie(Long movieId) {
			return this.movieRepository.findById(movieId).orElse(null); 
		}

}