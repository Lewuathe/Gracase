Gracase [![Build Status](https://travis-ci.org/Lewuathe/Gracase.svg?branch=master)](https://travis-ci.org/Lewuathe/Gracase)
=====

Gracase is the library to provide the bridge between Scala object and GraphQL query by using Scala meta macros.

# Usage

```scala
@GraphQL
case class User(name: String, age: Int) {}

val u = User("Nobita", 23)
assert(u.toGraphQL == "User{name age}")
```

# Future works

* Fully support of fields of nested case classes
* [Arguments](http://graphql.org/learn/queries/#arguments)


# LICENSE

[MIT License](https://opensource.org/licenses/MIT)