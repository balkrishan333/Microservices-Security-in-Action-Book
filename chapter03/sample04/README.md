## Chapter 3: Securing north/south traffic with an API gateway (sample04)

- This is API gateway, runs on port 8081
- Token validation is integrated with API gateway
- Authorization server is integrated 
  - Authorization server should be running on port 8080 (chapter 2, sample 2)
  - Calls to fetch token and introspect token go through API gateway
- Resource server (orders microservice) is also integrated
  - Orders microservices should be running on port 8082 (chapter 2, sample 3)
- If above services are running on different ports, update those ports in application.yml