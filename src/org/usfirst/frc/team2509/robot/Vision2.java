package org.usfirst.frc.team2509.robot;

import java.util.ArrayList;
import java.util.Iterator;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
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

public class Vision2{
	
	/**
	 * BEGIN VARIABLES
	 */


	protected final double
		CAMERA_ANGLE = 15,
		CAMERA_OFFSET_FRONT = 0,
		CAMERA_OFFSET_CENTER = 0,
		HORIZANTAL_FOV = 53.75,
		LOWER_THRESH = 1,
		VERTICAL_FOV = 30.25,
		UPPER_THRESH = 5;
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
	private final Mat 
		CLUSTERS = new Mat(),
		EDGES = new Mat(),
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
		LOWER_BOUNDS = new Scalar(37,0,188),
		UPPER_BOUNDS = new Scalar(104,46,255);
	protected final Size 
		RESIZE = new Size(320,240);
	
	
	/**
	 * BEGIN METHODS
	 */
	public void Canny(){
		CVSINK.grabFrame(SOURCE);
		Imgproc.cvtColor(SOURCE, HSV, Imgproc.COLOR_BGR2HSV);
		Imgproc.blur(HSV, EDGES, new Size(3,3));
		Imgproc.Canny(EDGES, EDGES, LOWER_THRESH,UPPER_THRESH,3,false);
		
	}
	
	public void GripBased(){
		CVSINK.grabFrame(SOURCE);
		Imgproc.cvtColor(SOURCE, HSV, Imgproc.COLOR_BGR2HSV);
		Core.inRange(HSV, LOWER_BOUNDS, UPPER_BOUNDS, THRESH);
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		contours.clear();
		Imgproc.findContours(THRESH, contours, HEIRARCHY, 
				Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		MatOfInt hull = new MatOfInt();
		for(int i = 0; i<contours.size(); i++){
			final MatOfPoint CONTOURS = contours.get(i);
			final Rect rec = Imgproc.boundingRect(CONTOURS);
			Imgproc.convexHull(CONTOURS, hull);
			MatOfPoint mopHull = new MatOfPoint();
			mopHull.create((int) hull.size().height,1, CvType.CV_32SC2);
			for (int j = 0; j < hull.size().height; j++){
				int index = (int)hull.get(j, 0)[0];
				double[] point = new double[] {CONTOURS.get(index,0)[0], CONTOURS.get(index,0)[1]};
				mopHull.put(j, 0, point);
			}
			contours.add(CONTOURS);
		}
		
	}
	
	public void ID_Target(){
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		MatOfInt hull = new MatOfInt();
		while(FRAME_RATE <5){
			contours.clear();
			CVSINK.grabFrame(SOURCE);
			Imgproc.cvtColor(SOURCE, HSV, Imgproc.COLOR_BGR2HSV);
			Core.inRange(HSV, LOWER_BOUNDS, UPPER_BOUNDS, THRESH);
			Imgproc.findContours(THRESH, contours,HEIRARCHY, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
			for(int i = 0; i<contours.size(); i++){
				MatOfPoint CONTOURS = contours.get(i);
				Rect rec = Imgproc.boundingRect(CONTOURS);
				Imgproc.convexHull(CONTOURS, hull);
				MatOfPoint mopHull = new MatOfPoint();
				mopHull.create((int) hull.size().height,1, CvType.CV_32SC2);
				for (int j = 0; j < hull.size().height; j++){
					int index = (int)hull.get(j, 0)[0];
					double[] point = new double[] {CONTOURS.get(index,0)[0], CONTOURS.get(index,0)[1]};
					mopHull.put(j, 0, point);
				}
				contours.add(CONTOURS);
			}
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
				Point center = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0);
				Point centerw = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0 - 20);
				Imgproc.putText(SOURCE, ""+(double)center.x+" , "+(double)center.y, center, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
				Imgproc.putText(SOURCE, ""+(double)centerw.x+" , "+(double)centerw.y, centerw, Core.FONT_HERSHEY_PLAIN, 3, BLACK);
			}
			OUTPUT_STREAM.putFrame(SOURCE);
		}
	}
}