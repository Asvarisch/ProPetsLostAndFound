package telran.ashkelon2020.lostfound.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import telran.ashkelon2020.lostfound.dto.FilterSearchDto;
import telran.ashkelon2020.lostfound.dto.NewPostDto;
import telran.ashkelon2020.lostfound.dto.PaginationPostsResponseDto;
import telran.ashkelon2020.lostfound.dto.PostResponseDto;
import telran.ashkelon2020.lostfound.dto.UpdatePostDto;

public interface LostFoundService {

	PostResponseDto createLFPost(NewPostDto newPostDto, String login, boolean typePost);

	ResponseEntity<PaginationPostsResponseDto> getLFPosts(int currentPage, int itemsOnPage, boolean typePost);

	ResponseEntity<PostResponseDto> getPostById(String id);

	ResponseEntity<PaginationPostsResponseDto> filterSearchInLFPosts(FilterSearchDto filterSearchDto, int currentPage, int itemsOnPage, boolean typePost);

	PostResponseDto updatePost(UpdatePostDto updatePostDto, String id);

	List<String> getTagsAndColorsOfImage(String image_url);

	PostResponseDto deletePostById(String id);

	List<PostResponseDto> getPostsById(List<String> postIds);
	
	
	
	

	

}
