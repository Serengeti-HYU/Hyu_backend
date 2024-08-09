package com.serengeti.hyu.backend.character.service;

import com.serengeti.hyu.backend.character.enums.Question;
import com.serengeti.hyu.backend.character.enums.ResponseOption;
import com.serengeti.hyu.backend.character.enums.ResultType;

import java.util.Map;

public class ResultMapping {

    private static final Map<Question, Map<ResponseOption, ResultType>> questionResultMap;

    static {
        questionResultMap = Map.of(
                Question.QUESTION_1, Map.of(
                        ResponseOption.STRONGLY_AGREE, ResultType.RESULT_2,
                        ResponseOption.AGREE, ResultType.RESULT_4,
                        ResponseOption.DISAGREE, ResultType.RESULT_3,
                        ResponseOption.STRONGLY_DISAGREE, ResultType.RESULT_1
                ),
                Question.QUESTION_2, Map.of(
                        ResponseOption.STRONGLY_AGREE, ResultType.RESULT_1,
                        ResponseOption.AGREE, ResultType.RESULT_3,
                        ResponseOption.DISAGREE, ResultType.RESULT_4,
                        ResponseOption.STRONGLY_DISAGREE, ResultType.RESULT_2
                ),
                Question.QUESTION_3, Map.of(
                        ResponseOption.STRONGLY_AGREE, ResultType.RESULT_2,
                        ResponseOption.AGREE, ResultType.RESULT_4,
                        ResponseOption.DISAGREE, ResultType.RESULT_3,
                        ResponseOption.STRONGLY_DISAGREE, ResultType.RESULT_1
                ),
                Question.QUESTION_4, Map.of(
                        ResponseOption.STRONGLY_AGREE, ResultType.RESULT_3,
                        ResponseOption.AGREE, ResultType.RESULT_4,
                        ResponseOption.DISAGREE, ResultType.RESULT_1,
                        ResponseOption.STRONGLY_DISAGREE, ResultType.RESULT_2
                ),
                Question.QUESTION_5, Map.of(
                        ResponseOption.STRONGLY_AGREE, ResultType.RESULT_1,
                        ResponseOption.AGREE, ResultType.RESULT_3,
                        ResponseOption.DISAGREE, ResultType.RESULT_4,
                        ResponseOption.STRONGLY_DISAGREE, ResultType.RESULT_2
                ),
                Question.QUESTION_6, Map.of(
                        ResponseOption.STRONGLY_AGREE, ResultType.RESULT_4,
                        ResponseOption.AGREE, ResultType.RESULT_2,
                        ResponseOption.DISAGREE, ResultType.RESULT_1,
                        ResponseOption.STRONGLY_DISAGREE, ResultType.RESULT_3
                ),
                Question.QUESTION_7, Map.of(
                        ResponseOption.STRONGLY_AGREE, ResultType.RESULT_3,
                        ResponseOption.AGREE, ResultType.RESULT_1,
                        ResponseOption.DISAGREE, ResultType.RESULT_4,
                        ResponseOption.STRONGLY_DISAGREE, ResultType.RESULT_2
                ),
                Question.QUESTION_8, Map.of(
                        ResponseOption.STRONGLY_AGREE, ResultType.RESULT_3,
                        ResponseOption.AGREE, ResultType.RESULT_4,
                        ResponseOption.DISAGREE, ResultType.RESULT_2,
                        ResponseOption.STRONGLY_DISAGREE, ResultType.RESULT_1
                ),
                Question.QUESTION_9, Map.of(
                        ResponseOption.STRONGLY_AGREE, ResultType.RESULT_4,
                        ResponseOption.AGREE, ResultType.RESULT_2,
                        ResponseOption.DISAGREE, ResultType.RESULT_3,
                        ResponseOption.STRONGLY_DISAGREE, ResultType.RESULT_1
                ),
                Question.QUESTION_10, Map.of(
                        ResponseOption.STRONGLY_AGREE, ResultType.RESULT_2,
                        ResponseOption.AGREE, ResultType.RESULT_4,
                        ResponseOption.DISAGREE, ResultType.RESULT_3,
                        ResponseOption.STRONGLY_DISAGREE, ResultType.RESULT_1
                )
        );
    }

    public static ResultType getResultType(Question question, ResponseOption response) {
        return questionResultMap.getOrDefault(question, Map.of())
                .getOrDefault(response, ResultType.RESULT_1); // Default
    }
}

