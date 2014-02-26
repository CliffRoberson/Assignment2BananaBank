import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class SimpleClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
				String hostName = args[0];
				
				int portNumber = 2000;//Integer.parseInt(args[1]);
				boolean shutdown = false;

				try {
				    Socket echoSocket = new Socket(hostName, portNumber);
				    
				    PrintStream out = new PrintStream(echoSocket.getOutputStream(), true);
				    
				    Scanner in = new Scanner(echoSocket.getInputStream());
				    
				    Scanner stdIn = new Scanner(System.in);
			    
				    String userInput = "foobar";
				    
				    while (!userInput.equals("exit") && !echoSocket.isClosed()){
				    	
				    	userInput = stdIn.nextLine();
				    	
				    	if (userInput == System.lineSeparator()){
				    		
				    		continue;
				    	}
				    	
					    out.print(userInput + "\n");
					    //out.flush();
					
					    String input;
					    while (in.hasNextLine()){
					    	input = in.nextLine();
					    	if (input.equals("shutdown")){
					    		shutdown = true;
					    		break;
					    	}
					    	if (input.equals("done")){
					    		break;
					    	}
					    	System.out.println(input);
					    	
						}
					    
					    if (shutdown){
					    	break;
					    }
					 

				    }
				
					
				    echoSocket.close();
				    in.close();
				    out.close();
				    stdIn.close();
				    
				}	catch (IOException e) {
		            System.err.println("???");
		            System.exit(1);
		        }
				
			}
		}
