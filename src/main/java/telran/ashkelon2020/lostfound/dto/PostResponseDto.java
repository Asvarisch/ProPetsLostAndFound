package telran.ashkelon2020.lostfound.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telran.ashkelon2020.lostfound.model.Address;
import telran.ashkelon2020.lostfound.model.Location;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponseDto {
	String id;
	boolean typePost;
	String userLogin;
	String userName;
	String avatar;
	LocalDateTime datePost;
	String type;
	String sex;
	String breed;
	Set<String> tags;
	List<String> photos;
	Address address;
	Location location;
	

}
