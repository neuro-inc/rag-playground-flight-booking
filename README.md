# AI powered expert system demo
1. `$ git clone` this repo && `$ cd` into root of it.
2.  Create all defined volumes `$ apolo-flow mkvolumes`
3.  Upload docs `$ apolo-flow upload docs` if we don't use `${{ upload(volumes.docs).ref_ro }}` in the `live.yml`
4.  Build image for web app with `$ apolo-flow build pfb`
5. `$ apolo-flow run pgvector` -- start postgresql.
6. `$ apolo-flow run ollama` -- start Ollama with VLLM server under the hood
7. `$ apolo-flow run pfb` -- start application.

# Query examples
```
Get booking details - booking number: 102, first: Jane, last: Smith
ChangeBooking -  102, Jane, Smith, 2025-11-25, from: FUN, to: FUN
CancelBooking -  booking number: 102, first: Jane, last: Smith
```

# Run ollama locally:
1. Run ollama in docker https://hub.docker.com/r/ollama/ollama
2. Run qwen2:1.5b model qwen2 with 1.5b parameters locally - ```docker exec -it ollama ollama run qwen2:1.5b```

# Forward ollama:
```bash
apolo job port-forward {job name} {port}:{port}
apolo job port-forward playground-flight-booking-ollama 11434:11434
```

# Forward DB:
```bash
apolo job port-forward playground-flight-booking-pgvector 5432:5432
psql -h 0.0.0.0 -U postgres -d postgres
```

# Rest http call:
```
POST http://localhost:11434/api/generate
Accept: application/json

{
"model": "llama3.2:1b",
"prompt": "What color is the sky at different times of the day? Respond using JSON",
"format": "json",
"stream": false
}
```

Spring AI re-implementation of https://github.com/marcushellberg/java-ai-playground

This app shows how you can use [Spring AI](https://github.com/spring-projects/spring-ai) to build an AI-powered system that:

- Has access to terms and conditions (retrieval augmented generation, RAG)
- Can access tools (Java methods) to perform actions (Function Calling)
- Uses an LLM to interact with the user

![alt text](diagram.jpg)

## Requirements

- Java 17+
- OpenAI API key in `OPENAI_API_KEY` environment variable

## Running

Run the app by running `Application.java` in your IDE or `mvn` in the command line.

### With OpenAI Chat

Add to the POM the Spring AI Open AI boot starter:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
</dependency>
```

Add the OpenAI configuraiton to the `applicaiton.properties`:

```
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o
```

### WIth VertexAI Geminie Chat

Add to the POM the Spring AI VertexAI Gemeni and Onnx Transfomer Embedding boot starters:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-vertex-ai-gemini-spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-transformers-spring-boot-starter</artifactId>
</dependency>
```

Add the VertexAI Gemini configuraiton to the `applicaiton.properties`:

```
spring.ai.vertex.ai.gemini.project-id=${VERTEX_AI_GEMINI_PROJECT_ID}
spring.ai.vertex.ai.gemini.location=${VERTEX_AI_GEMINI_LOCATION}
spring.ai.vertex.ai.gemini.chat.options.model=gemini-1.5-pro-001
# spring.ai.vertex.ai.gemini.chat.options.model=gemini-1.5-flash-001
```

### With Azure OpenAI Chat

Add to the POM the Spring AI Azure OpenAI boot starter:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-azure-openai-spring-boot-starter</artifactId>
</dependency>
```

Add the Azure OpenAI configuraiton to the `applicaiton.properties`:

```
spring.ai.azure.openai.api-key=${AZURE_OPENAI_API_KEY}
spring.ai.azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT}
spring.ai.azure.openai.chat.options.deployment-name=gpt-4o
```

### With Groq Chat

It reuses the OpenAI Chat client but ponted to the Groq endpont

Add to the POM the Spring AI Open AI boot starter:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-transformers-spring-boot-starter</artifactId>
</dependency>
```

Add the Groq configuraiton to the `applicaiton.properties`:

```
spring.ai.openai.api-key=${GROQ_API_KEY}
spring.ai.openai.base-url=https://api.groq.com/openai
spring.ai.openai.chat.options.model=llama3-70b-8192
```

### With Anthropic Claude 3 Chat

Add to the POM the Spring AI Anthropic Claude and Onnx Transfomer Embedding boot starters:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-anthropic-spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-transformers-spring-boot-starter</artifactId>
</dependency>
```

Add the Anthropic configuraiton to the `applicaiton.properties`:

```
spring.ai.anthropic.api-key=${ANTHROPIC_API_KEY}
spring.ai.openai.chat.options.model=llama3-70b-8192
spring.ai.anthropic.chat.options.model=claude-3-5-sonnet-20240620
```


## Build Jar

```shell
./mvnw clean install -Pproduction
```

```shell
java -jar ./target/playground-flight-booking-0.0.1-SNAPSHOT.jar
```


```
docker run -it --rm --name postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres ankane/pgvector
```
