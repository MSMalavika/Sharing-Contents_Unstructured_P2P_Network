//package UnstructuredP2P;

import java.io.IOException;
import java.net.InetAddress;

public class client  {
	
	//public String option;
	public int NP, Boot_port;
	public InetAddress BSIP;
	
	static command cmd = new command();
	
	   public client(int NP, InetAddress BSIP, int Boot_port)
	   {
	      this.NP = NP;
	      this.BSIP = BSIP;
	      this.Boot_port = Boot_port;
	     
	   }
	   
	 public void run() {
		 
		 System.out.println("Give the command  for client");			
		 String option = System.console().readLine();
					
			//System.out.println("option is "+option);
					switch (option){
					
					case "REG":						
						System.out.println("Give the username with which you want to register ");						
						String uname =System.console().readLine();						
							
							try {
								cmd.REG(BSIP,Boot_port,NP,uname);
							} catch (IOException e) {
								
								System.err.println(e);
							}
							break;
							
					case "JOIN":
						
							try {
								cmd.join(NP);
							} catch (IOException e) {
								
								System.err.println(e);
							}
						break;
					
					case "UNREGISTER":
							System.out.println("If you want to unregister a node give: <IP:Port> <user name>");	
							System.out.println("If you want to unregister the entire network give: <user name>");
							String uname1 =System.console().readLine();
							cmd.DEL(BSIP, Boot_port,NP, uname1);
						
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
