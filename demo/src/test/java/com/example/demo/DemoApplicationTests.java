package com.example.demo;

import com.example.demo.models.Player;
import com.example.demo.repositories.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private PlayerRepository playerRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void playerCreationTest() {
		Player actual = new Player("Saba", "Morchilashvili");
		playerRepository.save(actual);
//		Player expected = playerRepository.findById(1L).get();
		playerRepository.deleteById(1L);
		assertThat(playerRepository.findById(1L).get()).isEqualTo(Optional.empty());
	}

	@Test
	void playerDeletionTest() {
		Player actual = new Player("Saba", "Morchilashvili");
		playerRepository.save(actual);
//		Player expected = playerRepository.findById(1L).get();
		playerRepository.deleteById(1L);
		assertThat(playerRepository.findById(1L).get()).isEqualTo(Optional.empty());
	}
}
