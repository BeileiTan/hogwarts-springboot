package com.springdemo.hogwartsartifactsonline.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springdemo.hogwartsartifactsonline.system.Result;
import com.springdemo.hogwartsartifactsonline.system.StatusCode;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/artifacts")
public class ArtifactController {
    //injection from service to controller
    private final ArtifactService artifactService;

    private final MeterRegistry meterRegistry;

    public ArtifactController(ArtifactService artifactService, MeterRegistry meterRegistry) {
        this.artifactService = artifactService;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId){
        Artifact foundArtifact = this.artifactService.findById(artifactId);
        meterRegistry.counter("artifact.id." + artifactId).increment();
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

    @GetMapping("/summary")
    public Result summarizeArtifacts() throws JsonProcessingException {
        List<Artifact> foundArtifacts = this.artifactService.findAll();
        String artifactSummary = this.artifactService.summarize(foundArtifacts);
        return new Result(true, StatusCode.SUCCESS, "Summarize Success", artifactSummary);
    }
}
