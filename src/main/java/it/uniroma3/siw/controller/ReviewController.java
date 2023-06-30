//package it.uniroma3.siw.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import it.uniroma3.siw.model.Review;
//
//@Controller
//public class ReviewController {
//
//	/**
//	 * GET : pagina con la form per inserire un nuovo commento
//	 * @param model
//	 * @return
//	 */
//	@GetMapping(value="/formNewReview")
//	public String formNewReview(Model model) {
//		model.addAttribute("review",new Review()); 
//		return "all/formNewReview.html"; 
//	}
//}
