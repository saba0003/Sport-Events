package com.example.demo;

import com.example.demo.models.Event;
import com.example.demo.models.Team;
import com.example.demo.repositories.AppUserRepository;
import com.example.demo.services.implementations.AppUserServiceImpl;
import com.example.demo.models.admin.Admin;
import com.example.demo.repositories.EventRepository;
import com.example.demo.services.implementations.EventServiceImpl;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.services.implementations.TeamServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// Disable before running Tests!
//	@Bean
//	public CommandLineRunner commandLineRunner(AppUserServiceImpl appUserService,
//											   AppUserRepository appUserRepository,
//											   TeamRepository teamRepository,
//											   TeamServiceImpl teamService,
//											   EventServiceImpl eventService,
//											   EventRepository eventRepository) {
//		return args -> {
//			Admin admin = new Admin("System", "obeyMe");
//			appUserService.addNewUser(admin);
////			Team team1 = new Team("Barcelona FC", "Barcelona");
////			Team team2 = new Team("Real Madrid", "Madrid");
////			teamService.addNewTeam(team1);
////			teamService.addNewTeam(team2);
////			Event event = new Event("UEFA Champions League", team1, team2);
////			eventService.addNewEvent(event);
////			eventService.startEvent(event.getId());
//		};
//	}
}
