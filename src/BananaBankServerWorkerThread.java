import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.*;

public class BananaBankServerWorkerThread extends Thread {

Socket clientSocket = new Socket();
private double amount = 0;
private double src = 0;
private double dest = 0;
private boolean done = false;
//private static Object lock1 = new Object();

	public BananaBankServerWorkerThread (Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	

	public void run(){
		Scanner in = null;
		PrintStream out = null;
        
		try{
       		BananaBankServer.available.acquire();
        	
        	   
			in = new Scanner(clientSocket.getInputStream());
       	    out = new PrintStream(clientSocket.getOutputStream(), false);
		        	    
        		while (true){
        			
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
				            
				            	
				            		for (Account a : BananaBankServer.allAccounts){
					            		if (a.getAccountNumber() == src){
					            			if ((a.getBalance() - amount) <= 0){
					            				out.println("Failure -- not enough money");
					            			}
					            			else {
					            				Account tmp = BananaBankServer.bb.getAccount((int)dest);
					            				
					            				//http://stackoverflow.com/questions/5151266/how-to-avoid-nested-synchronization-and-the-resulting-deadlock
					            				int srcHash = System.identityHashCode(a);
					            				int destHash = System.identityHashCode(tmp);
					            				
					            				if (srcHash < destHash){
					            					synchronized(a){
						            					synchronized(tmp){
							            					a.transferTo((int) amount, tmp);
							            				}
						            				}
					            				}
					            				else {
					            					synchronized(tmp){
						            					synchronized(a){
							            					a.transferTo((int) amount, tmp);
							            				}
						            				}
					            				}
					            				
					            				
					            				
				            					out.println("Success, " + (int)amount +" transfered from account " + (int)src + " to " + (int)dest +".");

					            			}
					            			done = true;
					            			break;
					            		
					            		}
					            		
					            	}
				           		
				            	//}

				            	if (!done){
				            		out.println("Account number not found");
				            	}
		            	}else{
		            		out.println("Failure, invalid command");
		            	}
		        
		            
		            out.println("done");
		           	//out.flush();

		            }
		            
		          
		           
		            
		            
        		
        		
	            
		}catch(IOException | InterruptedException e){
			e.printStackTrace();
            

		}catch(NoSuchElementException e){
			//do nothing, thrown when socket closed
		}finally{
				in.close();
				out.close();
            	BananaBankServer.available.release();            
		}
		}
}
	

