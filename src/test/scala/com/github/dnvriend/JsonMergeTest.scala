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

import play.api.libs.json._

import scalaz.Monoid
import scalaz._
import scalaz.Scalaz._

class JsonMergeTest extends TestSpec {
  implicit val jsonMonoid: Monoid[JsValue] = Monoid.instance({
    case (JsNull, that) => that
    case (that, JsNull) => that
    case (l: JsObject, r: JsObject) => l ++ r
    case (l: JsArray, r: JsArray) => l ++ r
    case (JsNull, JsNull) => JsNull
  }, Json.obj())

  def buildProperties(properties: JsValue): JsObject = {
    Json.obj("Properties" -> properties)
  }

  it should "merge fields to jsob object" in {
    val properties: JsValue = List(
      None,
      Option(("foo1", "bar1")),
      Option(("foo2", "bar2")),
      None,
      Option(("foo3", "bar3")),
      None,
      Option(("foo4", "bar4")),
      None,
    ).foldMap {
      case Some((k, v)) => Json.obj(k -> v)
      case None => JsNull
    }
    println(buildProperties(properties))
  }
}
