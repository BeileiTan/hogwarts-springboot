package com.springdemo.hogwartsartifactsonline.wizard;

import com.springdemo.hogwartsartifactsonline.artifact.Artifact;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wizard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "name is required")
    private String name;

    //one-to-many: one wizard can have many artifact
    // artifacts to maintain the foregin key
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner")
    private List<Artifact> artifacts = new ArrayList<>();

    @Transient
    private Integer numberOfArtifacts;

    public Wizard(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Wizard(Integer id, String name, Integer numberOfArtifacts) {
        this.id = id;
        this.name = name;
        this.numberOfArtifacts = numberOfArtifacts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size();
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public void addArtifact(Artifact artifact){
        artifact.setOwner(this);
        this.artifacts.add(artifact);
    }

    //detach the artifacts when delete the wizard
    public void removeAllArtifact(){
        for(Artifact artifact : this.artifacts){
            artifact.setOwner(null);
            this.artifacts = null;
        }
    }

    public void removeArtifact(Artifact artifactToBeAssigned){
        artifactToBeAssigned.setOwner(null);
        this.artifacts.remove(artifactToBeAssigned);
    }

}
