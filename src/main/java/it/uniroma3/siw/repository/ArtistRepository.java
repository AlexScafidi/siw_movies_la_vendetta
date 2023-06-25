package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Artist;

public interface ArtistRepository extends CrudRepository<Artist, Long> {

	/**
	 * controlla se esiste un artista con tale nome e cognome
	 * 
	 * @param name
	 * @param surname
	 * @return
	 */
	public boolean existsByNameAndSurname(String name, String surname);

	public List<Artist> findByName(String Name);

	@Query(nativeQuery = true,
			value="select distinct * from artist a where a.id not in (select actors_id from movie_actors ma where ma.starred_movies_id = :movieId)")
	public Iterable<Artist> findAllNotInMovie(@Param("movieId")Long movieId);

}
