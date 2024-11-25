package pl.elpepe.quiz.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.elpepe.quiz.service.dataQuestionsService.CategoriesDto;
import pl.elpepe.quiz.service.dataQuestionsService.Difficulty;
import pl.elpepe.quiz.service.dataQuestionsService.QuizQuestionsService;
import pl.elpepe.quiz.service.gameService.CurrentGameService;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FrontendController.class)
class FrontendControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    private QuizQuestionsService quizQuestionsService;

    @MockBean
    private CurrentGameService currentGameService;


    @Test
    public void shouldShowIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void shouldShowSelectPage() throws Exception {
        CategoriesDto.CategoryDto categoryDtoOne = new CategoriesDto.CategoryDto();
        CategoriesDto.CategoryDto categoryDtoTwo = new CategoriesDto.CategoryDto();
        when(quizQuestionsService.getQuizCategories())
                .thenReturn(List.of(categoryDtoOne, categoryDtoTwo));

        mockMvc.perform(get("/select"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("select"))
                .andExpect(model().attributeExists("gameOptions"))
                .andExpect(model().attribute("categories", List.of(categoryDtoOne, categoryDtoTwo)));
    }


    @Test
    public void shouldPostSelectFormAndRedirectToGame() throws Exception {
        GameOptions gameOptions = new GameOptions();
        gameOptions.setDifficulty(Difficulty.EASY);
        gameOptions.setCategoryId(9);
        gameOptions.setNumberOfQuestions(4);

        doNothing().when(currentGameService).init(gameOptions);

        mockMvc.perform(post("/select").flashAttr("gameOptions", gameOptions))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:game"));
    }

    @Test
    public void shouldShowGame() throws Exception {
        when(currentGameService.getCurrentQuestionIndex()).thenReturn(1);
        when(currentGameService.getTotalQuestionNumber()).thenReturn(10);
        when(currentGameService.getCurrentQuestion()).thenReturn("question text");
        List<String> questions = List.of("question one", "question two");
        when(currentGameService.getCurrentQuestionAnswersInRandomOrder()).thenReturn(questions);

        mockMvc.perform(get("/game"))
                .andExpect(status().isOk())
                .andExpect(view().name("game"))
                .andExpect(model().attributeExists("userAnswer"))
                .andExpect(model().attribute("currentQuestion", "question text"))
                .andExpect(model().attribute("currentQuestionAnswers", questions));
    }

    @Test
    public void shouldPostAnswerAndGoToNextQuestion() throws Exception {

        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAnswer("test answer");

        when(currentGameService.checkAnswerForCurrentQuestionAndUpdatePoints(userAnswer.getAnswer())).thenReturn(true);
        when(currentGameService.proceedToNextQuestion()).thenReturn(true);

        mockMvc.perform(post("/game").flashAttr("userAnswer", userAnswer))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:game"));
    }


    // post answer for last question and go to summary
    @Test
    public void shouldPostAnswerAndGoToSummary() throws Exception {
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAnswer("test answer");

        when(currentGameService.checkAnswerForCurrentQuestionAndUpdatePoints(userAnswer.getAnswer())).thenReturn(true);
        when(currentGameService.proceedToNextQuestion()).thenReturn(false);

        mockMvc.perform(post("/game").flashAttr("userAnswer", userAnswer))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:summary"));

    }

    @Test
    public void shouldShowSummary() throws Exception {
        when(currentGameService.getDifficulty()).thenReturn(Difficulty.EASY);
        when(currentGameService.getCategoryName()).thenReturn("Sport");
        when(currentGameService.getPoints()).thenReturn(5);
        when(currentGameService.getTotalQuestionNumber()).thenReturn(10);

        mockMvc.perform(get("/summary"))
                .andExpect(status().isOk())
                .andExpect(view().name("summary"))
                .andExpect(model().attribute("difficulty", Difficulty.EASY))
                .andExpect(model().attribute("categoryName", "Sport"))
                .andExpect(model().attribute("points", 5))
                .andExpect(model().attribute("maxPoints", 10));
    }

}