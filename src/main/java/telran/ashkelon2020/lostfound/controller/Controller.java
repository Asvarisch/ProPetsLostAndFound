package telran.ashkelon2020.lostfound.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import telran.ashkelon2020.lostfound.dto.FilterSearchDto;
import telran.ashkelon2020.lostfound.dto.NewPostDto;
import telran.ashkelon2020.lostfound.dto.PaginationPostsResponseDto;
import telran.ashkelon2020.lostfound.dto.PostResponseDto;
import telran.ashkelon2020.lostfound.dto.UpdatePostDto;
import telran.ashkelon2020.lostfound.service.LostFoundService;

@RestController
@RequestMapping("/lostfound/en/v1")
public class Controller {

	@Autowired
	LostFoundService lostFoundService;

	// NEW LOST PET
	@PostMapping("/lost/{login}")
	public PostResponseDto createLostPetPost(@RequestBody NewPostDto newPostDto, @PathVariable String login) {
		return lostFoundService.createLFPost(newPostDto, login, false);
	}

	// NEW FOUND PET
	@PostMapping("/found/{login}")
	public PostResponseDto createFoundPetPost(@RequestBody NewPostDto newPostDto, @PathVariable String login) {
		return lostFoundService.createLFPost(newPostDto, login, true);
	}
	
	// GET POSTS OF LOST PETs
	@GetMapping("/losts")
	public ResponseEntity<PaginationPostsResponseDto> getLostPosts(@RequestParam("currentPage") int currentPage, @RequestParam("itemsOnPage") int itemsOnPage) {
		return lostFoundService.getLFPosts(currentPage, itemsOnPage, false);
	}

	// GET POSTS OF FOUND PETs
	@GetMapping("/founds")
	public ResponseEntity<PaginationPostsResponseDto> getFoundPosts(@RequestParam("currentPage") int currentPage, @RequestParam("itemsOnPage") int itemsOnPage) {
		return lostFoundService.getLFPosts(currentPage, itemsOnPage, true);
	}

	// GET POST BY ID
	@GetMapping("/{id}")
	public ResponseEntity<PostResponseDto> getPostById(@PathVariable String id) {
		return lostFoundService.getPostById(id);
	}
	
	//SEARCH BY INFORMATION OF FOUND PET
	@PostMapping("/founds/filter")
	public ResponseEntity<PaginationPostsResponseDto> filterSearchInFound(@RequestBody FilterSearchDto filterSearchDto, @RequestParam("currentPage") int currentPage, @RequestParam("itemsOnPage") int itemsOnPage) {
		return lostFoundService.filterSearchInLFPosts(filterSearchDto, currentPage, itemsOnPage, true);
	}
	
	// SEARCH BY INFORMATION OF LOST PET
	@PostMapping("/lost/filter")
	public ResponseEntity<PaginationPostsResponseDto> filterSearchInLost(@RequestBody FilterSearchDto filterSearchDto, @RequestParam("currentPage") int currentPage, @RequestParam("itemsOnPage") int itemsOnPage) {
		return lostFoundService.filterSearchInLFPosts(filterSearchDto, currentPage, itemsOnPage, false);
	}
	
	
	// UPDATE POST
	@PutMapping("/{id}")
	public PostResponseDto updatePost(@RequestBody UpdatePostDto updatePostDto, @PathVariable String id){
		return lostFoundService.updatePost(updatePostDto, id);
	}
	
	// TAGS AND COLORS OF PICTURE ---> FIXME requestParam or pathVariable?
	@GetMapping("/tagscolors")
	public List<String> getTagsAndColorsOfImage(@RequestParam String image_url) {
		return lostFoundService.getTagsAndColorsOfImage(image_url);
	}
	
	// DELETE POST BY ID
	@DeleteMapping("/{id}")
	public PostResponseDto deletePostById(@PathVariable String id) {
		return lostFoundService.deletePostById(id);
	}
	
	// GET USER DATA -> LIST ID
	@PostMapping("/userdata")
	public List<PostResponseDto> getUserData(@RequestBody List<String> postIds) {
		return lostFoundService.getPostsById(postIds);
	}
}
