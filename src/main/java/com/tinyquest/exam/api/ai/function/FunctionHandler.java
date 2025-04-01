package com.tinyquest.exam.api.ai.function;

public interface FunctionHandler {

    /**
     * GPT가 호출할 함수의 이름 (예: getWeather)
     */
    String getName();

    /**
     * 함수 실행 로직 (arguments는 JSON 문자열)
     */
    String invoke(String argumentsJson);

    /**
     * OpenAI API에 제공할 함수 스키마 (function schema)
     */
    Object getFunctionSchema();

}
