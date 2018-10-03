package com.jotason.phone.v1.orders.repository

import akka.actor.ActorSystem
import javax.inject.Inject
import play.api.libs.concurrent.CustomExecutionContext

/**
  * Execution context used for blocking db operations
  */
class DBIOExecutionContext @Inject()(actorSystem: ActorSystem)
  extends CustomExecutionContext(actorSystem, "repository.dispatcher")
