package UnstructuredP2P;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class unstructpp {

	 static command cmd = new command();
	 
	 /// hello pillalu
	
	public static void main(String[] args){
		
	try{			String NP = args[0];		
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
					
					switch (option){
					case "REG":
							String uname = args[4];							
							cmd.REG(BS_ip,BS_port,Node_port,uname);
					case "JOIN":
						cmd.join(Node_port);
							
					case "h":
						
						// help
						
					default:
							System.out.println("Error: Given command is unknown");
							System.out.println("Usage: unstructpp <Node port> <bootstrap ip> <bootstrap port> <option>");
							System.out.println("option: h(help) or REG(register)");							
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
	

}
