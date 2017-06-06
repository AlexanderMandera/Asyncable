package me.creepplays.asyncable;

@FunctionalInterface
public interface BiConsumer<T, U> {

    void handle(T value1, U value2) throws Exception;

}
