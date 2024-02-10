package com.example.demo;

import com.example.demo.appuser.AppUserRepository;
import com.example.demo.appuser.AppUserService;
import com.example.demo.appuser.admin.Admin;
import com.example.demo.event.Event;
import com.example.demo.event.EventRepository;
import com.example.demo.event.EventService;
import com.example.demo.team.Team;
import com.example.demo.team.TeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// Disable before running Tests!
	@Bean
	public CommandLineRunner commandLineRunner(AppUserService appUserService,
											   AppUserRepository appUserRepository,
											   TeamRepository teamRepository,
											   EventService eventService,
											   EventRepository eventRepository) {
		return args -> {
			Admin admin = new Admin("System", "obeyMe");
			appUserRepository.save(admin);
			Team team1 = new Team("Barcelona FC", "Barcelona");
			Team team2 = new Team("Real Madrid", "Madrid");
			teamRepository.saveAll(List.of(team1, team2));
			Event event = new Event("UEFA Champions League", team1, team2);
			eventRepository.save(event);
			eventService.startEvent(event.getId());
		};
	}
}
