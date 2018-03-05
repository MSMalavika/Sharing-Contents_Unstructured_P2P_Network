//package UnstructuredP2P;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.math3.distribution.ZipfDistribution;

public class command {
	
// Storage of nodes details received from Bootstrap Server
	static ArrayList<String> Node_info = new ArrayList<String>();
	
	
	public void reg(InetAddress BS_ip, int BS_port, int Node_port)   {
		
		try {
		String Node_IP = InetAddress.getLocalHost().getHostAddress();
		String request =  "REG" + " " + Node_IP + " " + Node_port + " " + client.uname ;
		int msg_len =  request.length() + 5;
		String reg_msg = String.format("%04d", msg_len) + " " +request;
		byte[] Reg_request = reg_msg.getBytes();
	
		DatagramSocket client_Socket = new DatagramSocket();	
	
		//sending the register request to the BS
			
			DatagramPacket REG_Packet = new DatagramPacket(Reg_request, Reg_request.length, BS_ip, BS_port);
			client_Socket.send(REG_Packet);
			
		//receiving node socket address from BS
			
			byte[] BS_response = new byte[65000];
		    DatagramPacket BSResponse = new DatagramPacket(BS_response, BS_response.length);
		    client_Socket.receive(BSResponse); 
		    
		    String BSR = new String(BSResponse.getData(),0,BSResponse.getLength());
		    String[] BS_Response = BSR.split(" ");
		    
		    if (BS_Response[3].equals("9999"))
			    {
			    	System.err.println("Error: Registration failure" );
				    }else if (BS_Response[3].equals("9998")){
				    	System.err.println("Error: Already registered, unregister first" );
					    }else if(BS_Response[3].equals("-1")){
					    	System.out.println("Error: Unknow REG comand ");
						    }else{
						    	System.out.println("Status: REGISTERED");
						    }
    
	  // Storing the details of the other nodes received from Bootstrap Server
		    
		    if ( BS_Response[3].equals("1") )
			    {
			    	String Node_1 = BS_Response[4]+":"+BS_Response[5];
			    	Node_info.add(Node_1);
				    }else if(BS_Response[3].equals("2"))
					    {
				    	String Node_1 = BS_Response[4]+":"+BS_Response[5];
				    	Node_info.add(Node_1);
				    	
				    	String Node_2 = BS_Response[6]+":"+BS_Response[7];
				    	Node_info.add(Node_2);
					    	
						    }else if(BS_Response[3].equals("3"))
							    {
						    	String Node_1 = BS_Response[4]+":"+BS_Response[5];
						    	Node_info.add(Node_1);
						    	
						    	String Node_2 = BS_Response[6]+":"+BS_Response[7];
						    	Node_info.add(Node_2);
						    	
						    	String Node_3 = BS_Response[8]+":"+BS_Response[9];
						    	Node_info.add(Node_3);
							    	
							    }
	
        
    client_Socket.close();
	} catch( IOException a) {
		System.err.println("IOError in reg: "+a);
	}
}

