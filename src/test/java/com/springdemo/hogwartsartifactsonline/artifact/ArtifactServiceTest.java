package com.springdemo.hogwartsartifactsonline.artifact;

import com.springdemo.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.springdemo.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.springdemo.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "dev")
class ArtifactServiceTest {

    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Delauniator");
        a1.setDescription("A Deluminator is a device invented by Albus");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //Given: Arrange inputs and targets. Define the behavior of Mock object artifactRepository
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        a.setOwner(w);

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));

        //When: Act on the target behavior. When steps should cover the method to be tested.
        Artifact returnedArtifact = artifactService.findById(  "1250808601744904192");

        //Then: Assert expected outcomes.
        assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
        verify(artifactRepository, times( 1)).findById("1250808601744904192");

    }

    @Test
    void testFindByIdNotFound(){
        //Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(() -> {
            Artifact returnedArtifact = artifactService.findById(  "1250808601744904192");
        });

        //Then
        assertThat(thrown).
                isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find the artifact with id 1250808601744904192 :(");
        verify(artifactRepository, times( 1)).findById("1250808601744904192");
    }

    @Test
    void testFindAllSucces(){
        //Given
        given(artifactRepository.findAll()).willReturn(this.artifacts);

        //When
        List<Artifact> actualArtifacts = artifactService.findAll();

        //Then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepository, times( 1)).findAll();
    }

    @Test
    void testSaveSuccess(){
        //given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription("Desrciption...");
        newArtifact.setImageUrl("ImageUrl");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);

        //when
        Artifact savedArtifact = artifactService.save(newArtifact);

        //then
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository, times( 1)).save(newArtifact);
    }

    @Test
    void testUpdateSuccess(){
        //given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904192");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldArtifact.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        Artifact update = new Artifact();
        //update.setId("1250808601744904192");
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        //when
        Artifact updatedArtifact = artifactService.update("1250808601744904192",update);

        //then (compare the given and when)
        assertThat(updatedArtifact.getId()).isEqualTo("1250808601744904192");
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
        verify(artifactRepository, times( 1)).findById("1250808601744904192");
        verify(artifactRepository, times( 1)).save(oldArtifact);
    }

    @Test
    void testUpdateNotFound(){
        //given
        Artifact update = new Artifact();
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () -> {
            artifactService.update("1250808601744904192", update);
        });

        //then
        verify(artifactRepository, times( 1)).findById("1250808601744904192");
    }

    @Test
    void testDeleteSuccess(){
        //given
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById("1250808601744904192");

        //when
        artifactService.delete("1250808601744904192");

        //then
        verify(artifactRepository, times( 1)).deleteById("1250808601744904192");
    }

    @Test
    void testDeleteNotFound(){
        //given
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () -> {
            artifactService.delete("1250808601744904192");
        });

        //then
        verify(artifactRepository, times( 1)).findById("1250808601744904192");
    }
}