package pl.elpepe.quiz.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.elpepe.quiz.service.dataQuestionsService.QuizQuestionsService;

@Slf4j
@Controller
public class FrontendController {

    @Autowired
    private QuizQuestionsService quizQuestionsService;

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
        return "index";
    }


}
