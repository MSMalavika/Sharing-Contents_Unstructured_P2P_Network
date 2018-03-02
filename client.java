package UnstructuredP2P;

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
	   
	 public void run() throws IOException {
		 
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
							
							cmd.DEL(BSIP, Boot_port);
						
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
