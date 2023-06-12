package golfbot1;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class RobotTest {

	public static void main(String[] args) {
		
		EV3Skeleton robot = new EV3Skeleton();
		
        robot.forwardWheelEntrance();
        
        robot.collectCornerBall();

        robot.forwardWheelEntrance();

		robot.wait(5000);
		
		//robot.shoot();
		
		
	}
	
	
}
