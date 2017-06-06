package me.creepplays.asyncable;

public class AsyncPromise<T> extends Promise<T, T> {

    public AsyncPromise(Consumer<Promise<T, T>> resolver) {
        super(resolver);
    }

}
