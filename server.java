//package UnstructuredP2P;


import java.awt.List;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Set;

public class server {
	
	static ArrayList<String> nodeResource = new ArrayList<String>();
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
		            case "NodeResources":
		            	
		            	for(int i=2;i<server_req.length;i++){
		            		nodeResource.add(server_req[i]);
		            	}
		            	break;
		            	
		            case "SER":
		            	
		            	String searchKey = server_req[4];
		            	int TTL = Integer.parseInt(server_req[6]);
		            	InetAddress sIP= InetAddress.getByName(server_req[2]);
		            	int sPort = Integer.parseInt(server_req[3]);
		            	int hops= Integer.parseInt(server_req[5]);
		            	
		            	TTL=TTL-1;hops= hops+1;
////////////////////////////////////////////////////////////////////////////////////////////
//First searching for the resources in the current node
////////////////////////////////////////////////////////////////////////////////////////////
		            	int rFound=0; String res=new String();
		    			for(int a=0;a<server.nodeResource.size();a++){
		    				String resources= server.nodeResource.get(a);
		    				if(resources.contains(searchKey))
		    					{	rFound=rFound+1;
		    						res = res+" "+resources;}
		    					}
////////////////////////////////////////////////////////////////////////////////////////////////
		    			if(rFound>0){
		    				
		    				//sending search OK message
			            	String search_msg = " SEROK" + rFound+ " "+InetAddress.getLocalHost().getHostAddress()+" "+Nodeport+" "+hops+" "+res ;
		    				String serOK= String.format("%04", search_msg.length()+4)+search_msg;
		    				
		    				byte[] serOKmsg = serOK.getBytes();
							DatagramPacket serOKPacket = new DatagramPacket(serOKmsg, serOKmsg.length, sIP, sPort);
							serverSocket.send(serOKPacket);
		    			
			    			}else if((rFound==0) && (TTL>0)){
			    				
			    				// SEND TO OTHER NODES
			    				
			    				Set<String> RTKeys= unstructpp.routingTable.keySet();
			    				String[] sockAddkey = new String[RTKeys.size()];
			    				RTKeys.toArray(sockAddkey);
			    				
			    				for(int b=0;b<unstructpp.routingTable.size();b++)
			    					{
			    						String[] sockAdd = sockAddkey[b].split(":");
			    						InetAddress ip= InetAddress.getByName(sockAdd[0]);
			    						int port = Integer.parseInt(sockAdd[1]);
			    					
				    					String serForwarding = " "+server_req[1]+" "+ server_req[2]+" "+server_req[3]+" "+server_req[4]+" "+hops+" "+ TTL;
				    					String serForwardingmsg = String.format("%04", serForwarding.length()+4)+serForwarding;
				    					byte[] serFordmsg= serForwardingmsg.getBytes();
				    					
				    					DatagramPacket serFPacket = new DatagramPacket(serFordmsg, serFordmsg.length, ip, port);
				    					serverSocket.send(serFPacket);	    					
			    					}
			    				
			    				
			    			}else if((rFound==0) && !(TTL>0)){
			    				// kill the packet
			    				String search_msg = " SEROK" + rFound+ " "+InetAddress.getLocalHost().getHostAddress()+" "+Nodeport+" "+hops;
			    				String serOK= String.format("%04", search_msg.length()+4)+search_msg;
			    				
			    				byte[] serOKmsg = serOK.getBytes();
								DatagramPacket serOKPacket = new DatagramPacket(serOKmsg, serOKmsg.length, sIP, sPort);
								serverSocket.send(serOKPacket);
			    				
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
