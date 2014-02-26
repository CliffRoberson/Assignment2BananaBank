import java.net.*;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.*;

public class BananaBankServerWorkerThread extends Thread {

Socket clientSocket = new Socket();
private double amount = 0;
private double src = 0;
private double dest = 0;
private boolean done = false;
private static Object lock1 = new Object();

	public BananaBankServerWorkerThread (Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	

	public void run() {
        try {

        		BananaBankServer.available.acquire();
        	
        	    Scanner in = new Scanner(clientSocket.getInputStream());
        	    PrintStream out = new PrintStream(clientSocket.getOutputStream(), false);
        	    
        		while (!out.checkError()){
        			
		            if (in.hasNextLine()){
		            	
		            	String foo = in.nextLine();		            	
		            	
		            	if (foo.equals(null) )
		            	{
		            		break;
		            	}
			            	            
		            	else if ( foo.equals("SHUTDOWN") 
		    					&& (clientSocket.getInetAddress().toString().contains("127.0.0.1")))
		    			{
		    				BananaBankServer.shutdown = true;
		    				while (BananaBankServer.max_permits - BananaBankServer.available.availablePermits() != 1 ){}//waits for threads to stop
		    		    	
		    		    	int total = 0;
		    		    	for (Account a : BananaBankServer.allAccounts){
		    		    		total = a.getBalance() + total;
		    		    	}
		    		    	out.println(total);
		    				break;
		    			}
			            
		            	else if (Pattern.matches(("(\\s*)(\\d+)(\\s*)(\\d+)(\\s*)(\\d+)(\\s*)"), foo)){
		            			Scanner foobar = new Scanner(foo);
		            			amount = foobar.nextDouble();
				            	src = foobar.nextDouble();
				            	dest = foobar.nextDouble();				            	
				            	foobar.close();
				            	//out.println(amount + " " + src + " " + dest + " ");
				            	
				            	synchronized(lock1){
				            	
				            		for (Account a : BananaBankServer.allAccounts){
					            		if (a.getAccountNumber() == src){
					            			if ((a.getBalance() - amount) <= 0){
					            				out.println("Failure -- not enough money");
					            			}
					            			else {
					            				a.transferTo((int) amount, BananaBankServer.bb.getAccount((int) dest));
								           		out.println("Success, " + (int)amount +" transfered from account " + (int)src + " to " + (int)dest +".");

					            			}
					            			done = true;
					            			break;
					            		
					            		}
					            		
					            	}
				           		
				            	}

				            	if (!done){
				            		out.println("Account number not found");
				            	}
		            	}else{
		            		out.println("Failure, invalid command");
		            	}
		        
		            out.println("done");
		           	out.flush();

		            }
		            
        		}
	            BananaBankServer.available.release();

        		in.close();
	            out.close();
	            clientSocket.close();
        			
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
	}
}
