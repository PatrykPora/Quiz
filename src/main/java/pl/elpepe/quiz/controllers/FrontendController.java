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
        model.addAttribute("pageTitle", "Quiz - game");
        return "index";
    }

    @GetMapping("/select")
    public String select(Model model) {
        try {
            model.addAttribute("pageTitle", "game options");
            model.addAttribute("gameOptions", new GameOptions());
            model.addAttribute("categories", quizQuestionsService.getQuizCategories());
        } catch (Exception e) {
            model.addAttribute("error", "Could not retrieve data, please try again later.");
        }
        return "select";
    }

    @PostMapping("/select")
    public String postSelectForm(Model model, @ModelAttribute GameOptions gameOptions) {
        model.addAttribute("pageTitle", "game");
        log.info("player submitted with options {}", gameOptions);
        try {
            currentGameService.init(gameOptions);
        } catch (Exception e) {
            model.addAttribute("error", "Could not retrieve data, please try again later.");
        }
        return "redirect:game";
    }

    @GetMapping("/game")
    public String game(Model model) {
        try {
            model.addAttribute("pageTitle", "game");
            model.addAttribute("userAnswer", new UserAnswer());
            model.addAttribute("currentQuestionNumber", currentGameService.getCurrentQuestionIndex());
            model.addAttribute("totalQuestionNumber", currentGameService.getTotalQuestionNumber());
            model.addAttribute("currentQuestion", currentGameService.getCurrentQuestion());
            model.addAttribute("currentQuestionAnswers", currentGameService.getCurrentQuestionAnswersInRandomOrder());
        } catch (Exception e) {
            model.addAttribute("error", "Could not retrieve data, please try again later.");
        }
        return "game";
    }

    @PostMapping("/game")
    public String postSelectForm(Model model, @ModelAttribute UserAnswer userAnswer) {
        model.addAttribute("pageTitle", "game");
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
        try {
            model.addAttribute("pageTitle", "summary");
            model.addAttribute("difficulty", currentGameService.getDifficulty());
            model.addAttribute("categoryName", currentGameService.getCategoryName());
            model.addAttribute("points", currentGameService.getPoints());
            model.addAttribute("maxPoints", currentGameService.getTotalQuestionNumber());
        } catch (Exception e) {
            model.addAttribute("error", "Could not retrieve data, please try again later.");
        }
        return "summary";
    }
}
