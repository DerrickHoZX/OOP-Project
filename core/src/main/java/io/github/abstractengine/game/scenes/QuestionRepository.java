package io.github.abstractengine.game.scenes;

import java.util.List;

/**
 * Abstraction for obtaining question sets.
 * Allows switching from hardcoded questions to data files without changing scene logic.
 */
public interface QuestionRepository {
    List<QuestionBank.Question> getLanguageQuestions();

    List<QuestionBank.Question> getCategoryQuestions();
}

