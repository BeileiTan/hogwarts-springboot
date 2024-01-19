package com.springdemo.hogwartsartifactsonline.system.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String objectName, String id) {
        super("Could not find the " + objectName + " with id " + id + " :(");
    }

    public ObjectNotFoundException(String objectName, Integer id) {
        super("Could not find the " + objectName + " with id " + id + " :(");
    }
}
