package me.creepplays.asyncablesample;

import me.creepplays.asyncable.Consumer;
import me.creepplays.asyncable.Function;
import me.creepplays.asyncable.FunctionPromise;

import static me.creepplays.asyncable.Asyncable.*;

public class Sample {

    public FunctionPromise<String, String> numberFunction() {
        return async(p -> p);
    }

    public Sample() {
        this.numberFunction()
                .then((Function<String, Integer>) Integer::valueOf)
                .then(i -> i + 2)
                .then((Consumer<Integer>) System.out::println)
                .runWithParams("3");
    }

    public static void main(String[] args) {
        new Sample();
    }

}
