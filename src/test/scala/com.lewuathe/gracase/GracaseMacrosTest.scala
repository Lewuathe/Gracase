package com.lewuathe.gracase

import com.lewuathe.gracase.GracaseMacros.GraphQL
import org.scalatest.WordSpec

/**
  */
class GracaseMacrosTest extends WordSpec {
  "GracaseMacros" should {

    @GraphQL
    case class User(name: String, age: Int) {
//      def hello = s"hello, $name"
    }
    @GraphQL
    case class AddressBook(users: List[User])

    @GraphQL
    case class UserPair(u1: User, u2: User)

    "gen copied object" in {
      val u = User("Nobita", 23)
      assert(u.name == "Nobita")
      assert(u.age == 23)
      assert(u.toGraphQL == "User{name age}")
      val a = AddressBook(List(u))
      assert(a.toGraphQL == "AddressBook{users}")
    }

    "gen nested object" in {
      val up = UserPair(User("Nobita", 14), User("Shizuka", 14))
      println(up.toGraphQL)
    }
  }
}
