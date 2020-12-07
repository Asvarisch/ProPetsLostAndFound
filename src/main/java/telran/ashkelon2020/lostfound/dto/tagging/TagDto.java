package telran.ashkelon2020.lostfound.dto.tagging;

import java.util.Map;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TagDto {
	double confidence;
	Map<String, String> tag;

}
