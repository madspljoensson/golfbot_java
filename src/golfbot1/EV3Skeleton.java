package golfbot1;

import java.util.concurrent.TimeUnit;
import golfbot1.ServerTest2.*;

//from: https://github.com/Ziron/lejos-ev3-examples/blob/ffe1e964862fc6c4145a971c6ed9aca0b7156d8f/EV3Skeleton.java#L167
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.geometry.Point;
import lejos.utility.Delay;


public class EV3Skeleton {

	static final String GOAL_SIDE_RELATIVE_TO_CAMERA = "right";

	static final double COURSE_HEIGHT = 1.235;
	static final double COURSE_WIDTH = 1.683;

	static final float[] LEFT_GOAL_POINT = { (float) 0.14, (float) (COURSE_HEIGHT / 2) };
	static final float[] RIGHT_GOAL_POINT = { (float) (COURSE_WIDTH - 0.14), (float) (COURSE_HEIGHT / 2) };
	
	static final float GOAL_PLUS_LENGTH = (float) 0.10;
	static final float GOAL_MINUS_LENGTH = (float) 0.00;

	// Diameter of a standard EV3 wheel in meters. (medium wheel: 0.056, small
	// wheel: 0.0432 NEW default: 0.0418)
	static final double WHEEL_DIAMETER = 0.0420;

	// Distance between center of wheels in meters. (default for test robot: 0.1725, NEW default: 0.1745)
	static final double WHEEL_SPACING = 0.1735;

	static final int ENTRANCE_ACCELERATION = 10000;
	static final int ENTRANCE_SPEED = 10000;

	// default speed in m/s (default: 0.3)
	static final double PILOT_LINEAR_SPEED = 0.3;
	// default acceleration in m/s^2 (default: 0.9)
	static final double PILOT_LINEAR_ACCELERATION = 0.9;

	// default turn speed in deg/s (default: 180)
	static final double PILOT_ANGULAR_SPEED = 180;
	// default turn acceleration in deg/s^2 (default: 540)
	static final double PILOT_ANGULAR_ACCELERATION = 540;

	// Ports for each wheel
	static final String LEFT_WHEEL_PORT = "A";
	static final String RIGHT_WHEEL_PORT = "D";
	static final String ENTRANCE_WHEEL_PORT1 = "B";
	static final String ENTRANCE_WHEEL_PORT2 = "C";

	// These variables are initialized by initRobot()
	static Brick brick;
	static EV3LargeRegulatedMotor leftMotor;
	static EV3LargeRegulatedMotor rightMotor;
	static EV3MediumRegulatedMotor wheelEntranceMotor1;
	static EV3MediumRegulatedMotor wheelEntranceMotor2;

	// These variables are initialized by initPilot()
	static MovePilot pilot;
	static PoseProvider poseProvider;
	static Navigator navigator;
	static Pose pose;

	/**
	 * Main function of program.
	 */
	public EV3Skeleton() {
		initRobot();
		initPilot();
		initNavigator();
		Delay delay = new Delay();

		System.out.println("Robot ready!");

	}
	
	public void driveTest() {
		
//		navigator.goTo((float)0.2, 0);
//		System.out.println("1");
//		navigator.goTo((float)0.2, (float)-0.2);
//		System.out.println("2");
//		navigator.goTo((float)0, (float)-0.2);
//		System.out.println("3");
//		navigator.goTo((float)0, 0);
//		System.out.println("4");
		
//		testMove((float)0.2, 0, 0, 0, 0);
//		testMove((float)0.2, (float)0.2, (float)0.2, 0, 0);
//		testMove((float)0, (float)0.2, (float)0.2, (float)0.2, 90);
//		testMove((float)0, (float)0, (float)0, (float)0.2, 180);
		
		navigator.rotateTo(0);
	}

	public void forwardWheelEntrance() { // 1200 speed?
		wheelEntranceMotor1.forward();
	}

	public void stopWheelEntrance() {
		wheelEntranceMotor1.stop();
	}

	public void backwardWheelEntrance() {
		wheelEntranceMotor1.backward();
	}

	public void goalEjectBall(Pose pose) {

		if (GOAL_SIDE_RELATIVE_TO_CAMERA == "left") {
			Pose newPose = makePose(pose, LEFT_GOAL_POINT[0] + GOAL_PLUS_LENGTH, LEFT_GOAL_POINT[1]);
			goTo(LEFT_GOAL_POINT[0] + GOAL_PLUS_LENGTH, LEFT_GOAL_POINT[1], pose);
			goTo(LEFT_GOAL_POINT[0] + GOAL_MINUS_LENGTH, LEFT_GOAL_POINT[1], newPose);
		} else {
			Pose newPose = makePose(pose, RIGHT_GOAL_POINT[0] - GOAL_PLUS_LENGTH, RIGHT_GOAL_POINT[1]);
			goTo(RIGHT_GOAL_POINT[0] - GOAL_PLUS_LENGTH, RIGHT_GOAL_POINT[1], pose);
			goTo(RIGHT_GOAL_POINT[0] - GOAL_MINUS_LENGTH, RIGHT_GOAL_POINT[1], newPose);
		}
		backwardWheelEntrance();

	}
	
