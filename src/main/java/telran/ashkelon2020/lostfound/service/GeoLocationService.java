package telran.ashkelon2020.lostfound.service;
import telran.ashkelon2020.lostfound.model.Address;
import telran.ashkelon2020.lostfound.model.AddressLocation;
import telran.ashkelon2020.lostfound.model.Location;

public interface GeoLocationService {
	
	public AddressLocation getAddressLocationn(Address address, Location location); 


	

}
