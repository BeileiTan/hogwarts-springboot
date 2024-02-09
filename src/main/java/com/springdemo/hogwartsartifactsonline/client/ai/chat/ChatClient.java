package com.springdemo.hogwartsartifactsonline.client.ai.chat;

import com.springdemo.hogwartsartifactsonline.client.ai.chat.dto.ChatRequest;
import com.springdemo.hogwartsartifactsonline.client.ai.chat.dto.ChatResponse;

public interface ChatClient {
    ChatResponse generate(ChatRequest chatRequest);
}
