package com.jotason.phone.v1.orders.services

import akka.actor.ActorSystem
import javax.inject.Inject
import play.api.libs.concurrent.CustomExecutionContext


/**
  * Execution context used for blocking internal api requests
  */
class ApiClientIOExecutionContext @Inject()(actorSystem: ActorSystem)
  extends CustomExecutionContext(actorSystem, "apiClient.dispatcher")
