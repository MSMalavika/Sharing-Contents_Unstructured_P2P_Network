# Sharing_Contents_Unstructured_P2P_Network
The repository contains 
this readMe.txt
unstructpp.java
server.java
client.java
command.java
resources.txt
commons-math3-3.6.jar
design.pdf file.

Commands:

To compile all the java files:

javac -cp commons-math3-3.6.jar unstructpp.java server.java client.java command.java

To execute:

java -cp .:commons-math3-3.6.jar unstructpp <Node port> <Bootstrap server IP> <Bootstrap server port>

After we enter into the interactive mode on the terminal a help is diplayed as follows:

Give any of the following options:
resAll : To alocate resources to all the nodes.
QUERY : Query search using zipf distribution.
findQF: To find a file given as user input
LEAVE: To leave the network.
UNREGISTER: To unregister from the Bootstrap server.
showRes: To display all the resources in a node.
REG: To register.
JOIN: To join the nodes.
exit: To exit. 
