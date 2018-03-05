package UnstructuredP2P;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class server {
	
	public int Nodeport;

	   public server(int Nodeport)
	   {	      
		   this.Nodeport = Nodeport;
	   }	   
	   
	 public void run(){
			try {
				DatagramSocket serverSocket = new DatagramSocket(Nodeport);
				
				while (true){
					byte[] reqData = new byte[65000];
					
				// Receiving a request from other nodes
		            DatagramPacket incomming_req = new DatagramPacket(reqData, reqData.length);		            
		            serverSocket.receive(incomming_req);
		            InetAddress reqIP = incomming_req.getAddress();
		            int reqPort = incomming_req.getPort();
		            String inRequest = new String(incomming_req.getData(),0,incomming_req.getLength());
		            
		            System.out.println("msg received in server: " + inRequest );
		            
		            String[] server_req = inRequest.split(" ");
		            String server_cmd = server_req[1];
		            
		            switch (server_cmd){
		            case "JOIN":
		            	
		            	String sockADD= server_req[2]+":"+server_req[3];
		            	
						
						if (unstructpp.routingTable.containsKey(sockADD)) 
							{
								String joinmsg= "0014 JOINOK 0"; 
				            	byte[] joinOkmsg = joinmsg.getBytes();
				         // Sending the JOINOK response
								DatagramPacket join_response = new DatagramPacket(joinOkmsg, joinOkmsg.length, reqIP, reqPort);
								serverSocket.send(join_response);
						
							}else if (!unstructpp.routingTable.containsKey(sockADD))
								{
									unstructpp.routingTable.put(sockADD, unstructpp.RTDetails);
									
									if (unstructpp.routingTable.containsKey(sockADD))
									{
										String joinmsg= "0013 JOINOK 0"; 
						            	byte[] joinOkmsg = joinmsg.getBytes();
						         // Sending the JOINOK response
										DatagramPacket join_response = new DatagramPacket(joinOkmsg, joinOkmsg.length, reqIP, reqPort);
										serverSocket.send(join_response);
										
										
									}else {
											String joinErr = "0016 JOINOK 9999";
											byte[] joinErrmsg = joinErr.getBytes();
											// Sending the JOINOK response
											DatagramPacket joinresponse = new DatagramPacket(joinErrmsg, joinErrmsg.length, reqIP, reqPort);
											serverSocket.send(joinresponse);
										}
									
								}else 
									{
										String joinErr = "0016 JOINOK 9999";
										byte[] joinErrmsg = joinErr.getBytes();
										// Sending the JOINOK response
										DatagramPacket joinresponse = new DatagramPacket(joinErrmsg, joinErrmsg.length, reqIP, reqPort);
										serverSocket.send(joinresponse);
									}
		            	break;
		            	
		            case "LEAVE":
		            	String leaveSockAdd=server_req[2]+";"+server_req[3];
		           
		            // removing the node from RT
						unstructpp.routingTable.remove(leaveSockAdd);
		            // Sending leave OK message 
						if(!unstructpp.routingTable.containsKey(leaveSockAdd)){
							String leavemsg= " LEAVEOK 0";
							String leaveok= String.format("%04", (leavemsg.length()+4))+leavemsg;
							byte[] leave = leaveok.getBytes();
							DatagramPacket leaverespose = new DatagramPacket(leave, leave.length, reqIP, reqPort);
							serverSocket.send(leaverespose);
							
							System.out.println("Status: "+reqIP+":"+ reqPort+" Node Successfully left ");
							
						}else {
							String leavemsg= " LEAVEOK 9999";
							String leaveok= String.format("%04", (leavemsg.length()+4))+leavemsg;
							byte[] leave = leaveok.getBytes();
							DatagramPacket leaverespose = new DatagramPacket(leave, leave.length, reqIP, reqPort);
							serverSocket.send(leaverespose);
						}
						
						break;
		            default:
		            	System.out.println(" The incoming request is  "+inRequest);
		            	break;
		            }
		            	
				}			
				
			} catch (NumberFormatException | SocketException e) {				
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

}
