package utils

import play.api.libs.json.{JsValue, Json}

object json {


  val createValidNewOrderJson: JsValue = Json.parse(
    """
       {
        "customerName": "Jose",
        "customerSurname": "Velasco",
        "customerEmail": "jotason.velasco@gmail.com",
        "lineItems": [
          {
            "phoneId": 1,
            "quantity": 2
          },
          {
            "phoneId": 2,
            "quantity": 1
          }
        ]
      }
  """)

  val createEmailInvalidNewOrderJson: JsValue = Json.parse(
    """
       {
        "customerName": "Jose",
        "customerSurname": "Velasco",
        "customerEmail": "jotason.velasco_gmail.com",
        "lineItems": [
          {
            "phoneId": 1,
            "quantity": 2
          },
          {
            "phoneId": 2,
            "quantity": 1
          }
        ]
      }
  """)

  val createNotCatalogedPhoneNewOrderJson: JsValue = Json.parse(
    """
       {
        "customerName": "Jose",
        "customerSurname": "Velasco",
        "customerEmail": "jotason.velasco@gmail.com",
        "lineItems": [
          {
            "phoneId": 111,
            "quantity": 2
          },
          {
            "phoneId": 2,
            "quantity": 1
          }
        ]
      }
  """)

  val createQuantityZeroPhoneNewOrderJson: JsValue = Json.parse(
    """
       {
        "customerName": "Jose",
        "customerSurname": "Velasco",
        "customerEmail": "jotason.velasco@gmail.com",
        "lineItems": [
          {
            "phoneId": 1,
            "quantity": 0
          },
          {
            "phoneId": 2,
            "quantity": 1
          }
        ]
      }
  """)

  val createNotNameNewOrderJson: JsValue = Json.parse(
    """
       {
        "customerName": "",
        "customerSurname": "Velasco",
        "customerEmail": "jotason.velasco@gmail.com",
        "lineItems": [
          {
            "phoneId": 1,
            "quantity": 2
          },
          {
            "phoneId": 2,
            "quantity": 1
          }
        ]
      }
  """)

  val createNoItemsNewOrderJson: JsValue = Json.parse(
    """
       {
        "customerName": "Jose",
        "customerSurname": "Velasco",
        "customerEmail": "jotason.velasco@gmail.com",
        "lineItems": []
      }
  """)

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

  val orderCreatedJson: JsValue = Json.parse(
    """
      {
        "location": "http://localhost:9003/v1/orders/1",
        "orderId": 1
      }
    """)

  val orderJson: JsValue = Json.parse(
    """
     {
       "customerEmail": "jotason.velasco@gmail.com",
       "customerName": "Jose",
       "customerSurname": "Velasco",
       "lineItems": [
         {
           "phoneId": 1,
           "phoneReference": "http://localhost:9000/v1/phones/1",
           "quantity": 2,
           "subtotal": 301.04
         },
         {
           "phoneId": 2,
           "phoneReference": "http://localhost:9000/v1/phones/2",
           "quantity": 1,
           "subtotal": 1100.32
         }
       ],
       "orderId": 1,
       "totalPrice": 1401.36
     }
    """)



}
