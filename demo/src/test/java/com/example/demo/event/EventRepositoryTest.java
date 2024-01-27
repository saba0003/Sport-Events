package com.example.demo.event;

import com.example.demo.team.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepo;
    private Team barca, real;

    @BeforeEach
    void setUp() {
        barca = new Team("Barcelona FC", "Barcelona");
        real = new Team("Real Madrid", "Madrid");
    }

    @AfterEach
    void tearDown() {
        eventRepo.deleteAll();
    }

    @Test
    void tryingToInitializeEventWithNullTitleTest() {
        Event event = new Event(null, barca, real);
    }
}
