https://www.baeldung.com/java-8-functional-interfaces

https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Function.html

Any interface with a SAM(Single Abstract Method) is a functional interface, and its implementation may be treated as lambda expressions.

Note that Java 8’s default methods are not abstract and do not count; a functional interface may still have multiple default methods. We can observe this by looking at the Function’s documentation.



