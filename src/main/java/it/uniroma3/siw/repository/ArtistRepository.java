package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

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

}
