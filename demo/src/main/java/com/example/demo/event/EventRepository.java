package com.example.demo.event;

import com.example.demo.team.Team;
import com.example.demo.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findEventByTitle(String title);
    @Query("SELECT e FROM Event e WHERE e.team1 = :team OR e.team2 = :team")
    List<Event> findByParticipatingTeam(@Param("team") Team team);
    @Query("SELECT e FROM Event e WHERE DATE(e.startDate) = DATE(:startDate)")
    List<Event> findByStartingDate(@Param("startDate") LocalDate startDate);
    List<Event> findByStatus(Status status);
}
