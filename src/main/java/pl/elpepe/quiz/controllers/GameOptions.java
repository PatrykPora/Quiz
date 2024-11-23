package pl.elpepe.quiz.controllers;


import lombok.Data;
import pl.elpepe.quiz.service.dataQuestionsService.Difficulty;

@Data
public class GameOptions {

    private int numberOfQuestions;
    private Difficulty difficulty;
    private int categoryId;

}
