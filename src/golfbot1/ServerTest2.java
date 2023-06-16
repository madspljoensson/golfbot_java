package golfbot1;

import java.io.*;
import golfbot1.NextMove.*;
import java.net.*;
import org.json.JSONObject;
import lejos.robotics.navigation.Pose;
import lejos.robotics.geometry.Point;

public class ServerTest2 {

	static final float MOVE1 = (float) 0.15;
	static final float MOVE2 = (float) 0.00;

	public static void main(String[] args) throws IOException {
		int portNumber = 10000;

		EV3Skeleton robot = new EV3Skeleton();

		NextMove nextMove = new NextMove();

		System.out.println("Server is listening on port " + portNumber);
		robot.forwardWheelEntrance();

		try (ServerSocket serverSocket = new ServerSocket(portNumber);
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {

			while (true) {
				try {

					String inputLine;
					while ((inputLine = in.readLine()) != null) {

						robot.forwardWheelEntrance();

						JSONObject jsonObj = new JSONObject(inputLine);

						nextMove = nextMove.fromJson(inputLine);
//                        System.out.println("Next ball: " + nextMove.getNextBall());
						System.out.println("Move type: " + nextMove.getMoveType());
//                        System.out.println("Robot coords: " + nextMove.getRobotCoords());
//                        System.out.println("Robot angle: " + nextMove.getRobotAngle());

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
							Pose llPose = makePose(pose, nextMove.getNextBall()[0] + MOVE1, nextMove.getNextBall()[1] + MOVE1);
							robot.goTo(nextMove.getNextBall()[0] + MOVE1, nextMove.getNextBall()[1] + MOVE1, pose);
							robot.goToCorner(nextMove.getNextBall()[0] - MOVE2, nextMove.getNextBall()[1] - MOVE2, llPose);
							break;
						case "lower_right_corner":
							System.out.println("Going to lower right corner ball");
							Pose lrPose = makePose(pose, nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1] + MOVE1);
							robot.goTo(nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1] + MOVE1, pose);
							robot.goToCorner(nextMove.getNextBall()[0] + MOVE2, nextMove.getNextBall()[1] - MOVE2, lrPose);
							break;
						case "upper_left_corner":
							System.out.println("Going to upper left corner ball");
							Pose ulPose = makePose(pose, nextMove.getNextBall()[0] + MOVE1, nextMove.getNextBall()[1] - MOVE1);
							robot.goTo(nextMove.getNextBall()[0] + MOVE1, nextMove.getNextBall()[1] - MOVE1, pose);
							robot.goToCorner(nextMove.getNextBall()[0] - MOVE2, nextMove.getNextBall()[1] + MOVE2, ulPose);
							break;
						case "upper_right_corner":
							System.out.println("Going to upper right corner ball");
							Pose urPose = makePose(pose, nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1] - MOVE1);
							robot.goTo(nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1] - MOVE1, pose);
							robot.goToCorner(nextMove.getNextBall()[0] + MOVE2, nextMove.getNextBall()[1] + MOVE2, urPose);
							break;
						case "left_edge":
							System.out.println("Going to left edge ball");
							Pose leftPose = makePose(pose, nextMove.getNextBall()[0] + MOVE1, nextMove.getNextBall()[1]);
							robot.goTo(nextMove.getNextBall()[0] + MOVE1, nextMove.getNextBall()[1], pose);
							robot.goToEdge(nextMove.getNextBall()[0] - MOVE2, nextMove.getNextBall()[1], leftPose);
							break;
						case "right_edge":
							System.out.println("Going to right edge ball");
							Pose rightPose = makePose(pose, nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1]);
							robot.goTo(nextMove.getNextBall()[0] - MOVE1, nextMove.getNextBall()[1], pose);
							robot.goToEdge(nextMove.getNextBall()[0] + MOVE2, nextMove.getNextBall()[1], rightPose);
							break;
						case "lower_edge":
							System.out.println("Going to lower edge ball");
							Pose downPose = makePose(pose, nextMove.getNextBall()[0], nextMove.getNextBall()[1] + MOVE1);
							robot.goTo(nextMove.getNextBall()[0], nextMove.getNextBall()[1] + MOVE1, pose);
							robot.goToEdge(nextMove.getNextBall()[0], nextMove.getNextBall()[1] - MOVE2, downPose);
							break;
						case "upper_edge":
							System.out.println("Going to upper edge ball");
							Pose upPose = makePose(pose, nextMove.getNextBall()[0], nextMove.getNextBall()[1] - MOVE1);
							robot.goTo(nextMove.getNextBall()[0], nextMove.getNextBall()[1] - MOVE1, pose);
							robot.goToEdge(nextMove.getNextBall()[0], nextMove.getNextBall()[1] + MOVE2, upPose);
							break;

						case "goal":
							System.out.println("Going to goal");
							robot.goalEjectBall(pose);
							break;

//	                        default:
//	                            // Default action when the option does not match any case
//	                            System.out.println("Invalid option");
//	                            
//	                            //TEMP:
//	                            robot.goalEjectBall();
//	                            break;

						}

						// robot.goTo(nextMove.getNextBall()[0], nextMove.getNextBall()[1]);
						// robot.goalEjectBall();

					}

					// When inputLine is null, close the current resources.
					out.close();
					in.close();
					clientSocket.close();

					// Print a message to indicate that the client has disconnected.
					System.out.println("Client has disconnected.");

					break;

				} catch (IOException e) {
					System.out.println("Exception caught when trying to listen on port " + portNumber
							+ " or listening for a connection");
					System.out.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port " + portNumber);
			System.out.println(e.getMessage());
		} finally {

		}
	}
	
	public static Pose makePose(Pose pose, float x, float y) {
		Point p = new Point(x, y);
		float heading = pose.angleTo(p);
		Pose newPose = new Pose(x, y, heading);
		return newPose;
	}
}
