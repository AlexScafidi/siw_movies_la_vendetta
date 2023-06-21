package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import jakarta.transaction.Transactional;

@Controller
public class MovieController {

	@Autowired
	private MovieService movieService;
	@Autowired ArtistService artistService; 
	
	@GetMapping(value = "/")
	public String index() {
		return "all/index.html";
	}

	/**
	 * GET : pagina principale dei film
	 * 
	 * @return
	 */
	@GetMapping(value = "/indexMovies")
	public String indexMovies() {
		return "all/indexMovies.html";
	}

	/**
	 * GET : pagina con tutti i film
	 */
	@GetMapping(value = "/movies")
	public String movies(Model model) {
		model.addAttribute("movies", this.movieService.getAllMovies());
		return "all/movies.html";
	}

	/**
	 * POST : pagina con il film appena inserito se tutto è corretto
	 * 
	 * @param movie
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/movies")
	public String newMovie(@ModelAttribute("movie") Movie movie, Model model) {
		// controllo se il film è corretto
		if (this.movieService.newMovie(movie)) {
			model.addAttribute("movie", movie);
			return "all/movie.html";
		}
		return "admin/formNewMovie.html";
	}

	@GetMapping(value = "/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

	/**
	 * GET : pagina con i dettagli di un film
	 */
	@GetMapping(value = "/movies/{movieId}")
	public String movie(@PathVariable("movieId") Long movieId, Model model) {
		model.addAttribute("movie", this.movieService.getMovie(movieId));
		return "all/movie.html";
	}
	
	/**
	 * GET : pagina per gestire i film
	 * @param model
	 * @return
	 */
	@GetMapping(value="/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies",this.movieService.getAllMovies()); 
		return "admin/manageMovies.html";
	}
	
	/**
	 * GET : pagina per il form per aggiornare un film
	 * @param movieId
	 * @param model
	 * @return
	 */
	@GetMapping(value="/admin/formUpdateMovie/{movieId}")
	public String formUpdateMovie(@PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.getMovie(movieId); 
		if(movie == null) return "movieError.html"; 
		model.addAttribute("movie",movie);
		return "admin/formUpdateMovie.html";
	}
	
	/**
	 * GET : pagina per indicare quale artista sia il regista del film
	 * @param movieId
	 * @param model
	 * @return
	 */
	@GetMapping(value="/admin/directorToAdd/{movieId}")
	public String addDirector(@PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.getMovie(movieId); 
		if(movie==null) return "movieError.html"; 
		//altrimenti 
		model.addAttribute("movie",movie);
		model.addAttribute("artists", this.artistService.getAllArtists());
		return "admin/directorToAdd.html";
	}
	
	/**
	 * GET : pagina movie update con il regista tolto
	 * @param idM
	 * @param model
	 * @return
	 */
	@GetMapping(value="/admin/removeDirectorToMovie/{movieId}")
	public String removeDirector(@PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.removeDirectorFromMovie(movieId); 
		if(movie == null) return "movieError.hmtl"; 
		model.addAttribute("movie",movie); 
		return  "admin/formUpdateMovie.html";
	}
	
	/**
	 * GET : pagina movie update con nuovo regista impostato
	 * @param directorId
	 * @param movieId
	 * @param model
	 * @return
	 */
	@GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirector(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.SetDirectorToMovie(movieId, directorId);
		if(movie == null) return "movieError.hmtl"; 
		model.addAttribute("movie",movie); 
		return "admin/formUpdateMovie.html";
	}
	
	/**
	 * GET : pagina per selezionare gli attori che hanno recitato nel movie
	 * @param movieId
	 * @param model
	 * @return
	 */
	@Transactional
	@GetMapping(value="/admin/updateActorsOnMovie/{movieId}")
	public String actorsToAdd(@PathVariable("movieId") Long movieId, Model model) {
		
		Movie movie = this.movieService.getMovie(movieId); 
		if(movie == null) return "movieError.hmtl"; 
		model.addAttribute("movie",movie); //il film 
		model.addAttribute("actorsNotInMovie", this.artistService.findAllByactorInMoviesIsNotContaining(movie)); //tutti gli attori non nel film
		model.addAttribute("actorsInMovie",movie.getActors());  //tutti gli attori del film
		return "admin/actorsToAdd.html";
	}
	
	/**
	 * GET : pagina per selezionare gli attori da aggiungere dopo la rimozione dell'attore
	 * @param actorId
	 * @param movieId
	 * @param model
	 * @return
	 */
	@Transactional
	@GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		
		
		Movie movie = this.movieService.removeActorFromMovie(movieId,actorId); 
		
		if(movie == null) return "movieError.html";
		//altrimenti 
		model.addAttribute("movie",movie); //il film 
		model.addAttribute("actorsNotInThisMovie", this.artistService.findAllByactorInMoviesIsNotContaining(movie)); //tutti gli attori non nel film
		model.addAttribute("actorsInMovie",movie.getActors());  //tutti gli attori del film
		return "admin/actorsToAdd.html"; 
	}
	
	@Transactional
	@GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model){
		
		//SOLUZIONE TEMPORANEA PER EVITARE DI AGGIUNGERE ALLA COLLEZIONE PIU' VOLTE LO STESSO FILM QUANDO RICARICO LA PAGINA
		//if(!movie.getActors().contains(artist)) movie.getActors().add(this.aR.findById(idA).get()); 
		
		Movie movie = this.movieService.addActorFromMovie(movieId,actorId); 
		
		if(movie == null) return "movieError.html";
		//altrimenti 
		model.addAttribute("movie",movie); //il film 
		model.addAttribute("actorsNotInThisMovie", this.artistService.findAllByactorInMoviesIsNotContaining(movie)); //tutti gli attori non nel film
		model.addAttribute("actorsInMovie",movie.getActors());  //tutti gli attori del film
		r

}
