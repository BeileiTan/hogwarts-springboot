package com.springdemo.hogwartsartifactsonline.wizard;

import com.springdemo.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.springdemo.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {
    private final WizardRepository wizardRepository;

    private final IdWorker idWorker;

    public WizardService(WizardRepository wizardRepository, IdWorker idWorker) {
        this.wizardRepository = wizardRepository;
        this.idWorker = idWorker;
    }

    public Wizard findById(Integer wizardId){
        return this.wizardRepository.findById(wizardId).orElseThrow(()->new ObjectNotFoundException("wizard",wizardId));
    }

    public List<Wizard> findAll(){
        return this.wizardRepository.findAll();
    }

    public Wizard save(Wizard newWizard){
        return this.wizardRepository.save(newWizard);
    }

    public Wizard update(Integer wizardId, Wizard update){
        return this.wizardRepository.findById(wizardId)
                .map(oldWizard -> {
                    oldWizard.setName(update.getName());
                    //save
                    return this.wizardRepository.save(oldWizard);
                })
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
    }

    public void delete(Integer wizardId){
        Wizard wizardToBeDelected = this.wizardRepository.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
        //before deletion, we will unassign this wizard's own artofacts, otherwise we have foregin key issue(500 internal error)
        wizardToBeDelected.removeAllArtifact();
        this.wizardRepository.deleteById(wizardId);
    }
}
