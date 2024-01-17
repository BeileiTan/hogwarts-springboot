package com.springdemo.hogwartsartifactsonline.artifact;

public class ArtifactNotFoundException extends RuntimeException{
    public ArtifactNotFoundException(String id){
        super("Could not find the artifact with id " + id + " :(");
    }
}