	public void reverseStep() {
		
		pilot.backward();
		
		wait(750);
		
		pilot.stop();
		
	}
	
	public void forwardStep() {
		
		pilot.forward();
		
		wait(500);
		
		pilot.stop();
		
	}

	public void goTo(float x, float y) {

		while (!navigator.pathCompleted()) {
			navigator.followPath();
		}
		
		navigator.addWaypoint(x, y);
		
		while (!navigator.pathCompleted()) {
			navigator.followPath();
		}

		System.out.println("Robot heading:" + navigator.getPoseProvider().getPose().getHeading());
		System.out.println("Robot x, y: " + navigator.getPoseProvider().getPose().getX() + ", " + navigator.getPoseProvider().getPose().getY());
		System.out.println("Went to x:" + x + "y:" + y);

	}
	
	public void goTo(float x, float y, Pose pose) {
	
		while (!navigator.pathCompleted()) {
			navigator.followPath();
		}
		
		poseProvider.setPose(pose);
		navigator.setPoseProvider(poseProvider);
		navigator.addWaypoint(x, y);
		
		while (!navigator.pathCompleted()) {
			navigator.followPath();
		}
		
		System.out.println("Robot heading:" + navigator.getPoseProvider().getPose().getHeading());
		System.out.println("Robot x, y: " + navigator.getPoseProvider().getPose().getX() + ", " + navigator.getPoseProvider().getPose().getY());
		System.out.println("Went to x:" + x + "y:" + y);


		
	}

	public void setPose(float x, float y, float heading) {
		System.out.println("Robot x, y: " + x + y);
		System.out.println("Robot heading: " + heading);
		//System.out.print("pose: " + pose);

		Pose pose = new Pose(x, y, heading);

		poseProvider.setPose(pose);
		navigator.setPoseProvider(poseProvider);

	}
	
	public void goToCorner(float x, float y, Pose pose) {
		
		goTo(x, y, pose);
        turnOnVentilator();
        wait(2000);
        turnOffVentilator();
        reverseStep();
		
	}
	
	public void goToEdge(float x, float y, Pose pose) {
		
		goTo(x, y, pose);
        turnOnVentilator();
        wait(2000);
        turnOffVentilator();
        reverseStep();
        
        
	}

	/**
	 * Instantiates a brick and all the motors and sensors of the robot.
	 */
	public static void initRobot() {
		if (brick != null) {
			// Already initialized
			return;
		}
		Button.LEDPattern(5); // Flashing red
		System.out.println("Initializing...");

		brick = BrickFinder.getDefault();

		leftMotor = new EV3LargeRegulatedMotor(brick.getPort(LEFT_WHEEL_PORT));
		rightMotor = new EV3LargeRegulatedMotor(brick.getPort(RIGHT_WHEEL_PORT));

		wheelEntranceMotor1 = new EV3MediumRegulatedMotor(brick.getPort(ENTRANCE_WHEEL_PORT1));
		wheelEntranceMotor1.setAcceleration(ENTRANCE_ACCELERATION);
		wheelEntranceMotor1.setSpeed(ENTRANCE_SPEED);
		wheelEntranceMotor2 = new EV3MediumRegulatedMotor(brick.getPort(ENTRANCE_WHEEL_PORT2));
		wheelEntranceMotor2.setAcceleration(ENTRANCE_ACCELERATION);
		wheelEntranceMotor2.setSpeed(ENTRANCE_SPEED);

		Button.LEDPattern(1); // Steady green
		Sound.beepSequenceUp();
		System.out.println("Ready!");
	}

	/**
	 * Instantiates a MovePilot and PoseProvider with default parameters.<br>
	 * Don't call this function if you plan to use leftMotor and rightMotor directly
	 * to control the robot.
	 */
	public static void initPilot() {
		initPilot(WHEEL_DIAMETER, WHEEL_SPACING);
	}

	/**
	 * Instantiates a MovePilot and PoseProvider.<br>
	 * Don't call this function if you plan to use leftMotor and rightMotor directly
	 * to control the robot.
	 *
	 * @param wheelDiameter Diameter of wheels in meters
	 * @param wheelSpacing  Distance between center of wheels in meters.
	 */
	public static void initPilot(double wheelDiameter, double wheelSpacing) {
		if (pilot != null) {
			// Already initialized
			return;
		}
		Wheel leftWheel = WheeledChassis.modelWheel(leftMotor, WHEEL_DIAMETER).offset(WHEEL_SPACING / 2);
		Wheel rightWheel = WheeledChassis.modelWheel(rightMotor, WHEEL_DIAMETER).offset(-WHEEL_SPACING / 2);

		Chassis chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);

