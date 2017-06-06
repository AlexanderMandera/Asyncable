package me.creepplays.asyncablesample;

import me.creepplays.asyncable.AsyncPromise;
import me.creepplays.asyncable.Consumer;
import me.creepplays.asyncable.Function;
import me.creepplays.asyncable.Promise;

import static me.creepplays.asyncable.Asyncable.*;

public class Sample {

    public AsyncPromise<String> numberFunction() {
        return async(() -> "3");
    }

    public Sample() {
        Promise<Integer, Integer> promise = this.numberFunction()
                .then((Function<String, Integer>) Integer::valueOf)
                .then(i -> i + 2)
                .then((Consumer<Integer>) System.out::println);
    }

    public static void main(String[] args) {
        new Sample();
    }

}
