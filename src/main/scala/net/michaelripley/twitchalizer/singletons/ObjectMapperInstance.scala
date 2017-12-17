package net.michaelripley.twitchalizer.singletons

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object ObjectMapperInstance {
  lazy val instance: ObjectMapper = new ObjectMapper()
    .registerModule(DefaultScalaModule)
}
