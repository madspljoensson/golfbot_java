package golfbot1;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

import golfbot1.NextMove.*;
import org.json.JSONObject;

public class JavaClient
{
    private final static String HOSTNAME = "localhost";
    private final static int PORT = 5000;

    public static void main(String[] args) throws IOException, InterruptedException
    {
    	NextMove nextMove = new NextMove();
        try (Socket clientSocket = new Socket(HOSTNAME, PORT);
             InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
             BufferedReader in = new BufferedReader(isr);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) { 
            System.out.println("Connected to " + HOSTNAME + " on port " + PORT);

        	while (true) {
        		try {
                    String data = "ready";
                    out.println(data);
                    
                    String line;
                    while ((line = in.readLine()) != null) {
                    	
                    	JSONObject jsonObj = new JSONObject(line);
                        nextMove = nextMove.fromJson(line);
                        nextMove.getNextBall();
                        nextMove.getMoveType();
                        nextMove.getRobotCoords();
                        nextMove.getRobotAngle();
                        TimeUnit.SECONDS.sleep(1);
                        out.println(data);
                    }
                    
                } catch (IOException e) {
                    System.out.println("Exception caught when trying to listen on port "
                            + PORT + " or listening for a connection");
                        System.out.println(e.getMessage());
                  }
        	}        	
        }
    }
}
