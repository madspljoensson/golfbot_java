package golfbot1;

import java.io.*;
import golfbot1.NextMove.*;
import java.net.*;
import org.json.JSONObject;

public class ServerTest2 {
    public static void main(String[] args) throws IOException {
        String host = "localhost";  // Change to the appropriate host if needed
        int port = 5000;  // Change to the appropriate port if needed
        int portNumber = 10000;

        //EV3Skeleton robot = new EV3Skeleton();

        //NextMove nextMove = new NextMove();

        System.out.println("Server is listening on port " + portNumber);
        //robot.forwardWheelEntrance();
        
        try (
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            

            while (true) {
                try {
                	String inputLine;
                	while ((inputLine = in.readLine()) != null) {
                		JSONObject jsonObj = new JSONObject(inputLine);

                        //nextMove = nextMove.fromJson(inputLine);
                        //System.out.println("Next ball: " + nextMove.getNextBall());
                        //System.out.println("Move type: " + nextMove.getMoveType());
                        //System.out.println("Robot coords: " + nextMove.getRobotCoords());
                        //System.out.println("Robot angle: " + nextMove.getRobotAngle());
                        
                        //robot.setPose(nextMove.getRobotCoords()[0], nextMove.getRobotCoords()[1], nextMove.getRobotAngle());
                        //robot.goTo(nextMove.getNextBall()[0], nextMove.getNextBall()[1]);
                        //robot.goalEjectBall();                      
                	}

                	// When inputLine is null, close the current resources.
                	out.close();
                	in.close();
                	clientSocket.close();

                	// Print a message to indicate that the client has disconnected.
                	System.out.println("Client has disconnected.");
                	
                	break;
                	

                    

                } catch (IOException e) {
                    System.out.println("Exception caught when trying to listen on port "
                        + portNumber + " or listening for a connection");
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber);
            System.out.println(e.getMessage());
        } finally {
        	
        }
        
        try {
            // Attempt to create a socket object and connect to the server
            Socket socket = new Socket(host, port);
            // Create input and output streams for the socket
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Send messages to the server
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            String message = "hello";
            output.println(message);

            // Close the connection
            socket.close();
        } catch (IOException e) {
            System.out.println("Connection failed. Retrying...");
            try {
                // Wait for a while before retrying connection
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
