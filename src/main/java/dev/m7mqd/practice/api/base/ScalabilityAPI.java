package dev.m7mqd.practice.api.base;

import lombok.NonNull;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.CompletableFuture;

public interface ScalabilityAPI{
    CompletableFuture<Arena> createRandom();
    CompletableFuture<Arena> create(@NonNull Arena templateArena);
    CompletableFuture<Arena> delete(@NonNull Arena templateArena);
    ScalabilityType getType();
    String getImplementation();
    default void load(){
    }
    default void unload(){

    }
}
