package test.task.hotel_api.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import test.task.hotel_api.entity.ArrivalTime;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ArrivalTimeRepository extends JpaRepository<@NonNull ArrivalTime, @NonNull Long> {

}
