package pl.elpepe.quiz.service.dataQuestionsService;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class CategoryQuestionCountInfoDto {

    @JsonProperty("category_id")
    private int categoryId;

    @JsonProperty("category_question_count")
    private CategoryQuestionCountDto categoryQuestionCount;


    @NoArgsConstructor
    @Getter
    @ToString
    private static class CategoryQuestionCountDto {
        @JsonProperty("total_question_count")
        private int totalQuestionCount;

        @JsonProperty("total_easy_question_count")
        private int totalEasyQuestionCount;

        @JsonProperty("total_medium_question_count")
        private int totalMediumQuestionCount;

        @JsonProperty("total_hard_question_count")
        private int totalHardQuestionCount;


    }

    public int getTotalQuestionCount() {
        return categoryQuestionCount.getTotalQuestionCount();
    }

    public int getQuestionCountForDifficulty(Difficulty difficulty) {
        switch (difficulty) {
            case EASY -> {
                return categoryQuestionCount.totalEasyQuestionCount;
            }
            case MEDIUM -> {
                return categoryQuestionCount.totalMediumQuestionCount;
            }
            case HARD -> {
                return categoryQuestionCount.totalHardQuestionCount;
            }
        }
        return 0;
    }

}
