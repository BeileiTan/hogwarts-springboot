package com.springdemo.hogwartsartifactsonline.wizard;

import com.springdemo.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.springdemo.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @Mock
    IdWorker idWorker;


    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Nevile Longbottom");

        this.wizards = new ArrayList<>();
        this.wizards.add(w1);
        this.wizards.add(w2);
        this.wizards.add(w3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByIdSuccess() {

        //given
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Hermione Granger");

        given(wizardRepository.findById(1)).willReturn(Optional.of(w));

        //when
        Wizard returnedWizard = wizardService.findById(1);

        //then
        assertThat(returnedWizard.getId()).isEqualTo(w.getId());
        assertThat(returnedWizard.getName()).isEqualTo(w.getName());
        assertThat(returnedWizard.getNumberOfArtifacts()).isEqualTo(w.getNumberOfArtifacts());
        verify(wizardRepository,times(1)).findById(1);
    }

    @Test
    void findByIdNotFound(){
        //given
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //when
        Throwable thrown = catchThrowable(() ->{
            Wizard returnedWizard = wizardService.findById(1);
        });

        //then
        assertThat(thrown).
                isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find the wizard with id 1 :(");
        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindAllSuccess(){
        //given
        given((wizardRepository.findAll())).willReturn(this.wizards);

        //when
        List<Wizard> actualWizards = wizardService.findAll();

        //then
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());
        verify(wizardRepository, times( 1)).findAll();

    }

    @Test
    void testSaveSucces(){
        //given
        Wizard newWizard = new Wizard();
        newWizard.setId(7);
        newWizard.setName("newWizard");
        given(wizardRepository.save(newWizard)).willReturn(newWizard);
        //when

        Wizard savedWizard = wizardService.save(newWizard);

        //then
        assertThat(savedWizard.getId()).isEqualTo(newWizard.getId());
        assertThat(savedWizard.getName()).isEqualTo(newWizard.getName());
        assertThat(savedWizard.getNumberOfArtifacts()).isEqualTo(newWizard.getNumberOfArtifacts());
        verify(wizardRepository, times( 1)).save(newWizard);

    }

    @Test
    void testUpdateSuccess(){
        //given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(2);
        oldWizard.setName("Harry Potter");

        given(wizardRepository.findById(2)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(oldWizard)).willReturn(oldWizard);

        Wizard update = new Wizard();
        update.setId(2);
        update.setName("NEW Harry Potter");

        //when
        Wizard updatedWizard = wizardService.update(2, update);

        //then
        assertThat(updatedWizard.getId()).isEqualTo(update.getId());
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());
        assertThat(updatedWizard.getNumberOfArtifacts()).isEqualTo(update.getNumberOfArtifacts());
        verify(wizardRepository, times( 1)).findById(2);
        verify(wizardRepository, times( 1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound(){
        Wizard update = new Wizard();
        update.setId(2);
        update.setName("NEW Harry Potter");

        //given
        given(wizardRepository.findById(2)).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () ->{
            wizardService.update(2, update);
        });

        //then
        verify(wizardRepository, times( 1)).findById(2);
    }

    @Test
    void testDeleteSuccess(){
        //given
        Wizard wizard = new Wizard();
        wizard.setId(2);
        wizard.setName("Harry Potter");

        given(wizardRepository.findById(2)).willReturn(Optional.of(wizard));
        doNothing().when(wizardRepository).deleteById(2);

        //when
        wizardService.delete(2);

        //then
        verify(wizardRepository, times( 1)).deleteById(2);
    }

    @Test
    void testDeleteNotFound(){
        //given
        given(wizardRepository.findById(2)).willReturn(Optional.empty());

        //when
        assertThrows(ObjectNotFoundException.class, () -> {
            wizardService.delete(2);
        });

        //then
        verify(wizardRepository, times( 1)).findById(2);
    }
}