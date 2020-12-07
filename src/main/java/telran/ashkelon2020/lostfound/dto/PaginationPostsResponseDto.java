package telran.ashkelon2020.lostfound.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class PaginationPostsResponseDto {
	Integer itemsOnPage;
	Integer currentPage;
	Integer itemsTotal;
	List<PostResponseDto> posts;
	

}
