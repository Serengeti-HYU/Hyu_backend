package com.serengeti.hyu.backend.character.enums;

public enum Question {
    QUESTION_1("휴일이 생기면 바로 약속을 잡는다."),
    QUESTION_2("휴일에는 근무일에 비해 연락 확인 빈도가 줄어든다."),
    QUESTION_3("운동, 모임 등 사람들과 함께 휴식을 즐긴다."),
    QUESTION_4("휴일에도 업무를 하는 경우가 많다."),
    QUESTION_5("외출을 하면 제대로 쉬었다는 기분이 들지 않는다."),
    QUESTION_6("평소 원데이 클래스를 즐긴다."),
    QUESTION_7("남들보다 조용한 취미를 갖고있다."),
    QUESTION_8("휴일에 도시보다 자연을 찾는다."),
    QUESTION_9("휴일에는 평소 못 해본 경험을 찾는다."),
    QUESTION_10("집에 혼자 있을 때 외로움을 잘 느낀다.");

    private final String questionText;

    Question(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionText() {
        return questionText;
    }

}
