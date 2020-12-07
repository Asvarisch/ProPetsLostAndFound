package telran.ashkelon2020.lostfound.dto;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import telran.ashkelon2020.lostfound.model.Address;
import telran.ashkelon2020.lostfound.model.Location;

@Getter
@Setter
public class FilterSearchDto {
	String type;
	String breed;
	Set<String> tags;
	Address address;
	Location location;

}
