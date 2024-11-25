package pl.elpepe.quiz.service.dataQuestionsService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.elpepe.quiz.controllers.GameOptions;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuizQuestionsServiceTest {

    @Mock
    QuizQuestionsService quizQuestionsService;


    @Test
    public void shouldGetQuizCategories() throws Exception {

        String jsonPath = "src/test/java/pl/elpepe/quiz/service/dataQuestionsService/categories.json";
        String jsonString = new String(Files.readAllBytes(Paths.get(jsonPath)));

        ObjectMapper objectMapper = new ObjectMapper();
        CategoriesDto categoriesDto = objectMapper.readValue(jsonString, CategoriesDto.class);
        List<CategoriesDto.CategoryDto> testResult = categoriesDto.getCategories();

        given(quizQuestionsService.getQuizCategories()).willReturn(testResult);
        List<CategoriesDto.CategoryDto> result = quizQuestionsService.getQuizCategories();

        assertAll(
                () -> assertEquals(24, result.size()),
                () -> assertEquals("General Knowledge", result.getFirst().getName()),
                () -> assertEquals("Entertainment: Cartoon & Animations", result.getLast().getName())
        );
        verify(quizQuestionsService).getQuizCategories();
    }

    @Test
    public void shouldGetQuizQuestions() throws Exception {

        String jsonFilePath = "src/test/java/pl/elpepe/quiz/service/dataQuestionsService/questions.json";
        String json = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        ObjectMapper objectMapper = new ObjectMapper();
        QuestionsDto questionsDto = objectMapper.readValue(json, QuestionsDto.class);
        List<QuestionsDto.QuestionDTO> testResult = questionsDto.getResults();

        given(quizQuestionsService.getQuizQuestions(new GameOptions())).willReturn(testResult);

        List<QuestionsDto.QuestionDTO> result = quizQuestionsService.getQuizQuestions(new GameOptions());
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(5, result.size()),
                () -> assertEquals("Roger Federer", result.getFirst().getCorrectAnswer())
        );
        verify(quizQuestionsService).getQuizQuestions(new GameOptions());
    }


}