	public void join(int Node_port) {	
		
		try {
			
			ArrayList<String> Sock1_RTDetails = new ArrayList<String>();
			ArrayList<String> Sock2_RTDetails = new ArrayList<String>();
			ArrayList<String> Sock3_RTDetails = new ArrayList<String>();
		
		DatagramSocket client_Socket = new DatagramSocket();

			// Joining the local node with the known nodes from BS
			
				String Node_IP = InetAddress.getLocalHost().getHostAddress();
				String join_req = "JOIN" + " " + Node_IP + " " + Node_port;
				int len = join_req.length() + 4;
				String join_msg = String.format("%04d", len) + " " +join_req;
				byte[] join_request = join_msg.getBytes();
			  
			int noIP = Node_info.size();
			
			if(noIP == 0) 
				{ 
					System.out.println("this is the first node");			
					} else{
					
				// sending JOIN message to the nodes	
					for(int k=0; k<noIP; k++){	
						
						String[] SockADD= Node_info.get(k).split(":");
						System.out.println(new String(join_request));
						DatagramPacket JOIN_Packet = new DatagramPacket(join_request, join_request.length, InetAddress.getByName(SockADD[0]), Integer.parseInt(SockADD[1]));
						client_Socket.send(JOIN_Packet);
					}	
					
				// receiving JOIN response
					
					byte[] JOIN_response = new byte[65000];
					
					int i=0;
					
					for(int n=0; n<noIP; n++)
					{
							DatagramPacket JResponse = new DatagramPacket(JOIN_response, JOIN_response.length);
						    client_Socket.receive(JResponse);
						    
						    String JR = new String(JResponse.getData(),0,JResponse.getLength());
						    
						    String[] S_JR= JR.split(" ");
						    if(S_JR[2].equals("0")){	    	
						    	System.out.println("Status: Join Successful with " + JResponse.getAddress().toString());	    	
						    	String socket = JResponse.getAddress().toString()+":"+ Integer.toString(JResponse.getPort());
						    	i=i+1;
						    		if (i==1){
						    			unstructpp.routingTable.put(socket, Sock1_RTDetails);
						    			}else if (i==2){
						    				unstructpp.routingTable.put(socket, Sock2_RTDetails);
							    			} else if (i==3){
							    				unstructpp.routingTable.put(socket, Sock3_RTDetails);
							    				}
						    		
						    		System.out.println("map is "+ unstructpp.routingTable);
						    		
						    		
							    	}else if(S_JR[2].equals("9999")){
							    			System.err.println("Error: while adding new node to routing table");
									    }else {
									    	System.out.println("Status: "+ JR);
									    }	   
					}client_Socket.close();}

		} catch(IOException i) {
			System.err.println("Error: IOError in join: ");
		} catch(NullPointerException n) {
			System.err.println("Error: This is the fist node ");
		}

}

	public void unReg(InetAddress BS_ip, int BS_port, int unRegPort)  {
		try {
			
			DatagramSocket client_Socket = new DatagramSocket();
			
					String unRegIP= InetAddress.getLocalHost().getHostName();
					String request =  "DEL IPADDRESS" + " " + unRegIP + " " + unRegPort + " " + client.uname ;					
					int msg_len =  request.length() + 5;
					String del_msg = String.format("%04d", msg_len) + " " +request;
					byte[] del_request = del_msg.getBytes();
					
				// Sending the unregister request to the BS
					
					DatagramPacket DEL_Packet = new DatagramPacket(del_request, del_request.length, BS_ip, BS_port);
					client_Socket.send(DEL_Packet);
					
				//receiving node socket address from BS
					
					byte[] BS_response = new byte[65000];
				    DatagramPacket BSResponse = new DatagramPacket(BS_response, BS_response.length);
				    client_Socket.receive(BSResponse); 
				    
				    String BSR = new String(BSResponse.getData(),0,BSResponse.getLength());
				    String[] BS_Response = BSR.split(" ");
				    
				    if (BS_Response[5].equals("9998")){
					    	System.err.println("Error: Not registered for the given user name" );
					    }else if(BS_Response[4].equals("-1")){
					    	System.out.println("Error: Error in DEL command ");
					    }else if(BS_Response[7].equals("1")){
					    	System.out.println("Status: UNREGISTERED");
					    }else{
					    	System.out.println("The DEL response received from BS: "+ BSR);
					    }
				            
				    client_Socket.close();
		
		}catch (IOException b) {
			System.err.println("IOError in join: "+b);
		}
			
		}
	
