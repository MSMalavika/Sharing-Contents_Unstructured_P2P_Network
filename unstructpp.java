//package UnstructuredP2P;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;

public class unstructpp extends Thread  {	
	
	static Hashtable<String, ArrayList<String>> routingTable = new Hashtable<String, ArrayList<String>>();
	static ArrayList<String> RTDetails = new ArrayList<String>();
	
	public static void main(String[] args){
		
	try{			
				String NP = args[0];
				String BSIP = args[1];
				String Boot_port =args[2];		
				
				if (args.length==3) {
					
					int Node_port = Integer.parseInt(NP);		
					InetAddress BS_ip = InetAddress.getByName(BSIP);		
					int BS_port = Integer.parseInt(Boot_port);
					
					if ((BS_port<5000 || BS_port>65535) || (Node_port < 5000 || Node_port > 65535))
						{
							System.out.println("Error: Port range, Range: 5000-65535");
							System.exit(1);
						}
					
					// Server thread 					
						serverThread(Node_port);
					
					// Client thread				      
						clientThread(Node_port,BS_ip, BS_port);
						
				} else if(args.length==4) {
					
					String option = args[3];
					
					if(option.equals("h")) {
						System.out.println("help");
					} else {
						System.out.println(" Wrong commad try using help : unstructpp <Node port> <bootstrap ip> <bootstrap port> h");
					}
					
				}else {
					System.out.println("Oops something went wrong");
				}
					
				      
		}catch(ArrayIndexOutOfBoundsException e)
			{	System.err.println("Error: Missing arguments, See help for correct instructions  ");				
				System.out.println("Usage: unstructpp <Node port> <bootstrap ip> <bootstrap port> h");				
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
	
	public int NP, Boot_port;
	public InetAddress BSIP;
	
	static command cmd = new command();	

	public static void clientThread(int NP, InetAddress BSIP, int Boot_port) {
		Thread T= (new Thread() {
        	public void run() {
        		
        		client clientMode = new client(NP,BSIP,Boot_port);
        		try {
					clientMode.run();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        			   
        		   }
        	
        });T.setDaemon(true);T.start();
	}
        
    public static void serverThread(int Nodeport) {
        (new Thread() {   
        	public void run(){
    			
        		server serverMode = new server(Nodeport);
        		serverMode.run();
    		}
        	
        }).start();
    }

    
}
