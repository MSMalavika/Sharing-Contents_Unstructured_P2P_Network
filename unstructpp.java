package UnstructuredP2P;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class unstructpp  {	
	
	public static void main(String[] args){
		
	try{			
				String NP = args[0];
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
					server serverThread = new server(Node_port);	 
					serverThread.start();
					
					// start the client thread 
				      client clientThread = new client(Node_port,BS_ip, BS_port,option);
				      clientThread.start();
				      
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
		
		
	}		

		
}
