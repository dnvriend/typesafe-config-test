// Copyright 2017 Dennis Vriend
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.github.dnvriend

import com.typesafe.config.{ Config, ConfigFactory }
import scala.collection.JavaConverters._

object Main extends App {
  val cfg: Config = ConfigFactory.parseString(
    """
      |person {
      |  name = "dennis"
      |  name = ${?PERSON_FIRST_NAME}
      |  age = 42
      |  age = ${?PERSON_AGE}
      |  luckyNumbers = ["1", "2", "3"]
      |  luckyNumbers = [${?LUCKY_NUMBER_0}, ${?LUCKY_NUMBER_1}, ${?LUCKY_NUMBER_2}]
      |  accessCodes = [1, 2, 3]
      |}
    """.stripMargin).resolve()

  println("Person first name: " + cfg.getString("person.name"))
  println("Person age: " + cfg.getInt("person.age"))
  println("Person Lucky Numbers: " + cfg.getStringList("person.luckyNumbers").asScala.toList)
  println("Person access codes: " + cfg.getIntList("person.accessCodes").asScala.toList)
}
