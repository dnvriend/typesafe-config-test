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
import scala.collection.JavaConverters._
import pureconfig._

object Person {
  implicit val format: Format[Person] = Json.format
}

// see: https://pureconfig.github.io/docs/override-behaviour-for-sealed-families.html
sealed trait Mammal {
  def name: String; def age: Int
}
case class Cat(name: String, age: Int) extends Mammal
case class Person(name: String, age: Int) extends Mammal

sealed trait KeySchema {
  def name: String

  def datatype: String
}

case class HashKey(name: String, datatype: String = "S") extends KeySchema

case class RangeKey(name: String, datatype: String = "S") extends KeySchema

case class GlobalSecondaryIndex(
    indexName: String,
    keySchemas: List[KeySchema] = Nil,
    projectionType: String = "",
    rcu: Int = 1,
    wcu: Int = 1
)

case class Table(
    name: String,
    hashKey: HashKey,
    rangeKey: Option[RangeKey] = None,
    globalSecondaryIndexes: List[GlobalSecondaryIndex],
    stream: Option[String] = None,
    rcu: Int = 1,
    wcu: Int = 1,
    configName: String = ""
)

class GenerateJsonFromConfig extends TestSpec {
  ignore should "define a person in config, extract the object and parse it, and convert it to a value object" in {
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

  it should "parse a custom config into a custom case class" in {
    val cfg = ConfigFactory.parseString(
      """
        |dynamodb {
        |  DapRepoSchemas {
        |    name = dap_repo_schemas
        |    hash-key = {
        |      name = schemaKey
        |      datatype = S
        |    }
        |    range-key = {
        |      name = version
        |      datatype = S
        |    }
        |    people {
        |     foo {
        |      name = "foox"
        |      age = 42
        |    }
        |    bar {
        |      name = "barx"
        |      age = 43
        |    }
        |    baz {
        |      name = "bazx"
        |      age = 44
        |    }
        |    }
        |    global-secondary-indexes = [
        |      {
        |        index-name = foo
        |          key-schemas = [
        |            {
        |                type = "hashkey"
        |                name = fingerprint
        |                datatype = B
        |              }
        |          ]
        |          projection-type = ALL
        |          rcu = 2
        |          wcu = 2
        |
        |      }
        |    ]
        |    stream = KEYS_ONLY
        |    rcu = 1
        |    wcu = 1
        |  }
        |}
      """.stripMargin)

    val dynamodb: Config = cfg.getConfig("dynamodb")

    dynamodb.root().keySet().asScala.toList.map(name => (name, dynamodb.getConfig(name))).foreach {
      case (name, conf) =>
        val xx = loadConfig[Map[String, Mammal]](conf.getConfig("people"))
        println("===> map = " + xx)
        println("===> " + conf.root().keySet().asScala.toList)
        println(s"found key: $name; config: $conf")
        val s = loadConfig[Table](conf)
        println("table: ===>" + s)

    }
  }
}
