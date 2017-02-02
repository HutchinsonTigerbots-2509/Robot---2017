package org.usfirst.frc.team2509.robot;

import java.util.ArrayList;
import java.util.Iterator;

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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * @author Nate
 * 
 * ORGANIZE VARIABLES IN THIS ORDER(ALPHABETICALLY)
 * 1. JAVA
 * 2. FIRST/WPI
 * 3. OPENCV
 *
 */

public class Vision{
	
	/**
	 * BEGIN VARIABLES
	 */
	private final ArrayList<MatOfPoint> 
		contours = new ArrayList<MatOfPoint>();
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
	public final UsbCamera 
	//IP FOR STREAM http://roborio-2509-frc.local:1181/?action=stream
		FRONT_CAM = CameraServer.getInstance().startAutomaticCapture();

	private final CvSink
		CVSINK = CameraServer.getInstance().getVideo();
	private final CvSource 
		OUTPUT_STREAM = CameraServer.getInstance().putVideo("ALT-Cam", 640, 480);
	public final Mat 
		BINARY = new Mat(),
		CLUSTERS = new Mat(),		
		HEIRARCHY = new Mat(),
		HSV = new Mat(),
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
		LOWER_BOUNDS = new Scalar(180,190,40),
		UPPER_BOUNDS = new Scalar(200,210,60);
	protected final Size 
		RESIZE = new Size(320,240);
	
	
	/**
	 * BEGIN METHODS
	 */
	
	public void cvt2Gray(){        
		contours.clear();
		CVSINK.grabFrame(SOURCE);
		Imgproc.cvtColor(SOURCE, HSV, Imgproc.COLOR_BGR2RGB);
		Imgproc.threshold(HSV, BINARY, 180, 200, Imgproc.THRESH_BINARY);	
		Imgproc.cvtColor(BINARY, THRESH, Imgproc.COLOR_BGR2GRAY);

		//Core.inRange(THRESH	, LOWER_BOUNDS, UPPER_BOUNDS, CLUSTERS);	
        Imgproc.findContours(THRESH, contours, HEIRARCHY, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        for(MatOfPoint mop :contours){
			Rect rec = Imgproc.boundingRect(mop);
			Imgproc.rectangle(SOURCE, rec.br(), rec.tl(), RED);
		}
		for(Iterator<MatOfPoint> iterator = contours.iterator();iterator.hasNext();){
			MatOfPoint matOfPoint = (MatOfPoint) iterator.next();
			Rect rec = Imgproc.boundingRect(matOfPoint);
			if( rec.height < 15 || rec.width < 15){
				iterator.remove();
			continue;
			}
			float aspect = (float)rec.width/(float)rec.height;
			if(aspect <0.35||aspect>0.45){
				iterator.remove();
				
			}
			SmartDashboard.putInt("Contours", contours.size());
			SmartDashboard.putInt("Width", rec.width);
		}
		if(contours.size()==2){
			Rect rec = Imgproc.boundingRect(contours.get(0));
			Point center = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0);
			Point centerw = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0 - 20);
			SmartDashboard.putString("Bottom Right:", ""+(Point)rec.br());
			SmartDashboard.putString("Top Left", ""+(Point)rec.tl());
			Imgproc.putText(SOURCE, ""+(Point)rec.br(), center, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
			Imgproc.putText(SOURCE, ""+(Point)rec.tl(), centerw, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
		}
		OUTPUT_STREAM.putFrame(SOURCE);
	}
	public void ID_Target(){
		while(FRAME_RATE <5){
			contours.clear();
			FRONT_CAM.setBrightness(30);
			CVSINK.grabFrame(SOURCE);
			Imgproc.cvtColor(SOURCE, HSV, Imgproc.COLOR_BGR2HSV);
			Core.inRange(HSV, LOWER_BOUNDS, UPPER_BOUNDS, THRESH);
			Imgproc.findContours(THRESH, contours,HEIRARCHY, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
			
			for(MatOfPoint mop :contours){
				Rect rec = Imgproc.boundingRect(mop);
				Imgproc.rectangle(SOURCE, rec.br(), rec.tl(), RED);
			}
			for(Iterator<MatOfPoint> iterator = contours.iterator();iterator.hasNext();){
				MatOfPoint matOfPoint = (MatOfPoint) iterator.next();
				Rect rec = Imgproc.boundingRect(matOfPoint);
				if( rec.height < 15 || rec.width < 15){
					iterator.remove();
				continue;
				}
				float aspect = (float)rec.width/(float)rec.height;
				if(aspect < 0.3)
					iterator.remove();
				if(aspect > 0.5){
					iterator.remove();
				}
			}
			if(contours.size()==2){
				Rect rec = Imgproc.boundingRect(contours.get(0));
				Point center = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0);
				Point centerw = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0 - 20);
				Imgproc.putText(SOURCE, ""+(Point)rec.br(), center, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
				Imgproc.putText(SOURCE, ""+(Point)rec.tl(), centerw, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
				
			}
			OUTPUT_STREAM.putFrame(SOURCE);
		}
	}

	public void Procces(){
		CVSINK.grabFrame(SOURCE);
		Imgproc.cvtColor(SOURCE, HSV, Imgproc.COLOR_BGR2RGB);
		Imgproc.threshold(HSV, BINARY, 180, 200, Imgproc.THRESH_BINARY);
		Imgproc.cvtColor(BINARY, THRESH, Imgproc.COLOR_BGR2HSV);
        Imgproc.findContours(THRESH, contours, HEIRARCHY, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        for(MatOfPoint mop :contours){
			Rect rec = Imgproc.boundingRect(mop);
			Imgproc.rectangle(SOURCE, rec.br(), rec.tl(), RED);
		}
		for(Iterator<MatOfPoint> iterator = contours.iterator();iterator.hasNext();){
			MatOfPoint matOfPoint = (MatOfPoint) iterator.next();
			Rect rec = Imgproc.boundingRect(matOfPoint);
			if( rec.height < 15 || rec.width < 15){
				iterator.remove();
			continue;
			}
			float aspect = (float)rec.width/(float)rec.height;
			if(aspect < 1.0)
				iterator.remove();
		}
		if(contours.size()==2){
			Rect rec = Imgproc.boundingRect(contours.get(0));
			Point center = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0);
			Point centerw = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0 - 20);
			Imgproc.putText(SOURCE, ""+(Point)rec.br(), center, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
			Imgproc.putText(SOURCE, ""+(Point)rec.tl(), centerw, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
		}
		OUTPUT_STREAM.putFrame(SOURCE);
	}
}