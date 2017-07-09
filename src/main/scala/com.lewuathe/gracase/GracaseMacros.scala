package com.lewuathe.gracase

import scala.meta._

/**
  * Implementations of macro annotation of Gracase
  */
object GracaseMacros {
  class GraphQL extends scala.annotation.StaticAnnotation {
    inline def apply(defn: Any): Any = meta {
      defn match {
        case q"..$mods class $tname (...$paramss) extends $template" => {
          template match {
            case template"{ ..$stats } with ..$ctorcalls { $param => ..$body }" => {
              val expr = paramss.flatten.map(p => q"${p.name.toString}").zip(paramss.flatten.map {
                case param"..$mods $paramname: $atpeopt = $expropt" => paramname
              }).map {case (q"$paramName", paramTree) => {
                q"${Term.Name(paramName.toString)} -> ${Term.Name(paramTree.toString)}"
              }}

              val modelName = q"""private def modelName: String = ${tname.toString}"""
              // Add public method for constructing GraphQL query
              val toGraphQL = q"""def toGraphQL: String = modelName + Map(..$expr).keys.mkString("{", " ", "}") """
              val newBody = body :+ toGraphQL :+ modelName
              val newTemplate = template"{ ..$stats } with ..$ctorcalls { $param => ..$newBody }"

              q"..$mods class $tname (...$paramss) extends $newTemplate"
            }
          }
        }
        case _ => abort("@GraphQL must annotate a class")
      }
    }
  }
}
