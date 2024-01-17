package com.springdemo.hogwartsartifactsonline.artifact;

import com.springdemo.hogwartsartifactsonline.system.Result;
import com.springdemo.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/artifacts")
public class ArtifactController {
    //injection from service to controller
    private final ArtifactService artifactService;

    public ArtifactController(ArtifactService artifactService) {
        this.artifactService = artifactService;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId){
        Artifact foundArtifact = this.artifactService.findById(artifactId);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", foundArtifact);
    }

    @GetMapping
    public Result findAllArtifacts(){
        List<Artifact> foundArtifacts = this.artifactService.findAll();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", foundArtifacts);
    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody Artifact artifact){
        Artifact savedArtifact =  this.artifactService.save(artifact);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedArtifact);
    }

    @PutMapping("/{artifactId}")
    public Result updatedArtifact(@PathVariable String artifactId, @Valid @RequestBody Artifact artifact){
        Artifact updatedArtifact = this.artifactService.update(artifactId, artifact);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedArtifact);
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId){
        this.artifactService.delete(artifactId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
