# 스프링 ai 에이전트 개발

## 개발환경
- Intellij
- Java, JDK 21 (필수)
- Docker, Docker Compose (필수)

## OpenAI API Key 등록
- vm 옵션 : -DOPENAI-API-KEY=[여기에 키를 넣어주세요.]
![image](https://github.com/user-attachments/assets/1ae6a398-2c40-4156-b726-e7ecd8c41951)

## 배경
- 스프링 AI로 만들었었는데, OpenAI에서 refusal, annotation 등 속성을 추가하니까 에러가 나오고 커스텀이 잘 안됨..
- 그래서 한번 만들어보고 싶었음.

## 추후 과제
- 인증 (Spring Security)을 통한 사용자별 히스토리 관리
- VectorDB 활용한 RAG 구현, 지금은 Mock 수준..
- 테스트 코드 구현
