import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.io.*;


public class BananaBankServer {
		
		static int max_permits = 10000;
		static BananaBank bb;
		static Collection<Account>  allAccounts;
		
		
		
		static boolean shutdown = false;
		static Semaphore available = new Semaphore(max_permits, true);
		
    	
		
		public static void main(String[] args) throws IOException, InterruptedException {

			bb = new BananaBank("accounts.txt");

				
			allAccounts = bb.getAllAccounts();
			

			
	    	int portNumber = 2000;

	    	ServerSocket serverSocket= new ServerSocket(portNumber);
	    	serverSocket.setSoTimeout(100);
	    	
	    	
	    	
	    	
	    	try {
	    		while (!shutdown){

	    			try{
	    				Socket clientSocket = serverSocket.accept();
	    				BananaBankServerWorkerThread bbswt = new BananaBankServerWorkerThread(clientSocket);
		    			bbswt.start();
	    			}catch (SocketTimeoutException e){
	    				//if server timed out, do nothing
	    			}
	    			
	    		}
	        } catch (IOException e) {
	        	
	        	e.printStackTrace();
	        }
	    	
	    	while (available.availablePermits() != max_permits){}//waits for threads to stop
	    	
	    	bb.save("accounts.foo");
	    	
	    	serverSocket.close();
	    
		}	

	}


