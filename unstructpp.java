//package UnstructuredP2P;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class unstructpp extends Thread {

	 static command cmd = new command();
	 public  String Nodeport;
	 public unstructpp(String Nodeport) throws SocketException // constructor for the class rcmdd_TCP
		{
			this.Nodeport = Nodeport;
		}
	
	public static void main(String[] args){
		
	try{			
			String NP = args[0];
		/*unstructpp daemonThread = new unstructpp(NP);
		daemonThread.setDaemon(true);
		daemonThread.start();*/		
						
				String BSIP = args[1];
				String Boot_port =args[2];		
				String option = args[3];
					
					int Node_port = Integer.parseInt(NP);		
					InetAddress BS_ip = InetAddress.getByName(BSIP);		
					int BS_port = Integer.parseInt(Boot_port);
					
					if ((BS_port<5000 || BS_port>65535) || (Node_port < 5000 || Node_port > 65535))
						{
							System.out.println("Error: Port range, Range: 5000-65535");
							System.exit(1);
						}
					
					// start the server thread 
					
					// start the client thread 
					
				
					
					System.out.println("option is "+option);
				switch (option){
				case "REG":
					System.out.println("reg case");
						String uname = args[4];							
						cmd.REG(BS_ip,BS_port,Node_port,uname);
						break;
				case "JOIN":
					System.out.println("join case");
					cmd.join(Node_port);
					break;		
				case "h":
						
						// help
						break;
				default:
						System.out.println("Error: Given command is unknown");
						System.out.println("Usage: unstructpp <Node port> <bootstrap ip> <bootstrap port> <option>");
						System.out.println("option: h(help) or REG(register)");	
						break;
					}	
			
		}catch(ArrayIndexOutOfBoundsException e)
			{	System.err.println("Error: Missing arguments");
				System.out.println("Usage: unstructpp <Node port> <bootstrap ip> <bootstrap port> <option>");
				System.out.println("option: h(help) or REG(register)");
			}
		catch(NumberFormatException e)
			{
				System.err.println("Error: Non-integer port number");
			}
		catch(UnknownHostException e)
			{
				System.err.println("Error: Check IP Address format");
			}
		catch(IOException e)
			{
				System.err.println(e);
			}
		
	}		

		public void run(){
			try {
				DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(Nodeport));
				
				while (true){
					byte[] reqData = new byte[65000];
					
				// Receiving a request from other nodes
		            DatagramPacket incomming_req = new DatagramPacket(reqData, reqData.length);		            
		            serverSocket.receive(incomming_req);
		            InetAddress reqIP = incomming_req.getAddress();
		            int reqPort = incomming_req.getPort();
		            String inRequest = new String(incomming_req.getData(),0,incomming_req.getLength());
		            
		            String[] server_req = inRequest.split(" ");
		            String server_cmd = server_req[1];
		            
		            switch (server_cmd){
		            case "JOIN":
		            	
		            	String joinmsg= "0014 JOINOK 0"; 
		            	byte[] joinOkmsg = joinmsg.getBytes();
		         // Rending the JOINOK response
						DatagramPacket join_response = new DatagramPacket(joinOkmsg, joinOkmsg.length, reqIP, reqPort);
						serverSocket.send(join_response);
		            	
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
