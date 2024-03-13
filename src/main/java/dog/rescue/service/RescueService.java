package dog.rescue.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dog.rescue.controller.model.LocationData;
import dog.rescue.dao.LocationDao;
import dog.rescue.entity.Location;

//Tells Spring this is a REST Service
@Service
public class RescueService {

	// instance of...
	@Autowired
	private LocationDao locationDao;

	@Transactional(readOnly = false)
	public LocationData saveLocation(LocationData locationData) {
		// we need to deal w/ an entity so we convert from LocationData back to Location
		Location location = locationData.toLocation();
		// adds primary key to location
		Location dbLocation = locationDao.save(location);

		// takes LocationData constructor that takes a location and turns Location into
		// LocationData
		return new LocationData(dbLocation);
	}

	// "readOnly = true" because we are only reading the returned data, nothing will
	// be changed.
	@Transactional(readOnly = true)
	public LocationData retrieveLocationById(Long locationId) {
		// Setting Location object equal to the value returned by findLocationById
		// method
		Location location = findLocationById(locationId);
		// converts Location object into a LocationData DTO object.
		return new LocationData(location);
	}

	private Location findLocationById(Long locationId) {
		// locationDao is injected bean, findById is a default method that returns an
		// optional. Since optional is returned we need to throw an exception if
		// findById returns a null value.
		return locationDao.findById(locationId)
				.orElseThrow(() -> new NoSuchElementException("Location with ID=" + locationId + " was not found."));
	}

	// readOnly = true because we are just Getting data
	@Transactional(readOnly = true)
	public List<LocationData> retrieveAllLocations() {
		/*
		 * // Created List of Location equal to all locations in LocationDao, and
		 * created a new List of LocationData
		 * 
		 * List<Location> locationEntities = locationDao.findAll(); 
		 * List<LocationData> locationDtos = new LinkedList<>();
		 * 
		 * Sorts two locations by business name
		 * locationEntities.sort((loc1, loc2) -> loc1.getBusinessName().compareTo(loc2.getBusinessName()));
		 * 
		 * Looped thru each location in List of Location and converted them into a
		 * LocationData object. Added converted LocationData objects to List of
		 * LocationData.
		 * 
		 * for (Location location : locationEntities) { 
		 * 	LocationData locationData = new LocationData(location); 
		 * 	locationDtos.add(locationData); }
		 * 
		 * return locationDtos;
		 */

		/*
		 * // locationDao has a list of Location entities. Turn this into a stream, then
		 * turn list of Location entities to a list of LocationData objects. ".sorted"
		 * has a lambda expression that takes two params and sorts loc1 and loc2 based
		 * on business name. 
		 */
		//@formatter:off
		return locationDao.findAll()
				.stream()
				.sorted((loc1, loc2) -> loc1.getBusinessName().compareTo(loc2.getBusinessName()))
				.map(loc -> new LocationData(loc))
				.toList();
		//@formatter:on

		/*//@formatter:off
		return locationDao.findAll()
				.stream()
				.map(LocationData::new)
				.toList();
		//@formatter:on
*/ }
	
	//readOnly = false because we are making changes
	@Transactional(readOnly = false)
	public void deleteLocation(Long locationId) {
		//call findLocationById method and set it to instance of Location
		Location location = findLocationById(locationId);
		
		//call delete method on JPA repository
		locationDao.delete(location);
	}

}
