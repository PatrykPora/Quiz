package pl.elpepe.quiz.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.elpepe.quiz.service.dataQuestionsService.QuizQuestionsService;
import pl.elpepe.quiz.service.gameService.CurrentGameService;

@Slf4j
@Controller
public class FrontendController {


    private final QuizQuestionsService quizQuestionsService;
    private final CurrentGameService currentGameService;

    public FrontendController(QuizQuestionsService quizQuestionsService, CurrentGameService currentGameService) {
        this.quizQuestionsService = quizQuestionsService;
        this.currentGameService = currentGameService;
    }

    @GetMapping("/")
    public String hello(Model model) {
        return "index";
    }

    @GetMapping("/select")
    public String select(Model model) {
        model.addAttribute("gameOptions", new GameOptions());
        model.addAttribute("categories", quizQuestionsService.getQuizCategories());
        return "select";
    }

    @PostMapping("/select")
    public String postSelectForm(Model model, @ModelAttribute GameOptions gameOptions) {
        log.info("player submitted wit options {}", gameOptions);
        currentGameService.init(gameOptions);
        return "redirect:game";
    }

    @GetMapping("/game")
    public String game(Model model) {
        model.addAttribute("userAnswer", new UserAnswer());
        model.addAttribute("currentQuestionNumber", currentGameService.getCurrentQuestionIndex());
        model.addAttribute("totalQuestionNumber", currentGameService.getTotalQuestionNumber());
        model.addAttribute("currentQuestion", currentGameService.getCurrentQuestion());
        model.addAttribute("currentQuestionAnswers", currentGameService.getCurrentQuestionAnswersInRandomOrder());
        return "game";
    }

    @PostMapping("/game")
    public String postSelectForm(Model model, @ModelAttribute UserAnswer userAnswer) {
        currentGameService.checkAnswerForCurrentQuestionAndUpdatePoints(userAnswer.getAnswer());
        boolean hasNextQuestion = currentGameService.proceedToNextQuestion();
        if (hasNextQuestion) {
            return "redirect:game";
        } else {
            return "redirect:summary";
        }
    }

    @GetMapping("/summary")
    public String summary(Model model) {
        model.addAttribute("difficulty", currentGameService.getDifficulty());
        model.addAttribute("categoryName", currentGameService.getCategoryName());
        model.addAttribute("points", currentGameService.getPoints());
        model.addAttribute("maxPoints", currentGameService.getTotalQuestionNumber());
        return "summary";
    }
}
