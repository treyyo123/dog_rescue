package dog.rescue.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import dog.rescue.entity.Location;

//<T, ID> replaced with <Location, Long> because Long is data type of identity field (primary key)
public interface LocationDao extends JpaRepository<Location, Long> {

}
