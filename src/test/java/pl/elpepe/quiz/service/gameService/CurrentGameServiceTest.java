package pl.elpepe.quiz.service.gameService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.elpepe.quiz.controllers.GameOptions;
import pl.elpepe.quiz.service.dataQuestionsService.CategoriesDto;
import pl.elpepe.quiz.service.dataQuestionsService.Difficulty;
import pl.elpepe.quiz.service.dataQuestionsService.QuestionsDto;
import pl.elpepe.quiz.service.dataQuestionsService.QuizQuestionsService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrentGameServiceTest {

    @Mock
    QuizQuestionsService quizQuestionsService;

    @InjectMocks
    CurrentGameService currentGameService;

    private GameOptions gameOptions;
    private List<QuestionsDto.QuestionDTO> mockQuestions;

    @BeforeEach
    public void setUp() throws IOException {
        gameOptions = new GameOptions();
        gameOptions.setNumberOfQuestions(5);
        gameOptions.setDifficulty(Difficulty.EASY);
        gameOptions.setCategoryId(9);

        String jsonFilePath = "src/test/java/pl/elpepe/quiz/service/dataQuestionsService/questions.json";
        String json = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        ObjectMapper objectMapper = new ObjectMapper();
        QuestionsDto questionsDto = objectMapper.readValue(json, QuestionsDto.class);
        mockQuestions = questionsDto.getResults();


        when(quizQuestionsService.getQuizQuestions(gameOptions)).thenReturn(mockQuestions);

        currentGameService.init(gameOptions);
    }

    // Initialize game with valid GameOptions and retrieve questions
    @Test
    public void test_initialize_game_with_valid_options() {
        assertEquals(5, currentGameService.getTotalQuestionNumber());
        assertEquals(1, currentGameService.getCurrentQuestionIndex());
    }

    @Test
    public void shouldGetCurrentQuestionIndex() {
        assertEquals(1, currentGameService.getCurrentQuestionIndex());
    }

    @Test
    public void shouldGetTotalQuestionNumber() {
        assertEquals(5, currentGameService.getTotalQuestionNumber());
    }

    @Test
    public void shouldGetCurrentQuestion() {
        String question = currentGameService.getCurrentQuestion();
        assertEquals(mockQuestions.getFirst().getQuestion(), question);
    }

    @Test
    public void shouldGetCurrentQuestionAnswersInRandomOrder() {
        List<String> currentQuestionAnswersInRandomOrder = currentGameService.getCurrentQuestionAnswersInRandomOrder();
        assertEquals(4, currentQuestionAnswersInRandomOrder.size());
    }

    @Test
    public void shouldCheckAnswerForCurrentQuestionAndUpdatePoints() {
        String userAnswer = "Roger Federer";
        boolean isAnswerChecked = currentGameService.checkAnswerForCurrentQuestionAndUpdatePoints(userAnswer);

        assertTrue(isAnswerChecked);

        // check if points was updated
        int points = currentGameService.getPoints();
        assertEquals(1, points);

    }

    @Test
    public void shouldProceedToNextQuestion() {
        currentGameService.proceedToNextQuestion();
        assertEquals(2, currentGameService.getCurrentQuestionIndex());
    }


    @Test
    public void shouldGetCategoryName() throws IOException {

        String jsonPath = "src/test/java/pl/elpepe/quiz/service/dataQuestionsService/categories.json";
        String jsonString = new String(Files.readAllBytes(Paths.get(jsonPath)));

        ObjectMapper objectMapper = new ObjectMapper();
        CategoriesDto categoriesDto = objectMapper.readValue(jsonString, CategoriesDto.class);
        List<CategoriesDto.CategoryDto> testResult = categoriesDto.getCategories();

        given(quizQuestionsService.getQuizCategories()).willReturn(testResult);

        String categoryName = currentGameService.getCategoryName();

        assertEquals("General Knowledge", categoryName);

    }

}