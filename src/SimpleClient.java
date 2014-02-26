import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class SimpleClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
				String hostName = args[0];
				
				int portNumber = 2000;//Integer.parseInt(args[1]);

				try {
				    Socket echoSocket = new Socket(hostName, portNumber);
				    
				    PrintStream out = new PrintStream(echoSocket.getOutputStream(), true);
				    
				    Scanner in = new Scanner(echoSocket.getInputStream());
				    
				    Scanner stdIn = new Scanner(System.in);
			    
				    String userInput = "foobar";
				    
				    while (true){
				    	
				    	userInput = stdIn.nextLine();
				    	if (userInput.equals("exit")){
				    		
				    		break;
				    	}
				    	if (userInput.equals("SHUTDOWN")){
				    		out.print(userInput + "\n");
				    		System.out.println(in.nextLine());
				    		break;
				    	}
				    	
				    	if (userInput == System.lineSeparator()){
				    		
				    		continue;
				    	}
				    	
					    out.print(userInput + "\n");
					    //out.flush();
					
					    String input;
					    while (in.hasNextLine()){
					    	input = in.nextLine();
					    	
					    	if (input.equals("done")){
					    		break;
					    	}
					    	System.out.println(input);
					    	
						}
					    
					    
					 

				    }
				    while (!echoSocket.isClosed()){
				    	stdIn.close();
					    out.close();
					    in.close();
					    echoSocket.close();
				    }
				    
				    
				   				    
				}	catch (IOException e) {
		            System.err.println("???");
		            System.exit(1);
		        }
				
			}
		}
