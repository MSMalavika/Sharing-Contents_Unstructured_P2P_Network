package UnstructuredP2P;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

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
		            	
		            	ArrayList<String> RTDetails = new ArrayList<String>();
		            	Hashtable<String, ArrayList<String>> routingTable1 = new Hashtable<String,ArrayList<String>>();
		            	
		            	String sockADD= server_req[2]+":"+server_req[3];
		            	
						try {
							Scanner scanner = new Scanner(new FileReader("RoutingTable.txt"));
							
						    while (scanner.hasNextLine()) 
							    {
							        String[] columns = scanner.nextLine().split(" ");
							        for(int i=1;i<columns.length;i++) 
							        {RTDetails.add(columns[i]);}							        
							        routingTable1.put(columns[0],RTDetails);
							    }scanner.close();

						    System.out.println("Map is "+ routingTable1);
							
						}catch(FileNotFoundException f) 
							{
								routingTable1.put(sockADD, RTDetails);
							
								// Creating a Routing table file and writing the sockADD to the file	
								File file = new File("RoutingTable.txt"); 
								try
								{
								   BufferedWriter bw = new BufferedWriter(new FileWriter(file));
								   for(String p:routingTable1.keySet())
								   {
								      bw.write(p + ":" + routingTable1.get(p));
								      bw.newLine();
								   }
								   bw.flush();
								   bw.close();
								}catch (IOException I) {
									System.out.println("Error: " + I);
									I.printStackTrace();
								}
							}
		            	
						
						if (routingTable1.containsKey(sockADD)) 
							{
								String joinmsg= "0014 JOINOK 0"; 
				            	byte[] joinOkmsg = joinmsg.getBytes();
				         // Sending the JOINOK response
								DatagramPacket join_response = new DatagramPacket(joinOkmsg, joinOkmsg.length, reqIP, reqPort);
								serverSocket.send(join_response);
								
							}else if (!routingTable1.containsKey(sockADD))
								{
									routingTable1.put(sockADD, RTDetails);
									
									if (routingTable1.containsKey(sockADD))
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
