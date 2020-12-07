package telran.ashkelon2020.lostfound.dto;

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
public class UpdatePostDto {
	String type;
	String sex;
	String breed;
	Address address;
	Location location;
	Set<String> tags;
	List<String> photos;

}
