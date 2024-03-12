# AI powered expert system demo

Spring AI re-implementation of https://github.com/marcushellberg/java-ai-playground

This app shows how you can use [Spring AI](https://github.com/spring-projects/spring-ai) to build an AI-powered system that:

- Has access to terms and conditions (retrieval augmented generation, RAG)
- Can access tools (Java methods) to perform actions (Function Calling)
- Uses an LLM to interact with the user

## Requirements

- Java 17+
- OpenAI API key in `OPENAI_API_KEY` environment variable

## Running

Run the app by running `Application.java` in your IDE or `mvn` in the command line.

## Build Jar

```shell
./mvnw clean install -Pproduction
```

```shell
java -jar ./target/playground-flight-booking-0.0.1-SNAPSHOT.jar
```
