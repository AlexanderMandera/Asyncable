package me.creepplays.asyncable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Asyncable {

    private static Asyncable instance;

    private static Asyncable getInstance() {
        if(instance == null) {
            instance = new Asyncable();
        }
        return instance;
    }

    private ExecutorService executorService;

    private Consumer<Runnable> synchronizer;
    private Consumer<Runnable> asynchronizer;

    private Asyncable() {
        this.executorService = Executors.newCachedThreadPool();
        this.asynchronizer = (r) -> this.executorService.submit(r);
    }

    private void setSynchronizerFunction(Consumer<Runnable> synchronizer) {
        this.synchronizer = synchronizer;
    }
    private void setAsynchronizerFunction(Consumer<Runnable> asynchronizer) {
        this.asynchronizer = asynchronizer;
    }

    private <T> AsyncPromise<T> makeAsync(final AsyncMethod<T> function) {
        return new AsyncPromise<>(promise -> {
            if(Thread.currentThread().getId() == 1) {
                this.asynchronizer.handle(() -> {
                    try {
                        promise.resolve(function.run());
                    } catch (Exception e) {
                        promise.reject(e);
                    }
                });
            } else {
                try {
                    promise.resolve(function.run());
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        });
    }

    private <P, R> FunctionPromise<R, P> makeAsync(final Function<P, R> function) {
        return new FunctionPromise<>((promise, param) -> {
            if (Thread.currentThread().getId() == 1) {
                Asyncable.this.executorService.submit(() -> {
                    try {
                        promise.resolve(function.run(param));
                    } catch (Exception e) {
                        promise.reject(e);
                    }
                });
            } else {
                try {
                    promise.resolve(function.run(param));
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        });
    }

    private void makeSync(Runnable runnable) {
        if(this.synchronizer != null) {
            try {
                this.synchronizer.handle(runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            runnable.run();
        }
    }

    // Static methods

    /**
     * Makes a method asynchronous
     * @param function The method to make asynchronous
     * @param <T> The return type of the method to use in Promise
     * @return The promise
     */
    public static <T> AsyncPromise<T> async(AsyncMethod<T> function) {
        return Asyncable.getInstance().makeAsync(function);
    }

    /**
     * Makes a method asynchronous and let it run with parameters
     * @param function The method with the parameter to make asynchronous
     * @param <T> The return type of the method to use in Promise
     * @param <P> The type of the parameter
     * @return The promise
     */
    public static <T, P> FunctionPromise<T, P> async(Function<P, T> function) {
        return Asyncable.getInstance().makeAsync(function);
    }

    /**
     * Runs an action on the main thread depending on the synchronizer function
     * @param runnable The action to run synchronously
     */
    public static void sync(Runnable runnable) {
        Asyncable.getInstance().makeSync(runnable);
    }

    /**
     * Sets the synchronizer function to run a action on the main thread
     * @param synchronizer The function to run a action on the main thread
     */
    public static void setSynchronizer(Consumer<Runnable> synchronizer) {
        Asyncable.getInstance().setSynchronizerFunction(synchronizer);
    }

    /**
     * Sets the asynchronizer function to run a action on a non-main thread
     * @param asynchronizer The function to run a action on a non-main thread
     */
    public static void setAsynchronizer(Consumer<Runnable> asynchronizer) {
        Asyncable.getInstance().setAsynchronizerFunction(asynchronizer);
    }

}
