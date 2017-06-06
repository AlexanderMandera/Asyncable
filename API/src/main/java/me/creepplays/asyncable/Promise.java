package me.creepplays.asyncable;

public class Promise<T, O> {

    Function<T, O> callback;
    Promise child;

    Promise parent;

    private Consumer<Exception> errorHandler;
    private Consumer<Promise<T, O>> resolver;

    public Promise(Consumer<Promise<T, O>> resolver) {
        this.resolver = resolver;
    }

    public Promise(Function<T, O> callback, Promise parent) {
        this.callback = callback;
        this.parent = parent;
    }

    public <R> Promise<O, R> then(Function<O, R> callback) {
        Promise parent = this.parent != null ? this.parent : this;
        this.child = new Promise<>(callback, parent);
        return this.child;
    }

    public Promise<T, T> then(final Consumer<T> consumer) {
        Promise parent = this.parent != null ? this.parent : this;
        this.child = new Promise<>((Function<T, T>) object -> {
            try {
                consumer.handle(object);
            } catch (Exception e) {
                this.reject(e);
            }
            return object;
        }, parent);
        return this.child;
    }

    public Promise<O, O> then(final Runnable runnable) {
        Promise parent = this.parent != null ? this.parent : this;
        this.child = new Promise<>((Function<O, O>) object -> {
            runnable.run();
            return object;
        }, parent);
        return this.child;
    }

    public Promise<O, O> thenSync(final Consumer<O> consumer) {
        Promise parent = this.parent != null ? this.parent : this;
        this.child = new Promise<>((Function<O, O>) value -> {
            try {
                consumer.handle(value);
            } catch (Exception e) {
                this.reject(e);
            }
            return value;
        }, parent);
        return this.child;
    }

    public Promise<T, O> error(final Consumer<Exception> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    public Promise<T, O> errorSync(final Consumer<Exception> errorHandler) {
        this.errorHandler = value -> Asyncable.sync(() -> {
            try {
                errorHandler.handle(value);
            } catch (Exception e) {
                this.reject(e);
            }
        });
        return this;
    }

    public void resolve(T value) {
        if(callback == null) {
            this.child.resolve(value);
        } else {
            O output = null;
            try {
                output = this.callback.run(value);
            } catch (Exception e) {
                this.reject(e);
            }
            this.child.resolve(output);
        }
    }

    public void reject(Exception exception) {
        if(this.errorHandler != null) {
            try {
                this.errorHandler.handle(exception);
            } catch (Exception e) {
                this.reject(exception);
            }
        } else {
            if(this.child != null) {
                this.child.reject(exception);
            }
        }
    }

    public void run() {
        if(this.parent == null) {
            try {
                this.resolver.handle(this);
            } catch (Exception e) {
                this.reject(e);
            }
            return;
        }
        this.parent.run();
    }

    public void runWithParams(Object parameter) {
        if(this.parent != null && this.parent instanceof FunctionPromise) {
            ((FunctionPromise) this.parent).run(parameter);
        } else {
            throw new IllegalStateException("Run with parameters within non-functional promise");
        }
    }

}
