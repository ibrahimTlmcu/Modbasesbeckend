package com.example.backend.repositories;



import com.example.backend.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByTicketId(String ticketId);
}
