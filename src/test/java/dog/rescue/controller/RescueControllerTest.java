package dog.rescue.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import dog.rescue.DogRescueApplication;
import dog.rescue.controller.model.LocationData;

//@SpringBootTest allows spring boot to control the test, classes is the configuration class
//@ActiveProfiles profile to test, allows setup of application-test.yaml
//@Sql tells spring boot to load schema.sql and data.sql before each test. when maven runs app it automatically adds any files in src/main/resources to the classpath, we are telling spring boot to look in classpath for schema and data.

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = DogRescueApplication.class)
@ActiveProfiles("test")
@Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" })
@SqlConfig(encoding = "utf-8")
class RescueControllerTest extends RescueServiceTestSupport {
//most of work is going into superclass aka parent class (RescueServiceTestSupport)

	@Test
	void testInsertLocation() {
		// Given: A location request
		LocationData request = buildInsertLocation(1);
		LocationData expected = buildInsertLocation(1);

		// When: The location is added to the location table
		LocationData actual = insertLocation(request);

		// Then: The location returned is what is expected
		assertThat(actual).isEqualTo(expected);

		// And: There is one row in the location table
		assertThat(rowsInLocationTable()).isOne();

	}

	@Test
	void testRetrieveLocation() {
		// Given: a location

		// must create a location at the beginning of each test because when the
		// database is initialized each time all the tables (except breed) are empty.
		// when buildInsertLocation is called it sets the primary key to 1, then when
		// insertLocation is called it sets primary key to null, but the return value
		// from insertLocation contains the actual primary key. The result is that the
		// "location" variable does have the primary key.

		LocationData location = insertLocation(buildInsertLocation(1));
		LocationData expected = buildInsertLocation(1);

		// When: the location is retrieved by location id
		LocationData actual = retrieveLocation(location.getLocationId());

		// Then: the actual location is equal to the expected location.
		assertThat(actual).isEqualTo(expected);

	}

	@Test
	void testRetrieveAllLocations() {
		// Given: two locations
		List<LocationData> expected = insertTwoLocations();

		// When: all locations are retrieved
		List<LocationData> actual = retrieveAllLocations();

		// Then: the retrieved locations are the same as expected
		assertThat(sorted(actual)).isEqualTo(sorted(expected));

	}

	@Test
	void testUpdateLocation() {
		//Given: a location and an update request
		insertLocation(buildInsertLocation(1));
		LocationData expected = buildUpdateLocation();
		
		//When: the location is updated
		LocationData actual = updateLocation(expected);
		
		//Then: the location is returned as expected 
		assertThat(actual).isEqualTo(expected);
		
		//And: there is one row in the location table
		assertThat(rowsInLocationTable()).isOne();
	}

	@Test
	void deleteLocationWithDogs() {
		//Given: a location and two dogs
		
		//creates location
		LocationData location = insertLocation(buildInsertLocation(1));
		//get location id for LocationData object
		Long locationId = location.getLocationId();
		
		insertDog(1);
		insertDog(2);
		
		assertThat(rowsInLocationTable()).isOne();
		assertThat(rowsInDogTable()).isEqualTo(2);
		assertThat(rowsInDogBreedTable()).isEqualTo(4);
		int breedRows = rowsInBreedTable();
		
		//When: the location is deleted
		deleteLocation(locationId);
		
		//Then: there are no location, dog, or dog_breed rows
		assertThat(rowsInLocationTable()).isZero();
		assertThat(rowsInDogTable()).isZero();
		assertThat(rowsInDogBreedTable()).isZero();
		
		//And: the number of breed rows has not changed.
		assertThat(rowsInBreedTable()).isEqualTo(breedRows);
	}


	

	

	
}
