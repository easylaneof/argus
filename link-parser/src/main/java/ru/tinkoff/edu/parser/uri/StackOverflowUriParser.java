package ru.tinkoff.edu.parser.uri;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.parser.ParsingResult;

@Component
public class StackOverflowUriParser extends AbstractUriParser {
    private static final String STACK_OVERFLOW_HOST = "stackoverflow.com";
    private static final String QUESTIONS = "questions";

    public StackOverflowUriParser() {
        super(STACK_OVERFLOW_HOST);
    }

    @Override
    protected ParsingResult parseImpl(String[] pathParts) {
        if (pathParts.length != 3) {
            return null;
        }

        if (!QUESTIONS.equals(pathParts[0])) {
            return null;
        }

        return new ParsingResult.StackOverflowQuestion(pathParts[1]);
    }
}
