package pl.elpepe.quiz.service.dataQuestionsService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.elpepe.quiz.controllers.GameOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuizQuestionsServiceTest {

    @Mock
    QuizQuestionsService quizQuestionsService;


    @Test
    public void shouldGetQuizCategories() throws JsonProcessingException {
        String json;
        json = "{\"trivia_categories\":[{\"id\":9,\"name\":\"General Knowledge\"},{\"id\":10,\"name\":\"Entertainment: Books\"},{\"id\":11,\"name\":\"Entertainment: Film\"},{\"id\":12,\"name\":\"Entertainment: Music\"},{\"id\":13,\"name\":\"Entertainment: Musicals & Theatres\"},{\"id\":14,\"name\":\"Entertainment: Television\"},{\"id\":15,\"name\":\"Entertainment: Video Games\"},{\"id\":16,\"name\":\"Entertainment: Board Games\"},{\"id\":17,\"name\":\"Science & Nature\"},{\"id\":18,\"name\":\"Science: Computers\"},{\"id\":19,\"name\":\"Science: Mathematics\"},{\"id\":20,\"name\":\"Mythology\"},{\"id\":21,\"name\":\"Sports\"},{\"id\":22,\"name\":\"Geography\"},{\"id\":23,\"name\":\"History\"},{\"id\":24,\"name\":\"Politics\"},{\"id\":25,\"name\":\"Art\"},{\"id\":26,\"name\":\"Celebrities\"},{\"id\":27,\"name\":\"Animals\"},{\"id\":28,\"name\":\"Vehicles\"},{\"id\":29,\"name\":\"Entertainment: Comics\"},{\"id\":30,\"name\":\"Science: Gadgets\"},{\"id\":31,\"name\":\"Entertainment: Japanese Anime & Manga\"},{\"id\":32,\"name\":\"Entertainment: Cartoon & Animations\"}]}";

        ObjectMapper objectMapper = new ObjectMapper();
        CategoriesDto categoriesDto = objectMapper.readValue(json, CategoriesDto.class);
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
    public void shouldGetQuizQuestions() throws JsonProcessingException {

        String jsonString = "{\n" +
                "  \"response_code\": 0,\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"type\": \"multiple\",\n" +
                "      \"difficulty\": \"easy\",\n" +
                "      \"category\": \"Sports\",\n" +
                "      \"question\": \"Who is often called \\\"the Maestro\\\" in the men\\u0027s tennis circuit?\",\n" +
                "      \"correct_answer\": \"Roger Federer\",\n" +
                "      \"incorrect_answers\": [\n" +
                "        \"Bill Tilden\",\n" +
                "        \"Boris Becker\",\n" +
                "        \"Pete Sampras\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"multiple\",\n" +
                "      \"difficulty\": \"easy\",\n" +
                "      \"category\": \"Sports\",\n" +
                "      \"question\": \"Who won the UEFA Champions League in 2017?\",\n" +
                "      \"correct_answer\": \"Real Madrid C.F.\",\n" +
                "      \"incorrect_answers\": [\n" +
                "        \"Atletico Madrid\",\n" +
                "        \"AS Monaco FC\",\n" +
                "        \"Juventus F.C.\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"multiple\",\n" +
                "      \"difficulty\": \"easy\",\n" +
                "      \"category\": \"Sports\",\n" +
                "      \"question\": \"What year did the New Orleans Saints win the Super Bowl?\",\n" +
                "      \"correct_answer\": \"2010\",\n" +
                "      \"incorrect_answers\": [\n" +
                "        \"2008\",\n" +
                "        \"2009\",\n" +
                "        \"2011\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"multiple\",\n" +
                "      \"difficulty\": \"easy\",\n" +
                "      \"category\": \"Sports\",\n" +
                "      \"question\": \"Which two teams played in Super Bowl XLII?\",\n" +
                "      \"correct_answer\": \"The New York Giants \\u0026 The New England Patriots\",\n" +
                "      \"incorrect_answers\": [\n" +
                "        \"The Green Bay Packers \\u0026 The Pittsburgh Steelers\",\n" +
                "        \"The Philadelphia Eagles \\u0026 The New England Patriots\",\n" +
                "        \"The Seattle Seahawks \\u0026 The Denver Broncos\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"multiple\",\n" +
                "      \"difficulty\": \"easy\",\n" +
                "      \"category\": \"Sports\",\n" +
                "      \"question\": \"Which driver has been the Formula 1 world champion for a record 7 times?\",\n" +
                "      \"correct_answer\": \"Michael Schumacher\",\n" +
                "      \"incorrect_answers\": [\n" +
                "        \"Ayrton Senna\",\n" +
                "        \"Fernando Alonso\",\n" +
                "        \"Jim Clark\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        QuestionsDto questionsDto = objectMapper.readValue(jsonString, QuestionsDto.class);
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