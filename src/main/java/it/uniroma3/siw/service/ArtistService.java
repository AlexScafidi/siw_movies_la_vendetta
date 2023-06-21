package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;
import jakarta.transaction.Transactional;

@Component
public class ArtistService {
	
	@Autowired 
	private ArtistRepository artistRepository; 

	/**
	 * restituisce tutti gli artisti prensenti nel sistema
	 * @return
	 */
	@Transactional
	public Iterable<Artist> getAllArtists() {
		return this.artistRepository.findAll(); 
	}

	/**
	 * salva un nuovo artista nel sistema
	 * @param artist
	 * @return
	 */
	public boolean newArtist(Artist artist) {
		//controllo che l'artista già esista
		if(this.artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname())) return false; 
		//altrimenti non esiste e lo salvo
		//NOTA : PER ORA SE METTO LA DATA DI MORTE ALLORA CICCIO E' MORTO 
		if(artist.getDateOfDeath() != null) artist.setAlive(false);
		this.artistRepository.save(artist); 
		return true; 
	}

	/**
	 * restituisce un artista a partire dall'id
	 * @param artistId
	 * @return
	 */
	public Artist getArtist(Long artistId) {
		return this.artistRepository.findById(artistId).orElse(null); 
	}

}
