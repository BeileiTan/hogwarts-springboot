package com.springdemo.hogwartsartifactsonline.client.ai.chat.dto;


import java.util.List;

public record ChatRequest(String model,
                          List<Message> messages) {
}
