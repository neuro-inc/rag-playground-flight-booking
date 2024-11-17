/*
 * Copyright 2024-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.spring.demo.ai.playground.services;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * * @author Christian Tzolov
 */
@Service
public class CustomerSupportAssistant {

    private final OllamaChatModel chatClient;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;
    private final static String systemPrompt = """
         Background: Start a new conversation by clearing previous memory.
         Current booking details: Booking number is "", customer first name is "", and last name is "".
         You are a customer chat support agent for an airline named "Funnair".
         Respond in a friendly, helpful, and joyful manner through an online chat system.

        Instructions:
        1. Always gather the following details before providing information about a booking or making changes:
           - Booking number
           - Customer first name
           - Customer last name
           Check the message history for these details before asking the user.
        2. Before making changes or cancellations:
           - Confirm the booking change is allowed under the terms.
           - Notify the user of any charges and get their explicit consent before proceeding.
        3. Use the provided functions for:
           - Fetching booking details
           - Making changes to bookings
           - Cancelling bookings
        4. Confirm user details before making any function calls.
        5. Use parallel function calls if needed.
        6. Do not respond with code.
        7. Do not use random data to call functions.
        Today is %s.
        """;
    private final static String prompt = """
        Context: %s.
        Question: %s.
        Response:
        """;

    public CustomerSupportAssistant(OllamaChatModel chatClient, VectorStore vectorStore, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.chatMemory = chatMemory;
    }

    public String chat(String chatId, String question) {
        initConversation(chatId);
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(question)
                .withTopK(5)
//            .withFilterExpression(new Filter.Expression(
//                    Filter.ExpressionType.EQ, new Filter.Key("category"), new Filter.Value("PUBLIC")
//                )
//            )
        );
        System.out.println("Retrieved " + similarDocuments.size() + " similar docs.");
        System.out.println("Docs: " + similarDocuments);
        String context = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining("\n"));
        return this.chatClient.call(prompt.formatted(context, question));
    }

    private void initConversation(String chatId) {
        List<Message> messages = chatMemory.get(chatId, 100);
        if (messages.isEmpty()) {
            String message = systemPrompt.formatted(LocalDate.now().toString());
            this.chatClient.call(message);
            chatMemory.add(chatId, new SystemMessage(message));
        }
    }
}
