# order-book Project notes
This is a gradle based project and needs Java 11 version (minimum). 
Can be setup in any ide that support java.

### A. To run tests
- Launch terminal or command prompt 
- Navigate to the directory & run below:
  - ```./gradlew test```


### B. Suggested improvements
- ```Order``` class could include setters & additional constructors:
  - Rename ```side``` to ```type``` 
  - Have a enum ```OrderType``` with BID & OFFER
  - Needs to implement ```equals``` ```hashcode``` for equality check
- Let ```OrderBook``` assign the ids, assuming its a service.
  - Needs further touch-up
- Needs to add more junit tests (sorry limited time)