	public void leave(InetAddress BS_ip, int BS_port,int Node_port)  {
	 try {	
		DatagramSocket client_Socket = new DatagramSocket();
				
		//// leave from the node in the table
		
		String leaveip = InetAddress.getLocalHost().getHostAddress();

	    System.out.println("Map is "+ unstructpp.routingTable);
		
		String leaveMsg= " LEAVE "+leaveip+ " "+Node_port;
		String leaveReq= String.format("%04d", (leaveMsg.length()+4))+ leaveMsg ;
		byte[] reqLeave = leaveReq.getBytes();
		
		Set<String> RTKeys= unstructpp.routingTable.keySet();
		String[] sockAddkey = new String[RTKeys.size()];
		RTKeys.toArray(sockAddkey);
		int count =0;
		for(int i=0;i<unstructpp.routingTable.size();i++)
			{
				String[] sockAdd = sockAddkey[i].split(":");
				InetAddress ip= InetAddress.getByName(sockAdd[0]);
				int port = Integer.parseInt(sockAdd[1]);
				
				DatagramPacket leavePacket = new DatagramPacket(reqLeave, reqLeave.length, ip, port);
				client_Socket.send(leavePacket);
				
				byte[] leavrResponse = new byte[65000];
			    DatagramPacket leaveRes = new DatagramPacket(leavrResponse, leavrResponse.length);
			    client_Socket.receive(leaveRes);
			    
			    String leave = new String(leaveRes.getData(),0,leaveRes.getLength());
			    String[] msgLeave = leave.split(" ");
			    
			    if (msgLeave[2].equals("0")) {
			    	System.out.println("Status: Leave ok from "+leaveRes.getAddress().toString());
			    	unstructpp.routingTable.remove(sockAddkey[i]);
			    	count+=count;
			    	}else if(msgLeave[2].equals("9999")) {
			    		System.err.println("Error: Leave is unsuccessful from "+leaveRes.getAddress().toString());
			    	}
				
			}
		if(count==unstructpp.routingTable.size()){
			 System.out.println("Status: Leave successful");
			 
///////////////////////////////////////////////////////////////////////////////////////////////////////////
//leaving from BS
///////////////////////////////////////////////////////////////////////////////////////////////////////////		

			String request =  "DEL IPADDRESS" + " " + leaveip + " " + Node_port + " " + client.uname ;					
			int msg_len =  request.length() + 5;
			String del_msg = String.format("%04d", msg_len) + " " +request;
			byte[] del_request = del_msg.getBytes();
			
			// Sending the unregister request to the BS

			DatagramPacket DEL_Packet = new DatagramPacket(del_request, del_request.length, BS_ip, BS_port);
			client_Socket.send(DEL_Packet);
			
			//receiving node socket address from BS

			byte[] BS_response = new byte[65000];
			DatagramPacket BSResponse = new DatagramPacket(BS_response, BS_response.length);
			client_Socket.receive(BSResponse); 
			
			String BSR = new String(BSResponse.getData(),0,BSResponse.getLength());
			String[] BS_Response = BSR.split(" ");

			if (BS_Response[5].equals("9998")){
					System.err.println("Error: Not registered for the given user name" );
				}else if(BS_Response[4].equals("-1")){
						System.out.println("Error: Error in DEL command ");
					}else if(BS_Response[7].equals("1")){
						System.out.println("Status: UNREGISTERED from BS");
						}else{
							System.out.println("The DEL response received from BS: "+ BSR);
							}
///////////////////////////////////////////////////////////////////////////////////////////////////////////
			 
			}else{
				System.out.println("Failed to delete the Routing table");}
			        
		client_Socket.close();
		}catch (SocketException s) {
		 System.err.println("Socket Error in leave: "+s);
		}catch (UnknownHostException unh) {
		 System.err.println("Unknown Host Error in leave: "+unh);		 
		}catch (IOException unh) {
			 System.err.println("Socket Error in leave: "+unh);
			 }		
	}
	
	public void resourceAllocation(InetAddress BS_ip, int BS_port,int Node_port) throws IOException{
		String request =  "GET IPLIST" + client.uname ;
		int msg_len =  request.length() + 5;
		String getIplist_msg = String.format("%04d", msg_len) + " " +request;
		byte[] getIplist_request = getIplist_msg.getBytes();
	
		DatagramSocket client_Socket = new DatagramSocket();	
	
		//sending the register request to the BS
			
		DatagramPacket getIplist_Packet = new DatagramPacket(getIplist_request, getIplist_request.length, BS_ip, BS_port);
		client_Socket.send(getIplist_Packet);
		
		byte[] BS_response = new byte[65000];
	    DatagramPacket BSResponse = new DatagramPacket(BS_response, BS_response.length);
	    client_Socket.receive(BSResponse); 
	
	    String BSR = new String(BSResponse.getData(),0,BSResponse.getLength());
		String[] getIplist = BSR.split(" ");
		
		File file = new File("resources.txt");
		FileInputStream fis = new FileInputStream(file);
		byte[] bfis = new byte[(int) file.length()]; 
		fis.read(bfis); fis.close();
		String s1 = new String(bfis);
		String[] ss = s1.split("\n");
		
		int noIp=((getIplist.length)-6)/2;
		int d=6; int h=3;
		for(int c=0;c<noIp;c++){
			
			String data = new String();
			data = "length NodeResources ";
			int i=0;
			while(i<(160/noIp)){ 
				  data=data+" "+ss[h];
				  h++; i++;
			}data.trim();
			byte[] nodeResources =data.getBytes() ;
			
			//sending the register request to the BS
				
			DatagramPacket Packet = new DatagramPacket(nodeResources, nodeResources.length, BS_ip, BS_port);
			client_Socket.send(Packet);
			d=d+2;
			
			
		}client_Socket.close();
	}
	
