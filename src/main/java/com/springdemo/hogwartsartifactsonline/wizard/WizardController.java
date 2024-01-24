package com.springdemo.hogwartsartifactsonline.wizard;

import com.springdemo.hogwartsartifactsonline.artifact.Artifact;
import com.springdemo.hogwartsartifactsonline.system.Result;
import com.springdemo.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {
    private final WizardService wizardService;

    public WizardController(WizardService wizardService) {
        this.wizardService = wizardService;
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId){
        Wizard foundWizard = this.wizardService.findById(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", foundWizard);
    }

    @GetMapping
    public Result findAllWizards(){
        List<Wizard> foundWizards = this.wizardService.findAll();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", foundWizards);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody Wizard wizard){
        Wizard savedWizard = this.wizardService.save(wizard);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedWizard);
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Integer wizardId, @Valid @RequestBody Wizard wizard){
        Wizard updatedArtifact = this.wizardService.update(wizardId, wizard);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedArtifact);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId){
        this.wizardService.delete(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact(@PathVariable Integer wizardId, @PathVariable String artifactId){
        this.wizardService.assignArtifact(wizardId, artifactId);
        return new Result(true, StatusCode.SUCCESS, "Artifact Assignment Success");
    }
}
