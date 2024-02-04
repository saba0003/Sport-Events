package com.example.demo.event;

import com.example.demo.team.Team;
import com.example.demo.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findEventByTitle(String title);
    @Query("SELECT e FROM Event e WHERE e.team1 = :team OR e.team2 = :team")
    List<Event> findByParticipatingTeam(@Param("team") Team team);
    @Query(value = "SELECT e.* FROM events e WHERE CAST(e.start_date AS DATE) = :startDate", nativeQuery = true)
    List<Event> findByStartingDate(@Param("startDate") LocalDate startDate);
    List<Event> findByStatus(Status status);
}
