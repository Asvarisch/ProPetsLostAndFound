package telran.ashkelon2020.lostfound.dto.colors;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Colorsdto {
	List<ColorsNestedDto> background_colors;
	List<ColorsNestedDto> foreground_colors;
	List<ColorsNestedDto> image_colors;
}
