package telran.ashkelon2020.lostfound.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import telran.ashkelon2020.lostfound.model.Address;
import telran.ashkelon2020.lostfound.model.AddressLocation;
import telran.ashkelon2020.lostfound.model.Location;

@Service
public class GeoLocationServiceImpl implements GeoLocationService {

	@Autowired
	GeoApiContext geoContext;

	@Override
	public AddressLocation getAddressLocationn(Address address, Location location) {
		AddressLocation addressLocation = new AddressLocation();
		if (location.getLatitude() != null && location.getLongitude() != null) {
			LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
			addressLocation = geoCodeReverse(coordinates);
			return addressLocation;
		} else {
			String finalAddress = "";
			if (address.getCountry() != null) {
				finalAddress = finalAddress.concat(address.getCountry());
			}
			if (address.getCity() != null) {
				finalAddress = finalAddress.concat(address.getCity());
			}
			if (address.getStreet() != null) {
				finalAddress = finalAddress.concat(address.getStreet());
			}
			if (address.getBuilding() != null) {
				finalAddress = finalAddress.concat(address.getBuilding());
			}
			addressLocation = geoCode(finalAddress);
			return addressLocation;
		}
	}


	private AddressLocation geoCode(String finalAddress) {
		AddressLocation addressLocation = new AddressLocation();
		try {
			GeocodingResult[] results = GeocodingApi.geocode(geoContext, finalAddress).language("en").await();
			if (results != null) {
				addressLocation = setFinalLocation(results);
			}
		} catch (ApiException | InterruptedException | IOException e) {
			e.printStackTrace();
		}
		return addressLocation;
	}

	private AddressLocation geoCodeReverse(LatLng coordinates) {
		AddressLocation addressLocation = new AddressLocation();
		try {
			GeocodingResult[] results = GeocodingApi.reverseGeocode(geoContext, coordinates).language("en")
					// .locationType(LocationType.APPROXIMATE) // only precise addresses
					.await();
			if (results != null) {
				addressLocation = setFinalLocation(results);
			}
		} catch (ApiException | InterruptedException | IOException e) {
			e.printStackTrace();
		}
		return addressLocation;
	}


	private AddressLocation setFinalLocation(GeocodingResult[] results) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(results[0]));
		AddressLocation addressLocation = new AddressLocation();
		Location location = new Location(results[0].geometry.location.lat, results[0].geometry.location.lng);
		Address address = new Address();
		AddressComponent[] addresscComponents = results[0].addressComponents;
		for (int i = 0; i < addresscComponents.length; i++) {
			if (addresscComponents[i].types[0].toString().equals("country")) {
				address.setCountry(addresscComponents[i].longName);
			}
			if (addresscComponents[i].types[0].toString().equals("locality")) {
				address.setCity(addresscComponents[i].longName);
			}
			if (addresscComponents[i].types[0].toString().equals("route")) {
				address.setStreet(addresscComponents[i].longName);
			}
			if (addresscComponents[i].types[0].toString().equals("street_number")) {
				address.setBuilding(addresscComponents[i].longName);
			}
		}
		addressLocation.setLocation(location);
		addressLocation.setAddress(address);;
		return addressLocation;
	}

}
