package com.springdemo.hogwartsartifactsonline.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springdemo.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.springdemo.hogwartsartifactsonline.client.ai.chat.ChatClient;
import com.springdemo.hogwartsartifactsonline.client.ai.chat.dto.ChatRequest;
import com.springdemo.hogwartsartifactsonline.client.ai.chat.dto.ChatResponse;
import com.springdemo.hogwartsartifactsonline.client.ai.chat.dto.Message;
import com.springdemo.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import io.micrometer.core.annotation.Timed;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArtifactService {
    //injection from repository to service
    private final ArtifactRepository artifactRepository;

    private final IdWorker idWorker;

    private final ChatClient chatClient;

    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker, ChatClient chatClient) {
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
        this.chatClient = chatClient;
    }

    @Observed(name = "artifact", contextualName = "findByIdService")
    public Artifact findById(String artifactId){
        return this.artifactRepository.findById(artifactId).orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
    }

    @Timed("findAllArtifactsService.time")
    public List<Artifact> findAll() {
        return this.artifactRepository.findAll();
    }

    public Artifact save(Artifact newArtifact){
        newArtifact.setId(idWorker.nextId() + "");
        return this.artifactRepository.save(newArtifact);
    }

    public Artifact update(String artifactId, Artifact update){
        //find
        return this.artifactRepository.findById(artifactId)
                .map(oldArtifact -> {
                    oldArtifact.setName(update.getName());
                    oldArtifact.setDescription(update.getDescription());
                    oldArtifact.setImageUrl(update.getImageUrl());
                    //save
                    return this.artifactRepository.save(oldArtifact);
                })
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
    }

    public void delete(String artifactId){
        this.artifactRepository.findById(artifactId).orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
        this.artifactRepository.deleteById(artifactId);
    }

    public String summarize(List<Artifact> artifacts) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArray = objectMapper.writeValueAsString(artifacts);

        //prepare the messages for summarizing
        List<Message> messages = List.of(
                new Message("system", "Your task is to generate a short summary of a given JSON array in at most 100 words. The summary must include the number of artifacts, each artifact's description, and the ownership information. Don't mention that the summary is from a given JSON array."),
                new Message("user", jsonArray)
        );

        ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", messages);

        ChatResponse chatResponse = this.chatClient.generate(chatRequest);
        return chatResponse.choices().get(0).message().content();
    }
}
