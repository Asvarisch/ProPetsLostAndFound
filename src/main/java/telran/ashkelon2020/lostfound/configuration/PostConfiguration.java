package telran.ashkelon2020.lostfound.configuration;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.google.maps.GeoApiContext;


@Configuration
public class PostConfiguration {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
					.setMatchingStrategy(MatchingStrategies.STRICT)
					.setFieldMatchingEnabled(true)
					.setPropertyCondition(Conditions.isNotNull())
					.setFieldAccessLevel(AccessLevel.PRIVATE);
		return modelMapper;
	}
	
	@Bean
	public GeoApiContext geoContext() {
		return new GeoApiContext.Builder()
	    .apiKey("AIzaSyCmWgf3jVVveOGgjq07_7EQdsywQI8TGMI")
	    .build();
	}
	
	@Bean
	public RestTemplate	restTemplate() {
		return new RestTemplate();
	}
	
	
	

}
