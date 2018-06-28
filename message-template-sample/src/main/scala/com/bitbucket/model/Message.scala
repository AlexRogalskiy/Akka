package com.bitbucket.model

import scala.concurrent.duration.FiniteDuration

case class Message(messageText: String)

case class UpdateDelay(duration: FiniteDuration)

case object NewNode


