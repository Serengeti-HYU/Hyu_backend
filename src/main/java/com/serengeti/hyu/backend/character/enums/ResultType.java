package com.serengeti.hyu.backend.character.enums;

public enum ResultType {
    RESULT_1("휴일은 하우스키퍼 유형", "휴일은 하우스키퍼 유형은 주로 휴일 날 시간을 집에서 보내고 집에서 휴식을 취해야 \n" +
            "진정한 휴식이라고 생각하는 유형입니다.\n" +
            "휴일은 하우스키퍼 유형인 00님을 위해 집에서 주로 즐기는 맞춤형 쉼을 추천드릴게요!"),
    RESULT_2("액티브하게 휴식 유형", "액티브하게 휴식 유형은 운동, 모임, 여행 등으로 휴일을 보내며\n" +
            "대화, 몸을 움직이며 에너지를 충전하는 유형입니다.\n" +
            "액티브하게 휴식 유형인 00님을 위해서 활동적인 맞춤형 쉼을 추천드릴게요!"),
    RESULT_3("스트레스 OUT! 힐링 유형", "스트레스 OUT! 힐링 유형은 근무일 동안 쌓인 스트레스를 풀기 위해서\n" +
            "힐링되는 활동으로 휴식을 취하는 유형입니다.\n" +
            "스트레스 OUT! 힐링 유형인 00님을 위해 힐링되는 맞춤형 쉼을 추천드릴게요! "),
    RESULT_4("지겹지 않게 항상 다른 휴식 유형", "지겹지 않게 항상 다른 휴식 유형은 평소 해보지 못한 활동들을 하면서\n" +
            "취미 활동을 찾고 그 과정에서 활력이 채워지는 유형입니다.\n" +
            "지겹지 않게 항상 다른 휴식 유형인 00님을 위해 다양한 휴식들을 추천드릴게요!");

    private final String typeName;
    private final String description;

    ResultType(String typeName, String description) {
        this.typeName = typeName;
        this.description = description;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getDescription() {
        return description;
    }
}

