package com.jotason.phone.v1.inventory.repository

/**
  * Builder utility for building URLs
  */
trait URLBuilder { this: ConfigurationLike =>

  private val hostPort = "inventory.host.port"
  private val hostDomain = "inventory.host.domain"

  /**
    * Build the absolute url of an image from a image reference
    * @param imageRef the reference of an image (name)
    * @return absolute url of a image
    */
  def buildImageUrl(imageRef: String): String = {
    val port = configuration.get[String](hostPort)
    val baseUrl = configuration.get[String](hostDomain)
    s"http://$baseUrl:$port/assets/images/$imageRef"
  }

}
