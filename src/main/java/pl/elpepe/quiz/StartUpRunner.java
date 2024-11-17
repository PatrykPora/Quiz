package pl.elpepe.quiz;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.elpepe.quiz.database.entities.PlayerEntity;
import pl.elpepe.quiz.database.repositories.PlayerRepository;

import java.util.List;

@Component
@Slf4j
public class StartUpRunner implements CommandLineRunner {

    @Autowired
    private PlayerRepository playerRepo;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting up");

        playerRepo.save(new PlayerEntity("pawel"));
        playerRepo.save(new PlayerEntity("Gawel"));
        playerRepo.save(new PlayerEntity("Romek"));

        List<PlayerEntity> playersFromDB = playerRepo.findAll();

        for (PlayerEntity playerDB : playersFromDB) {
            log.info("Player found : {}", playerDB);
        }
    }
}
