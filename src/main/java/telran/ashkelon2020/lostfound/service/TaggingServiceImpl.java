package telran.ashkelon2020.lostfound.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import telran.ashkelon2020.lostfound.dto.colors.ColorsResponseDto;
import telran.ashkelon2020.lostfound.dto.tagging.TaggingResponseDto;



@Service
public class TaggingServiceImpl implements TaggingService {

	@Override
	public List<String> getTagsAndColors(String image_url) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic YWNjX2MzY2Y2YWNhOTMwMGViMzo2MGQ4NTdhMmY3ODg0NGM4ZTMxNTM4MDYwYjhjZmFmMg==");
		
		// tags
		String url = "https://api.imagga.com/v2/tags";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("image_url", image_url)
				.queryParam("language", "en")
				.queryParam("limit", 4);
		RequestEntity<String> request = 
				new RequestEntity<>(headers, HttpMethod.GET, builder.build().toUri());
		ResponseEntity<TaggingResponseDto> response = 
				restTemplate.exchange(request, TaggingResponseDto.class);
		
		List<String> tags = response.getBody().getResult().getTags().stream()
				.flatMap(tagDto -> tagDto.getTag().values().stream())
				.collect(Collectors.toList());
		// colors
		url = "https://api.imagga.com/v2/colors";
		builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("image_url", image_url);
		request = new RequestEntity<>(headers, HttpMethod.GET, builder.build().toUri());
		List<String> colors = restTemplate.exchange(request, ColorsResponseDto.class).getBody().getResult().getColors().getImage_colors().stream()
				.map(o -> o.getClosest_palette_color()).collect(Collectors.toList());
		List<String> tagsAndColors = Stream.concat(tags.stream(), colors.stream())
                .collect(Collectors.toList());
		return tagsAndColors;
	}
}






