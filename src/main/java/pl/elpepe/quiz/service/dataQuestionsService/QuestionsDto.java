package pl.elpepe.quiz.service.dataQuestionsService;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class QuestionsDto {

    @JsonProperty("response_code")
    private int responseCode;
    private List<QuestionDTO> results;


    @NoArgsConstructor
    @Getter
    @ToString
    public static class QuestionDTO {

        private String category;
        protected String type;
        private String difficulty;
        private String question;

        @JsonProperty("correct_answer")
        private String correctAnswer;

        @JsonProperty("incorrect_answers")
        private List<String> incorrectAnswers;

        public void setQuestion(String question) {
            this.question = HtmlUtils.htmlUnescape(question);
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = HtmlUtils.htmlUnescape(correctAnswer);
        }

        public void setIncorrectAnswers(List<String> incorrectAnswers) {
            this.incorrectAnswers = incorrectAnswers.stream()
                    .map(HtmlUtils::htmlUnescape)
                    .toList();
        }
    }
}
