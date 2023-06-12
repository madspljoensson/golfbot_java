package golfbot1;

import java.io.*;
import golfbot1.NextMove.*;
import java.net.*;
import org.json.JSONObject;

public class ServerTest2 {
    public static void main(String[] args) throws IOException {
        int portNumber = 10000;

        EV3Skeleton robot = new EV3Skeleton();

        NextMove nextMove = new NextMove();

        System.out.println("Server is listening on port " + portNumber);
        robot.forwardWheelEntrance();
        
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

                        nextMove = nextMove.fromJson(inputLine);
                        System.out.println("Next ball: " + nextMove.getNextBall());
                        System.out.println("Move type: " + nextMove.getMoveType());
                        System.out.println("Robot coords: " + nextMove.getRobotCoords());
                        System.out.println("Robot angle: " + nextMove.getRobotAngle());
                        
                        robot.setPose(nextMove.getRobotCoords()[0], nextMove.getRobotCoords()[1], nextMove.getRobotAngle());
                        robot.goTo(nextMove.getNextBall()[0], nextMove.getNextBall()[1]);
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
    }
}
