package pl.elpepe.quiz;


import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.elpepe.quiz.database.entities.PlayerEntity;

import java.util.List;

@Component
@Slf4j
public class StartUpRunner implements CommandLineRunner {

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting up");

        PlayerEntity player = new PlayerEntity("Paweł");
        log.info("Player created : {}", player);
        entityManager.persist(player);
        log.info("Player after being persisted with auto generated id : {}", player);

        List<PlayerEntity> playersFromDB = entityManager.createQuery("select p from PLAYERS p").getResultList();

        for (PlayerEntity playerFromDB : playersFromDB) {
            log.info("Player found : {}", playerFromDB);
        }
    }
}
