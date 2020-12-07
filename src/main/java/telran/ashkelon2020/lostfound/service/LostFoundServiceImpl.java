package telran.ashkelon2020.lostfound.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.ashkelon2020.lostfound.dao.PostRepository;
import telran.ashkelon2020.lostfound.dto.FilterSearchDto;
import telran.ashkelon2020.lostfound.dto.KafkaPostDto;
import telran.ashkelon2020.lostfound.dto.NewPostDto;
import telran.ashkelon2020.lostfound.dto.PaginationPostsResponseDto;
import telran.ashkelon2020.lostfound.dto.PostResponseDto;
import telran.ashkelon2020.lostfound.dto.UpdatePostDto;
import telran.ashkelon2020.lostfound.dto.exceptions.PostNotFoundException;
import telran.ashkelon2020.lostfound.model.AddressLocation;
import telran.ashkelon2020.lostfound.model.Post;

@Service
public class LostFoundServiceImpl implements LostFoundService {
    String topic = "ir4jztza-lostfound";

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	PostRepository repository;
	
	@Autowired
	GeoLocationService geoService;
	
	@Autowired
	TaggingService taggingService;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	@Transactional
	public PostResponseDto createLFPost(NewPostDto newPostDto, String login, boolean typePost) {
		AddressLocation addressLocation = geoService.getAddressLocationn(newPostDto.getAddress(), newPostDto.getLocation());
		Post post = modelMapper.map(newPostDto, Post.class);
		post.setTypePost(typePost);
		post.setUserLogin(login);
		post.setDatePost(LocalDateTime.now());
		post.setAddress(addressLocation.getAddress());
		post.setLocation(addressLocation.getLocation());
		repository.save(post);
		PostResponseDto postResponseDto = modelMapper.map(post, PostResponseDto.class);
		sendPostToKarafka(post);
		//FIXME --> send info to activities service
		return postResponseDto;
	}

	private void sendPostToKarafka(Post post) {
		KafkaPostDto kafkaPost = modelMapper.map(post, KafkaPostDto.class);
		String postJson = "";
		try {
			postJson = mapper.writeValueAsString(kafkaPost);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		kafkaTemplate.send(topic, postJson);
	}

	@Override
	public ResponseEntity<PaginationPostsResponseDto> getLFPosts(int currentPage, int itemsOnPage, boolean typePost) {
		Pageable paging = PageRequest.of(currentPage, itemsOnPage);
		Page<Post> page = repository.findByIdNotNullAndTypePost(typePost, paging);
		return createPaginationPostsResponseDto(page, currentPage, itemsOnPage);
	}


	@Override
	public ResponseEntity<PostResponseDto> getPostById(String id) {
		Post post = repository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
		PostResponseDto postresponse = modelMapper.map(post, PostResponseDto.class);
		return new ResponseEntity<PostResponseDto>(postresponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PaginationPostsResponseDto> filterSearchInLFPosts(FilterSearchDto filterSearchDto,
			int currentPage, int itemsOnPage, boolean typePost) {
		Pageable paging = PageRequest.of(currentPage, itemsOnPage);
		AddressLocation addressLocation = geoService.getAddressLocationn(filterSearchDto.getAddress(), filterSearchDto.getLocation());
		
		Double[] point = new Double[] {addressLocation.getLocation().getLatitude(), addressLocation.getLocation().getLongitude()};
		Integer maxDistance = 2000;
		Page<Post> pageOfPosts = repository.filterSearch(typePost, filterSearchDto.getType(), filterSearchDto.getBreed(), filterSearchDto.getTags(), point, maxDistance, paging);
		
//		returning object Page
//		return new PageImpl<PostResponseDto>(pageOfPosts.getContent().stream()
//				.map(p -> modelMapper.map(p, PostResponseDto.class))
//				.collect(Collectors.toList()));
		return createPaginationPostsResponseDto(pageOfPosts, currentPage, itemsOnPage);
		
	}
	
	private ResponseEntity<PaginationPostsResponseDto> createPaginationPostsResponseDto(Page<Post> page,
			int currentPage, int itemsOnPage) {
		PaginationPostsResponseDto paginationResponse = new PaginationPostsResponseDto();
		paginationResponse.setCurrentPage(currentPage);
		paginationResponse.setItemsOnPage(itemsOnPage);
		paginationResponse.setItemsTotal((int) page.getTotalElements());
		//Page<PostResponseDto> pageOfPostResponse = page.map(p -> modelMapper.map(p, PostResponseDto.class));
		List<PostResponseDto> posts = page.getContent().stream()
				.map(p -> modelMapper.map(p, PostResponseDto.class))
				.collect(Collectors.toList());
		paginationResponse.setPosts(posts);
		return new ResponseEntity<PaginationPostsResponseDto>(paginationResponse, HttpStatus.OK);
	}

	@Override
	public PostResponseDto updatePost(UpdatePostDto updatePostDto, String id) {
		Post post = repository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
		AddressLocation addressLocation = geoService.getAddressLocationn(updatePostDto.getAddress(), updatePostDto.getLocation());
		post.setAddress(addressLocation.getAddress());
		post.setLocation(addressLocation.getLocation());
		post.setType(updatePostDto.getType());
		post.setSex(updatePostDto.getSex());
		post.setBreed(updatePostDto.getBreed());
		post.setTags(updatePostDto.getTags());
		post.setPhotos(updatePostDto.getPhotos());
		repository.save(post);
		PostResponseDto postResponseDto = modelMapper.map(post, PostResponseDto.class);
		return postResponseDto;
	}

	@Override
	public List<String> getTagsAndColorsOfImage(String image_url) {
		return taggingService.getTagsAndColors(image_url);
	}

	@Override
	public PostResponseDto deletePostById(String id) {
		Post post = repository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
		repository.deleteById(id);
		return modelMapper.map(post, PostResponseDto.class);
	}

	@Override
	public List<PostResponseDto> getPostsById(List<String> postIds) {
		List<Post> posts = repository.findByIdIn(postIds);
		return posts.stream()
				.map(p -> modelMapper.map(p, PostResponseDto.class))
				.collect(Collectors.toList());
	}
	
}
