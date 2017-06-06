package me.creepplays.asyncable;

@FunctionalInterface
public interface AsyncMethod<R> {

    R run() throws Exception;

}
