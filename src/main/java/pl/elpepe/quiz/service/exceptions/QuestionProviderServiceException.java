package pl.elpepe.quiz.service.exceptions;

public class QuestionProviderServiceException extends RuntimeException {
    public QuestionProviderServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
