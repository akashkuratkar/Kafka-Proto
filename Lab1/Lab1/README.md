# Lab 001 (revised) - Improved (incrementally) Socket Implementation

Modifications to Lab 001 (basic socket) are provided to support for timing 
studies.

Specifically, the original lab's code only provided communication from the 
client to the server. In the revised code, several changes are provide to 
support bi-directional communication and message identification. 

You will assuredly have many thoughts on how to improve this code, and are 
free to do so. Keep in mind that the goal is to capture a representation of
an implementation that you will measure the cost of improvements. As
packages are added to add robustness and/or features, you will want to know 
what were the costs and benefits of these changes. 

## Testing

As stated above, the additional code is provided to support timing studies.  An 
additional JUnit test has been provided to demonstrate this (see TimingTest).

## Improvements 

A list of possible improvements and features:

* Use of SocketChannel to better manage server-side connections (Reduce the number of threads).

* Asynchronous classes - AsynchronousSocketChannel and AsynchronousServerSocketChannel. Does 
this improve message rates?

* A robuse encoding of messages. What is the cost of this feature?

* Feature complete: Complete chat functionality - the server's handling of rooms/users and 
forwarding messages.

* Supporting functionality: Logging and stats

* Better error handling and reporting

* Refactor classes to isolate implementation details

