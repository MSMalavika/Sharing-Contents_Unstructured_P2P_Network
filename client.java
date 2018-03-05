package UnstructuredP2P;

import java.io.IOException;
import java.net.InetAddress;

public class client  {
	
	//public String option;
	public int NP, Boot_port;
	public InetAddress BSIP;
	static String uname;
	
	static command cmd = new command();
	
	   public client(int NP, InetAddress BSIP, int Boot_port)
	   {
	      this.NP = NP;
	      this.BSIP = BSIP;
	      this.Boot_port = Boot_port;
	     
	   }
	   
	 public void run() throws IOException {
		 
		 System.out.println(" Initilize your network with a username");			
		 uname = System.console().readLine();
		 
		 System.out.println("Give the command  for client");			
		 String option = System.console().readLine();
					
			//System.out.println("option is "+option);
					switch (option){
					
					case "REG":							
						cmd.reg(BSIP,Boot_port,NP);
						break;
							
					case "JOIN":						
						cmd.join(NP);
						break;
					
					case "UNREGISTER":							
							cmd.unReg(BSIP, Boot_port,NP);
							break;
					
					case "LEAVE":						
						cmd.leave(BSIP, Boot_port,NP);
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
