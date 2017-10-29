package io.github.pauljamescleary.petstore

import org.joda.time.DateTime
import org.scalacheck._
import org.scalacheck.Arbitrary.arbitrary

import io.github.pauljamescleary.petstore.model._
import io.github.pauljamescleary.petstore.model.OrderStatus._
import io.github.pauljamescleary.petstore.model.PetStatus._

trait PetStoreArbitraries {

  implicit val dateTime = Arbitrary[DateTime] {
    for {
      millis <- Gen.posNum[Long]
    } yield new DateTime(millis)
  }

  implicit val orderStatus = Arbitrary[OrderStatus] {
    Gen.oneOf(Approved, Delivered, Placed)
  }

  implicit val order = Arbitrary[Order] {
    for {
      petId <- Gen.posNum[Long]
      shipDate <- Gen.option(dateTime.arbitrary)
      status <- arbitrary[OrderStatus]
      complete <- arbitrary[Boolean]
      id <- Gen.option(Gen.posNum[Long])
    } yield Order(petId, shipDate, status, complete, id)
  }

  implicit val petStatus = Arbitrary[PetStatus] {
    Gen.oneOf(Available, Pending, Adopted)
  }

  implicit val pet = Arbitrary[Pet] {
    for {
      name <- arbitrary[String]
      category <- arbitrary[String]
      bio <- arbitrary[String]
      status <- arbitrary[PetStatus]
      numTags <- Gen.choose(0, 10)
      tags <- Gen.listOfN(numTags, Gen.alphaStr).map(_.toSet)
      photoUrls <- Gen
        .listOfN(numTags, Gen.alphaStr)
        .map(_.map(x => s"http://${x}.com"))
        .map(_.toSet)
      id <- Gen.option(Gen.posNum[Long])
    } yield Pet(name, category, bio, status, tags, photoUrls, id)
  }

}

object PetStoreArbitraries extends PetStoreArbitraries
