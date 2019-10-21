# Asyncable for Java

Use simply async your calls in Java very similar to the JavaScript Promise operations.

## Example
```java
AsyncPromise<String> numberFunction() {
  return async(() -> "3");
}


Promise<Integer, Integer> promise = this.numberFunction()
  .then((Function<String, Integer>) Integer::valueOf)
  .then(i -> i + 2)
  .then((Consumer<Integer>) System.out::println);
```
More details [here](https://github.com/AlexanderMandera/Asyncable/blob/master/Sample/src/main/java/me.creepplays.asyncablesample/Sample.java)
