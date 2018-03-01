

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class client extends Thread {
	
	public String option;
	public int NP, Boot_port;
	public InetAddress BSIP;

	   public client(int NP, InetAddress BSIP, int Boot_port, String option)
	   {
	      this.NP = NP;
	      this.BSIP = BSIP;
	      this.Boot_port = Boot_port;
	      this.option = option;
	   }
	   
	 public void run() {
		 
			
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
				
		   
	   }


}
