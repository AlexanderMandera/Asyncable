package me.creepplays.asyncable;

@FunctionalInterface
public interface Consumer<T> {

    void handle(T value) throws Exception;

}
