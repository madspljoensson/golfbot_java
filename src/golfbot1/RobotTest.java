package golfbot1;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class RobotTest {

	public static void main(String[] args) {
		
		EV3Skeleton robot = new EV3Skeleton();
		
		robot.forwardWheelEntrance();
		robot.wait(2000);
		
		while(true) {
	        //robot.forwardWheelEntrance();
			
	        robot.backwardWheelEntrance();
		}
        
        //robot.collectCornerBall();

        //robot.forwardWheelEntrance();

		//robot.wait(5000);
		
		//robot.shoot();
		
		
	}
	
	
}
