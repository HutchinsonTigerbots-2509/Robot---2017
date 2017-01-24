package org.usfirst.frc.team2509.robot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * 
 * ORGANIZE VARIABLES IN THIS ORDER(ALPHABETICALLY)
 * 1. JAVA
 * 2. FIRST/WPI
 * 3. OPENCV
 * @author Nate
 *
 */

public class Vision{
	
	/**
	 * BEGIN VARIABLES
	 */
	
	private final boolean 
		isINIT = true;
	protected final double
		CAMERA_ANGLE = 15,
		CAMERA_OFFSET_FRONT = 0,
		CAMERA_OFFSET_CENTER = 0,
		VERTICAL_FOV = 30.25,
		HORIZANTAL_FOV = 53.75;
	protected final int
		CAMERA_HEIGHT = 0,
		BOILER_HEIGHT = 88,
		BOILER_WIDTH = 15,
		FRAME_RATE = 0,
		GEAR_PEG_HEIGHT =16;
	public UsbCamera 
	//IP FOR STREAM http://roborio-2509-frc.local:1181/?action=stream
		FRONT_CAM = CameraServer.getInstance().startAutomaticCapture();
	private CvSink
		CVSINK;// = CameraServer.getInstance().getVideo();
	private final CvSource 
		OUTPUT_STREAM = CameraServer.getInstance().putVideo("ALT-Cam", 640, 480);
	private final Mat 
		CLUSTERS = new Mat(),		
		HEIRARCHY = new Mat(),
		HSL = new Mat(),
		OUTPUT = new Mat(),
		SOURCE = new Mat(),
		THRESH = new Mat();
	protected final Scalar 
	//COLOR VALUES
		BLACK = new Scalar(0,0,0),
		BLUE = new Scalar(255, 0, 0),
		GREEN = new Scalar(0, 255, 0),
		RED = new Scalar(0, 0, 255),
		YELLOW = new Scalar(0, 255, 255),
	//Thresholds values
		LOWER_BOUNDS = new Scalar(70,170,44),
		UPPER_BOUNDS = new Scalar(100,255,255);
	protected final Size 
		RESIZE = new Size(320,240);
	
	
	/**
	 * BEGIN METHODS
	 */
	
	public void cvt2Gray(){
        	CVSINK.grabFrame(SOURCE);
            Imgproc.cvtColor(SOURCE, OUTPUT, Imgproc.COLOR_BGR2GRAY);
            OUTPUT_STREAM.putFrame(OUTPUT);
	}
	public void ID_Target(){
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		double X,Y,TARGET_X,TARGET_Y,DISTANCE,AZIMUTH;
		long BEFORE = System.currentTimeMillis();
		while(FRAME_RATE <5){
			contours.clear();
			CVSINK.grabFrame(SOURCE);
			Imgproc.cvtColor(SOURCE, HSL, Imgproc.COLOR_BGR2HLS);
			Core.inRange(HSL, LOWER_BOUNDS, UPPER_BOUNDS, THRESH);
			Imgproc.findContours(THRESH, contours, HEIRARCHY, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
			for(MatOfPoint mop :contours){
				Rect rec = Imgproc.boundingRect(mop);
				Imgproc.rectangle(SOURCE, rec.br(), rec.tl(), RED);
			}
			for(Iterator<MatOfPoint> iterator = contours.iterator();iterator.hasNext();){
				MatOfPoint matOfPoint = (MatOfPoint) iterator.next();
				Rect rec = Imgproc.boundingRect(matOfPoint);
				if(rec.height < 15 || rec.width < 15){
					iterator.remove();
				continue;
				}
				float aspect = (float)rec.width/(float)rec.height;
				if(aspect < 1.0)
					iterator.remove();
			}
			if(contours.size()==1){
				Rect rec = Imgproc.boundingRect(contours.get(0));
				Y = rec.br().y + rec.height / 2.0;
				Y= -((2 * (Y / SOURCE.height())) - 1);
				DISTANCE = (GEAR_PEG_HEIGHT - CAMERA_HEIGHT) / 
						Math.tan((Y * VERTICAL_FOV / 2.0 + CAMERA_ANGLE) * Math.PI / 180);
				TARGET_X = rec.tl().x + rec.width / 2.0;
				TARGET_X = (2 * (TARGET_X / SOURCE.width())) - 1;
				AZIMUTH = normalize360(TARGET_X*HORIZANTAL_FOV /2.0 + 0);
				Point center = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0);
				Point centerw = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0 - 20);
				Imgproc.putText(SOURCE, ""+(int)DISTANCE, center, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
				Imgproc.putText(SOURCE, ""+(int)AZIMUTH, centerw, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
			}
			OUTPUT_STREAM.putFrame(SOURCE);
		}
	}
	public static double normalize360(double angle){
		// Mod the angle by 360 to give a value between (0, 360]
		// Make it positive (by adding 360) if required
		return (angle < 0) ? angle % 360 + 360 : angle % 360;
	}
	public void Procces(){
			CVSINK = CameraServer.getInstance().getVideo();
			
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();;
		CVSINK.grabFrame(SOURCE);
        Imgproc.cvtColor(SOURCE, OUTPUT, Imgproc.COLOR_BGR2HSV);
        Imgproc.cvtColor(SOURCE, HSL, Imgproc.COLOR_BGR2HLS);
		Core.inRange(HSL, LOWER_BOUNDS, UPPER_BOUNDS, THRESH);
		
		Imgproc.findContours(THRESH, contours, HEIRARCHY, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		for(MatOfPoint mop :contours){
			Rect rec = Imgproc.boundingRect(mop);
			Imgproc.rectangle(SOURCE, rec.br(), rec.tl(), RED);
		}
        OUTPUT_STREAM.putFrame(OUTPUT);
        
	}

}