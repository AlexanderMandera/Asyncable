package me.creepplays.asyncable;

public class FunctionPromise<T, P> extends Promise<T, T> {

    private BiConsumer<Promise<T, T>, P> resolver;

    private FunctionPromise(Consumer<Promise<T, T>> resolver) {
        super(resolver);
    }

    public FunctionPromise(BiConsumer<Promise<T, T>, P> resolver) {
        super(p -> resolver.handle(p, null));
        this.resolver = resolver;
    }

    public void run(P parameter) {
        if(this.parent == null) {
            try {
                this.resolver.handle(this, parameter);
            } catch (Exception e) {
                this.reject(e);
            }
        }
    }

}
