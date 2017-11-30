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

import com.typesafe.config.{ Config, ConfigFactory, ConfigRenderOptions }
import play.api.libs.json.{ Format, Json }

object Person {
  implicit val format: Format[Person] = Json.format
}
case class Person(name: String, age: Int)

class GenerateJsonFromConfig extends TestSpec {
  it should "define a person in config, extract the object and parse it, and convert it to a value object" in {
    val cfg: Config = ConfigFactory.parseString(
      """
        |person {
        |  name = "foo"
        |  age = 42
        |}
      """.stripMargin)
    val personJson = cfg.getObject("person").render(ConfigRenderOptions.concise())
    personJson shouldBe """{"age":42,"name":"foo"}"""
    Json.parse(personJson).as[Person] shouldBe Person("foo", 42)
  }
}