		pilot = new MovePilot(chassis);
		poseProvider = new OdometryPoseProvider(pilot);

		// Set default speed to 0.3 m/s and acceleration to 0.9 m/s^2
		pilot.setLinearSpeed(PILOT_LINEAR_SPEED);
		pilot.setLinearAcceleration(PILOT_LINEAR_ACCELERATION);

		// Set default turn speed to 180 deg/s and acceleration to 540 deg/s^2
		pilot.setAngularSpeed(PILOT_ANGULAR_SPEED);
		pilot.setAngularAcceleration(PILOT_ANGULAR_ACCELERATION);
	}

	public static void initNavigator() {
		if (navigator != null) {
			// Already initialized
			return;
		}

		navigator = new Navigator(pilot, poseProvider);
//	 navigator = new Navigator(pilot);

	}

	public static void moveTest() {
		System.out.println("Position: x = " + poseProvider.getPose().getX() + "y = " + poseProvider.getPose().getY());
		pilot.travel(-0.3);
		System.out.println("Position: x = " + poseProvider.getPose().getX() + " y = " + poseProvider.getPose().getY());

	}

	public static void turnTest() {

		System.out.println("Heading: " + poseProvider.getPose().getHeading());

//   for(int i = 0; i < 2; i++) {
//  	 pilot.rotate(-90);
//  	 System.out.println("Heading: " + poseProvider.getPose().getHeading());
//  	 pilot.rotate(90);
//  	 System.out.println("Heading: " + poseProvider.getPose().getHeading());
//   }
		pilot.rotate(-360);
		pilot.rotate(-360);
		pilot.rotate(-360);

		System.out.println("Heading: " + poseProvider.getPose().getHeading());

	}

	public static void turnRight() {

		System.out.println("Heading: " + poseProvider.getPose().getHeading());

		pilot.rotate(-90);

		System.out.println("Heading: " + poseProvider.getPose().getHeading());

	}

	public static void turnLeft() {

		System.out.println("Heading: " + poseProvider.getPose().getHeading());

		pilot.rotate(90);

		System.out.println("Heading: " + poseProvider.getPose().getHeading());

	}

	public static void navigatorTest() {

//	 float x, y = (float) 0.30;

//	 System.out.println(navigator.getPoseProvider().getPose().getLocation());
//	 
//	 System.out.println("Navigator is moving: " + navigator.isMoving());
//	 navigator.goTo( x, y);
//	 System.out.println("Navigator is moving: " + navigator.isMoving());
//	 navigator.goTo((float) 0.0,(float) 0.0, 90);
//	 
//	 System.out.println(navigator.getPoseProvider().getPose().getLocation());
		System.out.println();

//	 navigator.addWaypoint((float) 0.5, (float) 0);
		navigator.addWaypoint((float) 0.5, (float) 0.5);
//	 navigator.addWaypoint((float) 0, (float) 0.5);
//	 navigator.addWaypoint((float) 0, (float) 0);
//	 navigator.addWaypoint((float) 0.25, (float) 0.25);

		System.out.println("Path: " + navigator.getPath());

//	 navigator.singleStep(false);

		while (!navigator.pathCompleted()) {
			navigator.followPath();
//		 System.out.println("Going to: " + navigator.getWaypoint());
		}

		System.out.println("Path: " + navigator.getPath());

	}

	public void turnOnVentilator() {
		wheelEntranceMotor2.rotate(720);
		//wheelEntranceMotor2.rotate(360);
	}
	
	public void turnOffVentilator() {
		wheelEntranceMotor2.rotate(360);
		//wheelEntranceMotor2.rotate(720);
	}
	
	public void collectCornerBall() {
		turnOnVentilator();
		turnOffVentilator();
	}
	
	public void wait(int ms) {
		try {
			TimeUnit.MILLISECONDS.sleep(ms);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static Pose makePose(Pose pose, float x, float y) {
		Point p = new Point(x, y);
		float heading = pose.angleTo(p);
		Pose newPose = new Pose(x, y, heading);
		return newPose;
	}

}

//For testing
//public void forward () {
//	 
//	 leftMotor.forward();
//	 rightMotor.forward();
//	 
//}
//
//public void backward () {
//	 
//	 leftMotor.backward();
//	 rightMotor.backward();
//	 
//}
//
//public void right () {
//	 
//	 leftMotor.forward();
//	 rightMotor.backward();
//	 
//}
//
//public void left () {
//	 
//	 leftMotor.backward();
//	 rightMotor.forward();
//	 
//}
//
//public void stop () {
//	 
//	 leftMotor.stop();
//	 rightMotor.stop();
//	 
//}
