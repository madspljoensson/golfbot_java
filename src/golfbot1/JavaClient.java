package golfbot1;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

import golfbot1.NextMove.*;
import org.json.JSONObject;

import lejos.robotics.geometry.Point;
import lejos.robotics.navigation.Pose;

public class JavaClient
{
    private final static String HOSTNAME = "192.168.181.232";
    private final static int PORT = 10000;

    public static void main(String[] args) throws IOException, InterruptedException
    {
		
    	EV3Skeleton robot = new EV3Skeleton();
    	NextMove nextMove = new NextMove();
    	
    	final float MOVE1 = (float) 0.18;
    	final float MOVE2 = (float) -0.02;
    	
    	final float MOVE1EDGE = (float) 0.15;
    	final float MOVE2EDGE = (float) -0.06;
    	
    	final float maxDist = (float) 0.80;
    	final float precentageMove = (float) 0.80;
    	
    	int counter = 0;

        try (Socket clientSocket = new Socket(HOSTNAME, PORT);
             InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
             BufferedReader in = new BufferedReader(isr);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) { 
            System.out.println("Connected to " + HOSTNAME + " on port " + PORT);
            robot.forwardWheelEntrance();
            
          

        	while (true) {
        		try {
                    String data = "ready";
                    out.println(data);
                    
                    String line;
                    while ((line = in.readLine()) != null) {
                    	
						robot.forwardWheelEntrance();
						counter++;
                    	
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
                        
//                    	if (counter > -1){
//                    		System.out.println("Going to goal");
//    						robot.goalEjectBall(pose);
//    						counter=0;
//    						
//    						break;
//                    	}
                
                		
//                		if (nextMove.getNextBall().length > 0) {
//                			Point destination = new Point(nextMove.getNextBall()[0], nextMove.getNextBall()[1]);
//                			System.out.println("--- EXTRA POINT ---");
//	                        if (pose.distanceTo(destination) > maxDist) {
//	                            destination = pose.getLocation().pointAt(pose.distanceTo(destination) * precentageMove, pose.getHeading());
//	                            robot.goTo(destination.x, destination.y, pose);
//	                        }
//                        
//                        } 
                    	
                    	if (counter < 35) {
                    		switch (nextMove.getMoveType()) {

    						case "none":
    							System.out.println("Going straight to ball");
    							robot.goTo(nextMove.getNextBall()[0], nextMove.getNextBall()[1], pose);
    							break;
    						case "middle_edge":
    							System.out.println("Going middle edge");
    							
    							Pose mePose = robot.makePose(pose, nextMove.getExtraPoint()[0], nextMove.getExtraPoint()[1]);
    							robot.goTo(nextMove.getExtraPoint()[0], nextMove.getExtraPoint()[1], pose);
    							robot.goToEdge1(nextMove.getNextBall()[0],  nextMove.getNextBall()[1], mePose);
    							
    							break;
    						case "middle_corner":
    							System.out.println("Going middle corner");

    							Pose mcPose = robot.makePose(pose, nextMove.getExtraPoint()[0], nextMove.getExtraPoint()[1]);
    							robot.goTo(nextMove.getExtraPoint()[0], nextMove.getExtraPoint()[1], pose);
    							robot.goToCorner(nextMove.getNextBall()[0],  nextMove.getNextBall()[1], mcPose);
    							
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
    							robot.goTo(nextMove.getNextBall()[0] + MOVE1EDGE, nextMove.getNextBall()[1], pose);
    							robot.goToEdge(nextMove.getNextBall()[0] - MOVE2EDGE, nextMove.getNextBall()[1], leftPose);
    							break;
    						case "right_edge":
    							System.out.println("Going to right edge ball");
    							Pose rightPose = robot.makePose(pose, nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1]);
    							robot.goTo(nextMove.getNextBall()[0] - MOVE1EDGE, nextMove.getNextBall()[1], pose);
    							robot.goToEdge(nextMove.getNextBall()[0] + MOVE2EDGE, nextMove.getNextBall()[1], rightPose);
    							break;
    						case "lower_edge":
    							System.out.println("Going to lower edge ball");
    							Pose downPose = robot.makePose(pose, nextMove.getNextBall()[0], nextMove.getNextBall()[1] + MOVE1);
    							robot.goTo(nextMove.getNextBall()[0], nextMove.getNextBall()[1] + MOVE1EDGE, pose);
    							robot.goToEdge(nextMove.getNextBall()[0], nextMove.getNextBall()[1] - MOVE2EDGE, downPose);
    							break;
    						case "upper_edge":
    							System.out.println("Going to upper edge ball");
    							Pose upPose = robot.makePose(pose, nextMove.getNextBall()[0], nextMove.getNextBall()[1] - MOVE1);
    							robot.goTo(nextMove.getNextBall()[0], nextMove.getNextBall()[1] - MOVE1EDGE, pose);
    							robot.goToEdge(nextMove.getNextBall()[0], nextMove.getNextBall()[1] + MOVE2EDGE, upPose);
    							break;
    						case "goal":
                        		System.out.println("Going to goal");
        						robot.goalEjectBall(pose);
        						break;

    						
    						}
                    	} else {
                    		float maxDistGoal = (float) 0.15;
                            
                            Point leftGoalPoint = new Point(robot.LEFT_GOAL_POINT[0], robot.LEFT_GOAL_POINT[1]);
                            Point rightGoalPoint = new Point(robot.RIGHT_GOAL_POINT[0], robot.RIGHT_GOAL_POINT[1]);
                            
                            Point botRightAnchor = new Point((float) (robot.COURSE_WIDTH*(1/4)), (float) (robot.COURSE_HEIGHT*(3/4)));
                            Point botLeftAnchor = new Point((float) (robot.COURSE_WIDTH*(3/4)), (float) (robot.COURSE_HEIGHT*(3/4)));
                            Point topRightAnchor = new Point((float) (robot.COURSE_WIDTH*(1/4)), (float) (robot.COURSE_HEIGHT*(1/4)));
                            Point topLeftAnchor = new Point((float) (robot.COURSE_WIDTH*(1/4)), (float) (robot.COURSE_HEIGHT*(1/4)));
                            

//                            System.out.println(robot.pose.getX());
//
//                            System.out.println(robot.COURSE_WIDTH);
                            
                            if (nextMove.getRobotCoords()[0] < robot.COURSE_WIDTH / 2) {
                            	robot.goalEjectBall(pose, "left");
                            } else {
                            	robot.goalEjectBall(pose, "right");
                            }
                            counter = 0;
                            
                        	
                    	}
                    	
                    	
						
						TimeUnit.MILLISECONDS.sleep(200);
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


