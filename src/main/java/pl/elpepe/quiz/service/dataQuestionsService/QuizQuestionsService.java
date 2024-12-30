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
        CategoryQuestionCountInfoDto categoryQuestionCountInfoDto = getCategoryQuestionCount(gameOptions.getCategoryId());
        int availableQuestionCount = categoryQuestionCountInfoDto.getQuestionCountForDifficulty(gameOptions.getDifficulty());
        if (availableQuestionCount >= gameOptions.getNumberOfQuestions()) {
            return getQuizQuestions(gameOptions.getNumberOfQuestions(), gameOptions.getCategoryId(), gameOptions.getDifficulty());
        } else {
            return getQuizQuestions(availableQuestionCount, gameOptions.getCategoryId(), gameOptions.getDifficulty());
        }
    }

    private List<QuestionsDto.QuestionDTO> getQuizQuestions(int numberOfQuestions, int categoryId, Difficulty difficulty) {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder.fromHttpUrl("https://opentdb.com/api.php")
                .queryParam("amount", numberOfQuestions)
                .queryParam("category", categoryId)
                .queryParam("difficulty", difficulty.name().toLowerCase())
                .build().toUri();
        log.info("uri : {}", uri);

        QuestionsDto result = restTemplate.getForObject(uri, QuestionsDto.class);
        log.info("questions: {}", result.getResults());
        return result.getResults();
    }

    private CategoryQuestionCountInfoDto getCategoryQuestionCount(int categoryId) {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = UriComponentsBuilder.fromHttpUrl("https://opentdb.com/api_count.php")
                .queryParam("category", categoryId)
                .build().toUri();
        log.info("quiz category question count retrieve URL: {}", uri);
        CategoryQuestionCountInfoDto result = restTemplate.getForObject(uri, CategoryQuestionCountInfoDto.class);
        log.info("quiz category question count retrieve result: {}", result);
        return result;
    }

}
