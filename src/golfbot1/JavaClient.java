package golfbot1;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

import golfbot1.NextMove.*;
import org.json.JSONObject;
import lejos.robotics.navigation.Pose;

public class JavaClient
{
    private final static String HOSTNAME = "192.168.43.134";
    private final static int PORT = 5000;

    public static void main(String[] args) throws IOException, InterruptedException
    {
		
    	EV3Skeleton robot = new EV3Skeleton();
    	NextMove nextMove = new NextMove();
    	
    	final float MOVE1 = (float) 0.15;
    	final float MOVE2 = (float) 0.00;

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
                    	
						robot.forwardWheelEntrance();
                    	
                    	JSONObject jsonObj = new JSONObject(line);
                        nextMove = nextMove.fromJson(line);
                        nextMove.getNextBall();
                        nextMove.getMoveType();
                        nextMove.getRobotCoords();
                        nextMove.getRobotAngle();
                        
                        Pose pose = new Pose(nextMove.getRobotCoords()[0], nextMove.getRobotCoords()[1],
								nextMove.getRobotAngle());
						robot.setPose(nextMove.getRobotCoords()[0], nextMove.getRobotCoords()[1],
								nextMove.getRobotAngle());
						
						switch (nextMove.getMoveType()) {

						case "none":
							System.out.println("Going straight to ball");
							robot.goTo(nextMove.getNextBall()[0], nextMove.getNextBall()[1], pose);
							break;
						case "lower_left_corner":
							System.out.println("Going to lower left corner ball");
							Pose llPose = robot.makePose(pose, nextMove.getNextBall()[0] + MOVE1, nextMove.getNextBall()[1] + MOVE1);
							robot.goTo(nextMove.getNextBall()[0] + MOVE1, nextMove.getNextBall()[1] + MOVE1, pose);
							robot.goToCorner(nextMove.getNextBall()[0] - MOVE2, nextMove.getNextBall()[1] - MOVE2, llPose);
							break;
						case "lower_right_corner":
							System.out.println("Going to lower right corner ball");
							Pose lrPose = robot.makePose(pose, nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1] + MOVE1);
							robot.goTo(nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1] + MOVE1, pose);
							robot.goToCorner(nextMove.getNextBall()[0] + MOVE2, nextMove.getNextBall()[1] - MOVE2, lrPose);
							break;
						case "upper_left_corner":
							System.out.println("Going to upper left corner ball");
							Pose ulPose = robot.makePose(pose, nextMove.getNextBall()[0] + MOVE1, nextMove.getNextBall()[1] - MOVE1);
							robot.goTo(nextMove.getNextBall()[0] + MOVE1, nextMove.getNextBall()[1] - MOVE1, pose);
							robot.goToCorner(nextMove.getNextBall()[0] - MOVE2, nextMove.getNextBall()[1] + MOVE2, ulPose);
							break;
						case "upper_right_corner":
							System.out.println("Going to upper right corner ball");
							Pose urPose = robot.makePose(pose, nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1] - MOVE1);
							robot.goTo(nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1] - MOVE1, pose);
							robot.goToCorner(nextMove.getNextBall()[0] + MOVE2, nextMove.getNextBall()[1] + MOVE2, urPose);
							break;
						case "left_edge":
							System.out.println("Going to left edge ball");
							Pose leftPose = robot.makePose(pose, nextMove.getNextBall()[0] + MOVE1, nextMove.getNextBall()[1]);
							robot.goTo(nextMove.getNextBall()[0] + MOVE1, nextMove.getNextBall()[1], pose);
							robot.goToEdge(nextMove.getNextBall()[0] - MOVE2, nextMove.getNextBall()[1], leftPose);
							break;
						case "right_edge":
							System.out.println("Going to right edge ball");
							Pose rightPose = robot.makePose(pose, nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1]);
							robot.goTo(nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1], pose);
							robot.goToEdge(nextMove.getNextBall()[0] + MOVE2, nextMove.getNextBall()[1], rightPose);
							break;
						case "lower_edge":
							System.out.println("Going to lower edge ball");
							Pose downPose = robot.makePose(pose, nextMove.getNextBall()[0], nextMove.getNextBall()[1] + MOVE1);
							robot.goTo(nextMove.getNextBall()[0], nextMove.getNextBall()[1] + MOVE1, pose);
							robot.goToEdge(nextMove.getNextBall()[0], nextMove.getNextBall()[1] - MOVE2, downPose);
							break;
						case "upper_edge":
							System.out.println("Going to upper edge ball");
							Pose upPose = robot.makePose(pose, nextMove.getNextBall()[0], nextMove.getNextBall()[1] - MOVE1);
							robot.goTo(nextMove.getNextBall()[0], nextMove.getNextBall()[1] - MOVE1, pose);
							robot.goToEdge(nextMove.getNextBall()[0], nextMove.getNextBall()[1] + MOVE2, upPose);
							break;

						case "goal":
							System.out.println("Going to goal");
							robot.goalEjectBall(pose);
							break;
						}
						TimeUnit.SECONDS.sleep(2);
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
