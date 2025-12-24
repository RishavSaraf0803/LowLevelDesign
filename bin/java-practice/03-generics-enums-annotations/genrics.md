### Java Generics – Concepts to Learn (with quick interview notes)

- **Type Parameters**: Declared on classes, interfaces, and methods.
  - Syntax: `class Box<T> {}`, `<T, U>`, `<T extends Number>`, `<T extends A & B & C>`.
  - Method generics: `<T> T identity(T t)` vs class-level generics.

- **Generic Classes/Interfaces**: Create reusable, type-safe containers/abstractions.
  - Example: `Box<T>`, immutable `Pair<T,U>`.
  - Prefer immutability when possible; expose clear APIs.

- **Bounds (extends & super)**:
  - Upper bound: `<T extends Number>` restricts T to Number or its subclasses.
  - Multiple bounds (type + interfaces): `<T extends A & B & C>`.
  - For `Comparable`: `<T extends Comparable<? super T>>` (supports proper comparisons and subtypes).

- **Wildcards `?`**:
  - Unbounded: `List<?>` – read-only as `Object`, cannot add (except `null`).
  - Upper-bounded: `List<? extends Number>` – producer; safe to read as `Number`.
  - Lower-bounded: `List<? super Integer>` – consumer; safe to add `Integer`.
  - Rule of thumb (PECS): Producer Extends, Consumer Super.

- **PECS in practice**:
  - Read totals: `double sum(List<? extends Number> src)`.
  - Write ints: `void addAll(List<? super Integer> dst, List<Integer> src)`.

- **Variance & Subtyping**:
  - `List<Integer>` is NOT a subtype of `List<Number>`; wildcards model variance.
  - Arrays are covariant (`Number[] a = new Integer[1]`) but unsafe at runtime.

- **Generic Methods vs Class Generics**:
  - Prefer method-level `<T>` if the type parameter is only needed within a method.
  - Utility helpers (e.g., `copy`, `max`, `uniqueByKey`) are good candidates.

- **Type Erasure**:
  - Generics are compile-time only; type info is erased to raw types at runtime.
  - Implications:
    - No `new T()`, no `T.class`, no `new T[]` directly.
    - Overloads that differ only by type parameters can collide post-erasure.
    - Use `Class<T>` tokens or factories if you need runtime type info.

- **Limitations & Gotchas**:
  - Cannot use primitives as type args: use wrappers (`Integer`, `Double`).
  - No generic array creation: prefer `List<T>` or `@SuppressWarnings("unchecked")` with care.
  - Beware of raw types (`List`): lose type-safety and warnings appear.
  - Wildcards vs type params: prefer `<?>` for read-only APIs, `<T>` when callers must supply/receive concrete types consistently.

- **Collections and Generics**:
  - Prefer interface types: `List<T>` over `ArrayList<T>`.
  - Use bounded wildcards in APIs to increase usability (e.g., `void copy(List<? super T> dst, List<? extends T> src)`).

- **Comparisons & Ordering**:
  - For max/min: `<T extends Comparable<? super T>> T max(List<T>)`.
  - With `Comparator<T>`: `max(list, Comparator<? super T> cmp)`.

- **Functional types + Generics**:
  - `Function<? super T, ? extends R>` for mapping.
  - Great for utilities like `uniqueByKey(items, keyExtractor)` and `groupBy`.

- **Best Practices**:
  - Keep APIs flexible with wildcards on parameters and concrete types on returns.
  - Minimize use of raw types and unchecked casts.
  - Favor immutability and clear variance semantics.
  - Document bounds and variance intent.

- **Common Interview Prompts**:
  - Explain PECS with examples; when to use `extends` vs `super`.
  - Why `List<Integer>` is not a `List<Number>`; how to design APIs to accept both.
  - Implement `max`, `copy`, `groupBy`, `uniqueByKey` using generics and bounds.
  - Discuss type erasure limitations and workarounds (`Class<T>` tokens, factories).
  - Differences between generic methods and class-level generics.

- **Mini-Exercises (see code in `generics/`)**:
  - `Pair<T,U>`: create, compare, use as map keys.
  - `Box<T>`: map a value with `Function` and convert to `Optional`.
  - `GenericUtils.max` and `copy`: add bounds correctly.
  - `PECSExamples`: reason about which methods accept which lists.
  - Build `groupBy` and `uniqueByKey` using `Function` keys.

```text
Remember: Producer Extends, Consumer Super (PECS).
Return concrete types, accept wildcards to maximize usability.
```


