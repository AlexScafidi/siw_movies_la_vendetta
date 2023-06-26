package it.uniroma3.siw.controller;

import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
		if (this.movieService.newMovie(movie) != null) {
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
	
	/***********************PER RICERCA***************************
	 ******************************************************************************* 
	 *******************************************************************************/
	
	/**
	 * GET : pagina per ricercare tutti i film usciti in una certa data
	 * @param model
	 * @return
	 */
	@GetMapping(value="/formSearchMoviesByYear")
	public String formSearchMovieByYear() {
		return "all/formSearchMoviesByYear.html"; 
	}
	
	/**
	 * POST : la pagina con tutti i film usciti nell'anno specificato
	 * @param year
	 * @param model
	 * @return
	 */
	@PostMapping(value="/saerchMoviesByYear")
	public String searchMoviesByYear(@RequestParam Year year, Model model) {
		model.addAttribute("moviesFounded", this.movieService.findMoviesByYear(year));
	model.addAttribute("year",year); 
		return "all/moviesFoundByYear.html"; 
	}
	
	/**
	 * GET : pagina per ricercare tutti i film che hanno nel titolo la stringa inserita
	 */
	@GetMapping(value="/formSearchMoviesByTitle")
	public String formSearchMoviesByTitle() {
		return "all/formSearchMoviesByTitle.html"; 
	}
	
	/**
	 * POST : la pagina con tutti i film che hanno nel titolo la stringa inserita
	 * @param title
	 * @param model
	 * @return
	 */
	@PostMapping(value="/saerchMoviesByTitle")
	public String searchMoviesByTitle(@RequestParam String title, Model model) {
		model.addAttribute("moviesFounded", this.movieService.findMoviesByTitle(title));
	model.addAttribute("title",title); 
		return "all/moviesFoundByTitle.html"; 
	}
	
	
	/***********************GESTIONE E MODIFICA DEI MOVIE***************************
	 ******************************************************************************* 
	 *******************************************************************************/
	
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
	@GetMapping(value="/admin/setDirectorToMovie/{movieId}/{directorId}")
	public String setDirector(@PathVariable("movieId") Long movieId, @PathVariable("directorId") Long directorId,Model model) {
		Movie movie = this.movieService.setDirectorToMovie(movieId, directorId);
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
		model.addAttribute("actorsNotInTheMovie", this.artistService.findAllActorsNotInTheMovie(movieId)); //tutti gli attori non nel film
		model.addAttribute("actorsInTheMovie",movie.getActors());  //tutti gli attori del film
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
	@GetMapping(value="/admin/removeActorFromMovie/{movieId}/{actorId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		
		
		Movie movie = this.movieService.removeActorFromMovie(movieId,actorId); 
		
		if(movie == null) return "movieError.html";
		//altrimenti 
		model.addAttribute("movie",movie); //il film 
		model.addAttribute("actorsNotInTheMovie", this.artistService.findAllActorsNotInTheMovie(movieId)); //tutti gli attori non nel film
		model.addAttribute("actorsInTheMovie",movie.getActors());  //tutti gli attori del film
		return "admin/actorsToAdd.html"; 
	}
	
	@Transactional
	@GetMapping(value="/admin/addActorToMovie/{movieId}/{actorId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model){
	
		Movie movie = this.movieService.addActorToMovie(movieId,actorId); 
		
		if(movie == null) return "all/movieError.html";
		//altrimenti 
		model.addAttribute("movie",movie); //il film 
		model.addAttribute("actorsNotInTheMovie", this.artistService.findAllActorsNotInTheMovie(movieId)); //tutti gli attori non nel film
		model.addAttribute("actorsInTheMovie",movie.getActors());  //tutti gli attori del film
		
		return "admin/actorsToAdd.html"; 
	}
	
	@GetMapping(value="/admin/formUpdateInfoMovie/{movieId}")
	public String formInfoMovie(@PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.getMovie(movieId);
		if(movie == null) return "all/movieError.html"; 
		//altrimenti 
		model.addAttribute("movie",movie);
		return "admin/formEditInfoMovie.html"; 
	}
	
	/**
	 * POST : metodo per cambiare le info di un film 
	 * @param movieId
	 * @param newTitle
	 * @param newYear
	 * @param model
	 * @return
	 */
	@PostMapping(value="/admin/updateInfo/{movieId}")
	public String newInfoMovie(@PathVariable("movieId") Long movieId, @RequestParam("newTitle") String newTitle, @RequestParam("newYear")  Year newYear, Model model) {
		Movie movie = this.movieService.changeInfo(movieId,newTitle,newYear); 
		if(movie == null) return "all/movieError.html"; 
		//altrimenti tutto a posto
		model.addAttribute("movie", movie); 
		return  "admin/formUpdateMovie.html";
	}
	

}
