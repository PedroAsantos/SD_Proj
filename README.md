# SD_Proj
Project developed in distributed systems course.

The project was divided in 3 parts.
## Race_1
The first part was a concurrent solution and this solution has to be un locally and it serves as the base for the 
distributed versions.

## Race_2
This solution is based on the previous one, due to the fact that it reutilizes the concurrent code to create 
multiple proxy agents at the servers to serve multiple clients' requests.
Active entities and clients communicate via message passing through TCP sockets.

## Race_RMI
The previous solution was adapted to be implemented using RMI. RMI is a java framework to call methods on remote 
objects through TCP. 
