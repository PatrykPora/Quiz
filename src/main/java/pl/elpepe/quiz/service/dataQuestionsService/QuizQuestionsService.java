package pl.elpepe.quiz.service.dataQuestionsService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.elpepe.quiz.controllers.GameOptions;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class QuizQuestionsService {

    public List<CategoriesDto.CategoryDto> getQuizCategories() {
        RestTemplate restTemplate = new RestTemplate();
        CategoriesDto result = restTemplate.getForObject("https://opentdb.com/api_category.php", CategoriesDto.class);
        List<CategoriesDto.CategoryDto> categories = result.getCategories();
        log.info("categories: {}", categories);
        return categories;
    }

    public List<QuestionsDto.QuestionDTO> getQuizQuestions(GameOptions gameOptions) {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder.fromHttpUrl("https://opentdb.com/api.php")
                .queryParam("amount", gameOptions.getNumberOfQuestions())
                .queryParam("category", gameOptions.getCategoryId())
                .queryParam("difficulty", gameOptions.getDifficulty().name().toLowerCase())
                .build().toUri();
        log.info("uri : {}", uri);

        QuestionsDto result = restTemplate.getForObject(uri, QuestionsDto.class);
        log.info("questions: {}", result.getResults());
        return result.getResults();
    }

}
