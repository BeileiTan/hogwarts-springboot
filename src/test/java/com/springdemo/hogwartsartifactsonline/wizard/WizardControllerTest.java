package com.springdemo.hogwartsartifactsonline.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springdemo.hogwartsartifactsonline.system.StatusCode;
import com.springdemo.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(value = "dev")
class WizardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WizardService wizardService;

    @Autowired
    ObjectMapper objectMapper;

    List<Wizard> wizards;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.wizards = new ArrayList<>();

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        this.wizards.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        this.wizards.add(w2);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Nevile Longbottom");
        this.wizards.add(w3);

        Wizard w4 = new Wizard();
        w4.setId(4);
        w4.setName("Luna Lovegood");
        this.wizards.add(w4);

        Wizard w5 = new Wizard();
        w5.setId(5);
        w5.setName("Ginny Weasley");
        this.wizards.add(w5);

        Wizard w6 = new Wizard();
        w6.setId(6);
        w6.setName("Sirius Black");
        this.wizards.add(w6);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findWizardByIdSuccess() throws Exception {
        //given
        given(wizardService.findById(1)).willReturn(this.wizards.get(0));

        //when and then
        this.mockMvc.perform(get(this.baseUrl+ "/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value("Albus Dumbledore"));

    }

    @Test
    void findWizardByIdNotFound() throws Exception {
        given(wizardService.findById(10)).willThrow(new ObjectNotFoundException("wizard",10));

        //When AND Then
        this.mockMvc.perform(get(this.baseUrl+ "/wizards/10").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find the wizard with id 10 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testFindAllWizardsSuccess() throws Exception {
        given(wizardService.findAll()).willReturn(this.wizards);

        //When AND Then
        this.mockMvc.perform(get(this.baseUrl+ "/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[0].name").value("Albus Dumbledore"))
                .andExpect(jsonPath("$.data[1].id").value("2"))
                .andExpect(jsonPath("$.data[1].name").value("Harry Potter"));
    }

    @Test
    void testAddWizardSuccess() throws Exception {
        Wizard wizard = new Wizard(null, "Ron Weasley", null);
        String json = this.objectMapper.writeValueAsString(wizard);

        //faked
        Wizard savedWizard = new Wizard();
        savedWizard.setId(7);
        savedWizard.setName("Ron Weasley");

        given(this.wizardService.save(Mockito.any(Wizard.class))).willReturn(savedWizard);

        //when and then
        this.mockMvc.perform(post(this.baseUrl + "/wizards").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedWizard.getName()));
    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
        //Simulate what the front-end provides to the controller
        Wizard wizard = new Wizard(2, "newHarry Potter", null);
        String json = this.objectMapper.writeValueAsString(wizard);

        //fake data
        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(2);
        updatedWizard.setName("newHarry Potter");

        given(this.wizardService.update(eq(2), Mockito.any(Wizard.class))).willReturn(updatedWizard);

        //when and then
        this.mockMvc.perform(put(this.baseUrl + "/wizards/2").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.name").value(updatedWizard.getName()));
    }

    @Test
    void testUpdateWizardErrorWithNonExisitedId() throws Exception {
        //Simulate what the front-end provides to the controller
        Wizard wizard = new Wizard(2, "newHarry Potter", null);
        String json = this.objectMapper.writeValueAsString(wizard);

        given(this.wizardService.update(eq(2), Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("wizard",2));

        //when and then
        this.mockMvc.perform(put(this.baseUrl + "/wizards/2").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find the wizard with id 2 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWizardSuccess() throws Exception {
        //given
        doNothing().when(this.wizardService).delete(2);

        //when and then
        this.mockMvc.perform(delete(this.baseUrl + "/wizards/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testDeleteWizardErrorWithNonExisitedId() throws Exception {
        //given
        doThrow(new ObjectNotFoundException("wizard",2)).when(this.wizardService).delete(2);

        //When AND Then
        this.mockMvc.perform(delete(this.baseUrl + "/wizards/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find the wizard with id 2 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactSuccess() throws Exception {
        //given
        doNothing().when(this.wizardService).assignArtifact(2, "1250808601744904192");
        //when and then
        this.mockMvc.perform(put(this.baseUrl + "/wizards/2/artifacts/1250808601744904192").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact Assignment Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonWizardExisitedId() throws Exception {
        //given
        doThrow(new ObjectNotFoundException("wizard",5)).when(this.wizardService).assignArtifact(5, "1250808601744904192");

        //When AND Then
        this.mockMvc.perform(put(this.baseUrl + "/wizards/5/artifacts/1250808601744904192").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find the wizard with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonArtifactExisitedId() throws Exception {
        //given
        doThrow(new ObjectNotFoundException("artifact","1250808601744904110")).when(this.wizardService).assignArtifact(2, "1250808601744904110");

        //When AND Then
        this.mockMvc.perform(put(this.baseUrl + "/wizards/2/artifacts/1250808601744904110").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find the artifact with id 1250808601744904110 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}