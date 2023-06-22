package golfbot1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NextMove {
    private float[] nextBall;
    private String moveType;
    private float[] robotCoords;
    private float robotAngle;
    private float[] extraPoint;

    public NextMove() {
        this.nextBall = new float[0];
        this.moveType = "";
        this.robotCoords = new float[0];
        this.robotAngle = 0.0f;
        this.extraPoint = new float[0];
    }
    
//    public void setNextMove(float[] nextBall, float[] robotCoords, float[] robotAngle) {
//        this.nextBall = nextBall;
//        this.robotCoords = robotCoords;
//        this.robotAngle = robotAngle;
//    }

    public float[] getNextBall() {
        return this.nextBall;
    }

    public void setNextBall(float[] nextBall) {
        this.nextBall = nextBall;
    }
    
    public String getMoveType() {
        return this.moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public float[] getRobotCoords() {
        return this.robotCoords;
    }

    public void setRobotCoords(float[] robotCoords) {
        this.robotCoords = robotCoords;
    }

    public float getRobotAngle() {
        return this.robotAngle;
    }

    public void setRobotAngle(float robotAngle) {
        this.robotAngle = robotAngle;
    }
    
    public float[] getExtraPoint() {
        return this.extraPoint;
    }

    public void setExtraPoint(float[] extraPoint) {
        this.extraPoint = extraPoint;
    }
    

    public static NextMove fromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        NextMove nextMove = new NextMove();
        
        System.out.println("next ball: " + jsonObject.getJSONArray("next_ball"));
        nextMove.setNextBall(fillData1(jsonObject.getJSONArray("next_ball")));
        
        System.out.println("move type: " + jsonObject.getString("move_type"));

        nextMove.setMoveType(jsonObject.getString("move_type"));
        
        System.out.println("robot coord: " + jsonObject.getJSONArray("robot_coords"));
        nextMove.setRobotCoords(fillData1(jsonObject.getJSONArray("robot_coords")));

        System.out.println("robot heading: " + jsonObject.getFloat("robot_heading"));
        nextMove.setRobotAngle(jsonObject.getFloat("robot_heading"));
        
        System.out.println("extra point: " + jsonObject.getJSONArray("extra_point"));
        nextMove.setExtraPoint(fillData1(jsonObject.getJSONArray("extra_point")));
 
        return nextMove;
    }
    
    
//    private static float[] convertToFloatArray(double[] data) {
//    	
//    	float[] floatArray = new float[data.length];
//        for(int i = 0 ; i < data.length; i++) {
//        	floatArray[i] = (float) data[i];
//        }
//        
//        return floatArray;
//    	
//    }
   
    
    private static float[] fillData1(JSONArray jsonArray){
        float[] fData = new float[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                fData[i] = (float) jsonArray.getDouble(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return fData;
    }
    
}