	public void query(int Node_port) throws IOException{
		System.out.println("Give the value of s for Zip'f distribution(0.7/0.8): ");
		String s = System.console().readLine();
		
		int keyIndex;
		String searchKey;
		
		File file = new File("resources.txt");
		FileInputStream fis = new FileInputStream(file);
		byte[] bfis = new byte[(int) file.length()]; 
		fis.read(bfis); fis.close();
		String s1 = new String(bfis);
		String[] ss = s1.split("\n");
		
		int n = 160;
		ZipfDistribution zf = new ZipfDistribution(n,Double.parseDouble(s));
		int noQueries = 350;
		
		for(int i=0;i<noQueries;i++){
			keyIndex = zf.sample() - 1;
			
			if(keyIndex < 0){
				keyIndex = 0;
			}
			if(keyIndex > ss.length){
				keyIndex = ss.length-1;
			}
			searchKey = ss[keyIndex];
////////////////////////////////////////////////////////////////////////////////////////////
// First searching for the resources in the current node
////////////////////////////////////////////////////////////////////////////////////////////
			int rFound=0; String res=new String();
			for(int a=0;a<server.nodeResource.size();a++){
				String resources= server.nodeResource.get(a);
				if(resources.contains(searchKey))
					{	rFound=rFound+1;
						res = res+" "+resources;}
					}res.trim();
//////////////////////////////////////////////////////////////////////////////////////////
			if(rFound>0){
		// if found in the current node
				System.out.println("Status: Resources found in the current node "+res);
				
			}else{
	//  if not found in the current node sending the request to the other nodes			
			int hops = 0;	
			int timeToLive = 80;
			String searchRequest = " SER " +InetAddress.getLocalHost().getHostAddress() + " " + Node_port + " " +searchKey + " " +hops+ " " + timeToLive;
			String sRequest = String.format("%04d", searchRequest.length() + 4) + " " +searchRequest;
			
			byte[] bsearchKey = sRequest.getBytes();

			Set<String> RTKeys= unstructpp.routingTable.keySet();
			String[] sockAddkey = new String[RTKeys.size()];
			RTKeys.toArray(sockAddkey);
			
			for(int ii=0;ii<unstructpp.routingTable.size();ii++)
			{
				try{
				DatagramSocket sock = new DatagramSocket();
				String[] sockAdd = sockAddkey[ii].split(":");
				InetAddress ip= InetAddress.getByName(sockAdd[0]);
				int port = Integer.parseInt(sockAdd[1]);

				DatagramPacket search_Packet = new DatagramPacket(bsearchKey, bsearchKey.length, ip, port);
				sock.send(search_Packet);
				
				System.out.println("Status: Waiting for the resource to be found!");
				byte[] search_response = new byte[65000];
			    DatagramPacket SearchResponse = new DatagramPacket(search_response, search_response.length);
			    sock.receive(SearchResponse);
			    sock.setSoTimeout(3600);
			    String SR = new String(SearchResponse.getData(),0,SearchResponse.getLength());
			    String[] search = SR.split(" ");
			    int no_files = Integer.parseInt(search[2]);
			    int found = 0;
			    if(no_files>=1){
			    	System.out.println("Status:Resource found at " + SearchResponse.getSocketAddress().toString());
			    	found += found;
			    	System.out.println("Resource found in "+ search[5] +" hops!");
			    }else if(no_files==0){
			    	System.out.println("Status: Resource not found at " + SearchResponse.getSocketAddress().toString());
			    }else if(no_files==9999){
			    	System.out.println("Error: Node Unreachable");
			    }else {
			    	System.out.println("Other Error!");
			    }
			    System.out.println("Status: "+ searchKey + "found at" + found + " nodes" );
			    sock.close();
				}catch(SocketTimeoutException e){
					System.out.println("Error: Node Unreachable!");
				}  
			}
			}
		}
	}
}