package UnstructuredP2P;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;


public class client extends Thread {
	
	public String option;
	public int NP, Boot_port;
	public InetAddress BSIP;
	
	static command cmd = new command();
	
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
						
						Scanner uName = new Scanner(System.in);
						String uname =uName.next();							
							
							try {
								cmd.REG(BSIP,Boot_port,NP,uname);
							} catch (IOException e) {
								
								System.err.println(e);
							}
							break;
					case "JOIN":
						System.out.println("join case");
							try {
								cmd.join(NP);
							} catch (IOException e) {
								
								System.err.println(e);
							}
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
