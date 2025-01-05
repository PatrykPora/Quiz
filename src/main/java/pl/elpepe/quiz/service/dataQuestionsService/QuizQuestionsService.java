package pl.elpepe.quiz.service.dataQuestionsService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.elpepe.quiz.controllers.GameOptions;
import pl.elpepe.quiz.service.exceptions.QuestionProviderServiceException;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class QuizQuestionsService {

    @Retryable(
            value = {QuestionProviderServiceException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public List<CategoriesDto.CategoryDto> getQuizCategories() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            CategoriesDto result = restTemplate.getForObject("https://opentdb.com/api_category.php", CategoriesDto.class);
            List<CategoriesDto.CategoryDto> categories = result.getCategories();
            log.info("categories: {}", categories);
            return categories;
        } catch (Exception e) {
            log.error("failed to get categories");
            throw new QuestionProviderServiceException("failed to fetch categories from provider", e);
        }
    }

//    @Recover
//    public List<CategoriesDto.CategoryDto> recoverGetQuizCategories(QuestionProviderServiceException e) {
//        log.error("failed to fetch categories after retries {} ", e.getMessage());
//        throw new QuestionProviderServiceException("failed to fetch categories after retries", e.getCause());
//    }

    public List<QuestionsDto.QuestionDTO> getQuizQuestions(GameOptions gameOptions) {
        CategoryQuestionCountInfoDto categoryQuestionCountInfoDto = getCategoryQuestionCount(gameOptions.getCategoryId());
        int availableQuestionCount = categoryQuestionCountInfoDto.getQuestionCountForDifficulty(gameOptions.getDifficulty());
        if (availableQuestionCount >= gameOptions.getNumberOfQuestions()) {
            return getQuizQuestions(gameOptions.getNumberOfQuestions(), gameOptions.getCategoryId(), gameOptions.getDifficulty());
        } else {
            return getQuizQuestions(availableQuestionCount, gameOptions.getCategoryId(), gameOptions.getDifficulty());
        }
    }

    @Retryable(
            value = {QuestionProviderServiceException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    private List<QuestionsDto.QuestionDTO> getQuizQuestions(int numberOfQuestions, int categoryId, Difficulty difficulty) {
        try {
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
        } catch (Exception e) {
            log.error("failed to fetch questions");
            throw new QuestionProviderServiceException("failed to fetch questions from provider", e);
        }
    }

//    @Recover
//    public List<QuestionsDto.QuestionDTO> recoverGetQuizQuestions(QuestionProviderServiceException e) {
//        log.error("failed to fetch questions after retries {} ", e.getMessage());
//        throw new QuestionProviderServiceException("failed to fetch questions after retries", e.getCause());
//    }

    @Retryable(
            value = {QuestionProviderServiceException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    private CategoryQuestionCountInfoDto getCategoryQuestionCount(int categoryId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            URI uri = UriComponentsBuilder.fromHttpUrl("https://opentdb.com/api_count.php")
                    .queryParam("category", categoryId)
                    .build().toUri();
            log.info("quiz category question count retrieve URL: {}", uri);
            CategoryQuestionCountInfoDto result = restTemplate.getForObject(uri, CategoryQuestionCountInfoDto.class);
            log.info("quiz category question count retrieve result: {}", result);
            return result;
        } catch (Exception e) {
            log.error("failed to fetch category question count");
            throw new QuestionProviderServiceException("failed to fetch info about number of questions from provider", e);
        }
    }

//    @Recover
//    public CategoryQuestionCountInfoDto recoverGetCategoryQuestionCount(QuestionProviderServiceException e) {
//        log.error("failed to fetch category question count after retries {} ", e.getMessage());
//        throw new QuestionProviderServiceException("failed to fetch info about number of questions from provider", e);
//    }

}
