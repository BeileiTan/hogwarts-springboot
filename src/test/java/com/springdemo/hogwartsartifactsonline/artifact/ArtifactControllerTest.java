package com.springdemo.hogwartsartifactsonline.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springdemo.hogwartsartifactsonline.system.StatusCode;
import com.springdemo.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;

    List<Artifact> artifacts;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Delauniator");
        a1.setDescription("A Deluminator is a device invented by Albus");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");
        this.artifacts.add(a2);

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Time Turner");
        a3.setDescription("A Time Turner is a magical time-travel device.");
        a3.setImageUrl("ImageUrl");
        this.artifacts.add(a3);

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("Pensieve");
        a4.setDescription("A Pensieve is a magical object to review and enter memories.");
        a4.setImageUrl("ImageUrl");
        this.artifacts.add(a4);

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("Marauder's Map");
        a5.setDescription("The Marauder's Map is a magical document revealing Hogwarts School of Witchcraft and Wizardry.");
        a5.setImageUrl("ImageUrl");
        this.artifacts.add(a5);

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Portkey");
        a6.setDescription("A Portkey is an enchanted object for magical transportation.");
        a6.setImageUrl("ImageUrl");
        this.artifacts.add(a6);
    }


    @AfterEach
    void tearDown() {
    }

    @Test
    void findArtifactByIdSuccess() throws Exception {
        //Given
        given(artifactService.findById("1250808601744904191")).willReturn(this.artifacts.get(0));

        //When AND Then
        this.mockMvc.perform(get(this.baseUrl + "/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data.name").value("Delauniator"));
    }

    @Test
    void findArtifactByIdNotFound() throws Exception {
        //Given
        given(artifactService.findById("1250808601744904191")).willThrow(new  ObjectNotFoundException("artifact", "1250808601744904191"));

        //When AND Then
        this.mockMvc.perform(get(this.baseUrl + "/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find the artifact with id 1250808601744904191 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        //Given
        given(artifactService.findAll()).willReturn(this.artifacts);

        //When and then
        this.mockMvc.perform(get(this.baseUrl+ "/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data[0].name").value("Delauniator"))
                .andExpect(jsonPath("$.data[1].id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data[1].name").value("Invisibility Cloak"));
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        //given
        //Simulate what the front-end provides to the controller
            Artifact artifact = new Artifact(null, "Philosopher's Stone",
                    "The Philosopher's Stone is a legendary alchemical substance that can turn any metal into pure gold and grant immortality.",
                    "ImageUrl", null);
            String json = this.objectMapper.writeValueAsString(artifact);

            //fake the Artifact with the id
            Artifact savedArtifact = new Artifact();
            savedArtifact.setId("1250808601744904197");
            savedArtifact.setName("Philosopher's Stone");
            savedArtifact.setDescription("The Philosopher's Stone is a legendary alchemical substance that can turn any metal into pure gold and grant immortality.");
            savedArtifact.setImageUrl("ImageUrl");

            given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);
        //when and then
        this.mockMvc.perform(post(this.baseUrl+ "/artifacts").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl()));
    }

    @Test
    void  testUpdateArtifactSuccess() throws Exception {
        //given
        //Simulate what the front-end provides to the controller
        Artifact artifact = new Artifact("1250808601744904192", "Invisibility Cloak",
                "A new description.",
                "ImageUrl", null);
        String json = this.objectMapper.writeValueAsString(artifact);

        //fake the Artifact with the id
        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("1250808601744904192");
        updatedArtifact.setName("Invisibility Cloak");
        updatedArtifact.setDescription("A new description.");
        updatedArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.update(eq("1250808601744904192"), Mockito.any(Artifact.class))).willReturn(updatedArtifact);
        //when and then
        this.mockMvc.perform(put(this.baseUrl+ "/artifacts/1250808601744904192").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedArtifact.getImageUrl()));
    }

    @Test
    void testUpdateArtifactErrorWithNonExisitedId() throws Exception {
        //given
        //Simulate what the front-end provides to the controller
        Artifact artifact = new Artifact("1250808601744904192", "Invisibility Cloak",
                "A new description.",
                "ImageUrl", null);
        String json = this.objectMapper.writeValueAsString(artifact);

        given(this.artifactService.update(eq("1250808601744904192"), Mockito.any(Artifact.class))).willThrow(new ObjectNotFoundException("artifact", "1250808601744904192"));
        //when and then
        this.mockMvc.perform(put(this.baseUrl+ "/artifacts/1250808601744904192").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find the artifact with id 1250808601744904192 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactSuccess() throws Exception {
        //given
        doNothing().when(this.artifactService).delete("1250808601744904191");

        //When AND Then
        this.mockMvc.perform(delete(this.baseUrl+ "/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void testDeleteArtifactErrorWithNonExisitedId() throws Exception {
        //given
        doThrow(new ObjectNotFoundException("artifact", "1250808601744904191")).when(this.artifactService).delete("1250808601744904191");

        //When AND Then
        this.mockMvc.perform(delete(this.baseUrl+ "/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find the artifact with id 1250808601744904191 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}