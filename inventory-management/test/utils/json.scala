package utils

import play.api.libs.json.{JsValue, Json}

object json {

  val catalogJson: JsValue = Json.parse(
    """
      [
         {
           "description": "Mobile phone 4G with 10 Mpx. Not bad.",
           "id": 1,
           "imageUrl": "http://localhost:9000/assets/images/moto-g-2014-1.jpg",
           "name": "Moto G2",
           "price": 150.52
         },
         {
           "description": "Mobile phone 4G very expensive",
           "id": 2,
           "imageUrl": "http://localhost:9000/assets/images/iphone_x.jpeg",
           "name": "iPhone X",
           "price": 1100.32
         },
         {
           "description": "Mobile phone 4G from Sony",
           "id": 3,
           "imageUrl": "http://localhost:9000/assets/images/xperia_l1.jpg",
           "name": "Xperia L1",
           "price": 200
         }
     ]
  """)

  val phoneJson: JsValue = Json.parse(
    """
     {
       "description": "Mobile phone 4G with 10 Mpx. Not bad.",
       "id": 1,
       "imageUrl": "http://localhost:9000/assets/images/moto-g-2014-1.jpg",
       "name": "Moto G2",
       "price": 150.52
     }
  """)


}
