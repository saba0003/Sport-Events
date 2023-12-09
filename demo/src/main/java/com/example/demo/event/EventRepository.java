package com.example.demo.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findEventByName(String name);
    @Query("SELECT e FROM Event e WHERE DATE(e.startDate) = DATE(:startDate)")
    List<Event> findByStartingDate(@Param("startDate") LocalDate startDate);
}
