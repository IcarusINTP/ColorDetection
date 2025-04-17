package detection;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class RedColorDetection {

	public static void main(String[] args) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		VideoCapture camera = new VideoCapture(0);

		if (!camera.isOpened()) {
			System.out.println("Error: Camera not found o");
			return;
		}

		Mat frame = new Mat(); // here we will store the live feed from webcam
		Mat hsvImage = new Mat(); // To store the HSV image, the live feed is in RGB but for detection we need hue saturation and other stuff, we will see more of this down below
		Mat redMask = new Mat(); // To store the red color mask, if you are not familiar with photoshop or picture/video editing 'mask' is as name implied just another layer over or seperated from original

		while (true) {
			
			camera.read(frame);// okay so this part says read, yes, but it is actually filling the camera feed to frame

			if (frame.empty()) {
				System.out.println("Error: Could not capture frame!");
				break;
			}

			// Converting to hsv(hue saturation and (I dont remember what v stand for lol), because it can detect red better.
			Imgproc.cvtColor(frame, hsvImage, Imgproc.COLOR_BGR2HSV);

			// Define lower and upper bounds for red color in HSV space
			Scalar lowerRed = new Scalar(0, 120, 70); 
			Scalar upperRed = new Scalar(10, 255, 255);

			// Create a mask for red colors
			Core.inRange(hsvImage, lowerRed, upperRed, redMask);

			// Also, include a second range for red, this is specially for red only because like red has two ranges, though i am not sure if any other color has more than one rang 
		
			Scalar lowerRed2 = new Scalar(170, 120, 70);
			Scalar upperRed2 = new Scalar(180, 255, 255);
			Mat redMask2 = new Mat();
			Core.inRange(hsvImage, lowerRed2, upperRed2, redMask2);

			// Combine the two red masks
			Core.add(redMask, redMask2, redMask);

			// Show the original frame and the mask, this display the UI windows showing webcam feed 
			HighGui.imshow("Original", frame);
			HighGui.imshow("Red Mask", redMask);

			// Break the loop if the user presses 'ESC'
			if (HighGui.waitKey(1) == 27) {
				break; // ESC to exit
			}
		}

		// Release the camera and close all OpenCV windows
		camera.release();
		HighGui.destroyAllWindows();
	}
}
