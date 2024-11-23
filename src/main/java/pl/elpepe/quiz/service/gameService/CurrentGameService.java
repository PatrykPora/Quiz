package pl.elpepe.quiz.service.gameService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.elpepe.quiz.controllers.GameOptions;
import pl.elpepe.quiz.service.dataQuestionsService.QuestionsDto;
import pl.elpepe.quiz.service.dataQuestionsService.QuizQuestionsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
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
            points ++;
        }
        return correct;
    }

    public boolean proceedToNextQuestion() {
        currentQuestionIndex++;
        return currentQuestionIndex < questions.size();
    }
}
