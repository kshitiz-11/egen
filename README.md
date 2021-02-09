# Steps to run the application
1. Install Maven
2. Clone the repository and go to the orderservice folder
3. run the  below commands in terminal to run the project

 mvn package docker:build 
 docker run -p 8080:8080  order-servcie/orderservice


4. Once the application gets up and running use the postman to run the below apis:

Post Api:  This will create the order

http://localhost:8081/order/
```
{
  "cutomerId": 123123,
  "subtotal": 1,
  "total": 1,
  "shippingCharges": 1,
  "tax": 1,
  "orderItems": [
    {
      "itemID": 1,
      "quantity": 3
    }
  ],
  "shippingAndBillingAddress": [
    {
      "address_line_1": "sgfgfgdfgd",
      "address_line_2": "dfdf",
      "city": "dsfd",
      "state": "fdfddsf",
      "zip": "dsfdsf",
      "addressType": "Billing"
    }
  ],
  "payment": {
    "paymentDate": "2021-01-06T18:43:14.954+0000",
    "conformationNumber": "dfd23846872346",
    "paymentMethod": "CreditCard"
  },
  "orderStatus": [
    {
      "date": "2021-01-06T18:43:14.954+0000",
      "status": "Created"
    }
  ],
  "status": "Created",
  "deliveryMethod": "curbsideDelivery"
}
```
Put Api: This will update the existing ordere

http://localhost:8081/order/

```
{
    "id": 1,
    "cutomerId": 12345,
    "subtotal": 1.0,
    "total": 1.0,
    "shippingCharges": 1.0,
    "tax": 1.0,
    "created_At": "2021-42-09",
    "updated_At": null,
    "orderItems": [
        {
            "id": 1,
            "itemID": 1,
            "quantity": 3
        }
    ],
    "shippingAndBillingAddress": [
        {
            "id": null,
            "address_line_1": "sgfgfgdfgd",
            "address_line_2": "dfdf",
            "city": "dsfd",
            "state": "fdfddsf",
            "zip": "dsfdsf",
            "addressType": "Billing",
            "created_At": null,
            "updated_At": null
        }
    ],
    "payment": {
        "id": 1,
        "paymentDate": "2021-01-06T18:43:14.954+00:00",
        "conformationNumber": "dfd23846872346",
        "paymentMethod": "CreditCard",
        "created_At": "2021-42-09"
    },
    "orderStatus": [
        {
            "id": 1,
            "created_At": "2021-42-09",
            "status": "Created"
        }
    ],
    "status": "Created",
    "deliveryMethod": "curbsideDelivery"
}
```

GET API: To retrive the order

http://localhost:8081/order/1
