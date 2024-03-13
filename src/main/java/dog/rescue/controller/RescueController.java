package dog.rescue.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dog.rescue.controller.model.LocationData;
import dog.rescue.service.RescueService;
import lombok.extern.slf4j.Slf4j;

//Tells Spring this is a REST controller
@RestController
//Tells URI starts w/ /dog_rescue
@RequestMapping("/dog_rescue")
@Slf4j
public class RescueController {
	// instance of...
	@Autowired
	private RescueService rescueService;

	// POST to uri /dog_rescue/location
	@PostMapping("/location")
	@ResponseStatus(code = HttpStatus.CREATED)
	// @RequestBody tells Spring that LocationData is in the Request Body
	public LocationData createLocation(@RequestBody LocationData locationData) {
		// {} replaced by value in specified variable (locationData)
		log.info("Creating location {}", locationData);

		return rescueService.saveLocation(locationData);
	}

	// @PutMapping tells Spring this is an "update" request to uri
	// "dog_rescue/location/{variable id in braces}. @RequestBody gets LocationData
	// from JSON that is in request payload. Response status defaults to 200(OK) so
	// no @ResponseStatus annotation is necessary.
	@PutMapping("/location/{locationId}")
	public LocationData updateLocation(@PathVariable Long locationId, @RequestBody LocationData locationData) {
		locationData.setLocationId(locationId);
		log.info("Updating location {}", locationData);

		return rescueService.saveLocation(locationData);
	}

	// @PathVariable indicates locationId is coming in on url, @GetMapping tells
	// Spring this is a GET request to the specified uri. {} indicates this is a
	// variable path/replaceable parameter.
	@GetMapping("/location/{locationId}")
	public LocationData retrieveLocation(@PathVariable Long locationId) {
		// {} is replaced with the value of parameter locationId.
		log.info("Retrieving location with ID={}", locationId);

		return rescueService.retrieveLocationById(locationId);
	}

	@GetMapping("/location")
	public List<LocationData> retrieveAllLocations() {
		log.info("Retrieving all locations.");

		return rescueService.retrieveAllLocations();
	}

	// delete doesn't really return anything so will return exception or in this
	// case a Map to return a message. jackson will turn Map into JSON
	@DeleteMapping("/location/{locationId}")
	public Map<String, String> deleteLocation(@PathVariable Long locationId) {
		log.info("Deleting location with ID=" + locationId + ".");
		
		//call the deleteLocation method in the RescueService 
		rescueService.deleteLocation(locationId);
		
		//if no exception is thrown, return Map of message
		return Map.of("message", "Location with ID=" + locationId + " was deleted successfully");
	}
	
}// end of class
