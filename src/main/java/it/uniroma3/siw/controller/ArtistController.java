package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.service.ArtistService;

@Controller
public class ArtistController {

	@Autowired ArtistService artistService; 
	
	/**
	 * GET : pagina principale artisti
	 * 
	 * @return
	 */
	@GetMapping("/indexArtists")
	public String indexArtists() {
		return "all/indexArtists.html";
	}

	/**
	 * GET : pagina con tutti gli artisti
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/artists")
	public String artists(Model model) {
		model.addAttribute("artists", this.artistService.getAllArtists());
		return "all/artists.html";
	}

	/**
	 * POST : pagina con il nuovo artista, altrimenti ritorno alla pagina con la
	 * form per inserire un nuovo artista nel sistema
	 * 
	 * @param artist
	 * @param model
	 * @return
	 */
	@PostMapping("/admin/artists")
	public String newArtist(@ModelAttribute("artist") Artist artist, Model model) {
		// controllo esistenza dell'artista
		if (this.artistService.newArtist(artist)) {
			model.addAttribute("artist", artist);
			model.addAttribute("starredMovies", artist.getStarredMovies());
			model.addAttribute("directedMovies", artist.getDirectedMovies()); 
			return "all/artist.html";
		}
		return "admin/formNewArtist.html";
	}

	/**
	 * GET : pagina con la form per inserire un nuovo artista nel sistema
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}

	
	@GetMapping("/artists/{artistId}")
	public String artist(@PathVariable("artistId") Long artistId, Model model) {
		Artist artist = this.artistService.getArtist(artistId); 
		model.addAttribute("artist", artist);
		model.addAttribute("starredMovies", artist.getStarredMovies()); 
		model.addAttribute("directedMovies", artist.getDirectedMovies()); 
		return "all/artist.html";
	}
}
