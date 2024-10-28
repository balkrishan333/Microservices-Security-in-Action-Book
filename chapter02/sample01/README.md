## Chapter 2: First steps in securing microservices (sample01)

- Sample services with no security
- Server runs on port 8080  
- 2 URLs:
  - one for creating order - POST http://localhost:8080/orders
  
        {
            "orderId": "69444e79-70e4-4cf8-bc23-f3b4d34603b4",
            "items": [
                {
                    "itemCode": "IT0001",
                    "quantity": 3
                },
                {
                    "itemCode": "IT0004",
                    "quantity": 1
                }
            ],
            "shippingAddress": "No 4, Castro Street, Mounatin View, CA, USA"
        }
  - one for getting order - GET http://localhost:8080/orders/{orderId} 