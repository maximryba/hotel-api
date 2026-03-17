package test.task.hotel_api.repository;


import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import test.task.hotel_api.entity.Contacts;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactsRepository extends JpaRepository<@NonNull Contacts, @NonNull Long> {

}
