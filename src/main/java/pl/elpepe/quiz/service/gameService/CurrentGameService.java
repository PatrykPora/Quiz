package pl.elpepe.quiz.service.gameService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import pl.elpepe.quiz.controllers.GameOptions;
import pl.elpepe.quiz.service.dataQuestionsService.Difficulty;
import pl.elpepe.quiz.service.dataQuestionsService.QuestionsDto;
import pl.elpepe.quiz.service.dataQuestionsService.QuizQuestionsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@SessionScope
public class CurrentGameService {


    private GameOptions gameOptions;
    private int currentQuestionIndex;

    @Getter
    private int points;

    private List<QuestionsDto.QuestionDTO> questions;

    @Autowired
    private QuizQuestionsService quizQuestionsService;

    public void init(GameOptions gameOptions) {
        this.gameOptions = gameOptions;
        this.currentQuestionIndex = 0;
        this.points = 0;

        this.questions = quizQuestionsService.getQuizQuestions(gameOptions);
    }


    public int getCurrentQuestionIndex() {
        return currentQuestionIndex + 1;
    }

    public int getTotalQuestionNumber() {
        return questions.size();
    }

    public String getCurrentQuestion() {
        QuestionsDto.QuestionDTO dto = questions.get(currentQuestionIndex);
        return dto.getQuestion();
    }

    public List<String> getCurrentQuestionAnswersInRandomOrder() {
        QuestionsDto.QuestionDTO dto = questions.get(currentQuestionIndex);
        List<String> answers = new ArrayList<>();
        answers.add(dto.getCorrectAnswer());
        answers.addAll(dto.getIncorrectAnswers());
        Collections.shuffle(answers);
        return answers;
    }

    public boolean checkAnswerForCurrentQuestionAndUpdatePoints(String userAnswer) {
        QuestionsDto.QuestionDTO dto = questions.get(currentQuestionIndex);
        boolean correct = dto.getCorrectAnswer().equals(userAnswer);
        if (correct) {
            points++;
        }
        return correct;
    }

    public boolean proceedToNextQuestion() {
        currentQuestionIndex++;
        return currentQuestionIndex < questions.size();
    }

    public Difficulty getDifficulty() {
        return gameOptions.getDifficulty();
    }

    public String getCategoryName() {
        Optional<String> category = quizQuestionsService.getQuizCategories().stream()
                .filter(categoryDto -> categoryDto.getId() == gameOptions.getCategoryId())
                .map(categoryDto -> categoryDto.getName())
                .findAny();
        return category.orElse(null);
    }
}
