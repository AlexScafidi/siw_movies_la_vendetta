package it.uniroma3.siw.repository;

import java.time.Year;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Movie;

public interface MovieRepository extends CrudRepository<Movie, Long> {

	/**
	 * controlla se esiste un film dato il titolo e la data di uscita
	 * 
	 * @param title
	 * @param year
	 * @return
	 */
	public boolean existsByTitleAndYear(String title, Year year);

	/**
	 * riporta tutti i film usciti nella data specificata
	 * 
	 * @param year
	 * @return
	 */
	public List<Movie> findByYear(Year year);

	@Query(nativeQuery = true, value = "SELECT * FROM movie WHERE title LIKE 'c%'")
	public List<Movie> findMoviesThatStartWith(Character c);

}
