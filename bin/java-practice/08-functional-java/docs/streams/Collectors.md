https://www.baeldung.com/java-collectors

So what happens if our collection contains duplicate elements? Contrary to toSet(), the toMap() method doesn’t silently filter duplicates, which is understandable because how would it figure out which value to pick for this key?

List<String> listWithDuplicates = Arrays.asList("a", "bb", "c", "d", "bb");
assertThatThrownBy(() -> {
    listWithDuplicates.stream().collect(toMap(Function.identity(), String::length));
}).isInstanceOf(IllegalStateException.class);
Copy
Note that toMap() doesn’t even evaluate whether the values are also equal. If it sees duplicate keys, it immediately throws an IllegalStateException.

In such cases with key collision, we should use toMap() with another signature:

Map<String, Integer> result = givenList.stream()
  .collect(toMap(Function.identity(), String::length, (item, identicalItem) -> item));
Copy
The third argument here is a BinaryOperator(), where we can specify how we want to handle collisions. In this case, we’ll just pick any of these two colliding values because we know that the same strings will always have the same lengths too.

