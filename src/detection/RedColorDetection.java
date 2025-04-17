package detection;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class RedColorDetection {

	public static void main(String[] args) {
		// Load OpenCV native library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// Create a VideoCapture object to open the webcam (0 is usually the default
		// webcam)
		VideoCapture camera = new VideoCapture(0);

		if (!camera.isOpened()) {
			System.out.println("Error: Camera not found or could not be opened!");
			return;
		}

		Mat frame = new Mat(); // To store the current frame
		Mat hsvImage = new Mat(); // To store the HSV image
		Mat redMask = new Mat(); // To store the red color mask

		while (true) {
			// Capture a frame from the webcam
			camera.read(frame);

			if (frame.empty()) {
				System.out.println("Error: Could not capture frame!");
				break;
			}

			// Convert the captured frame from BGR to HSV
			Imgproc.cvtColor(frame, hsvImage, Imgproc.COLOR_BGR2HSV);

			// Define lower and upper bounds for red color in HSV space
			Scalar lowerRed = new Scalar(0, 120, 70); // Lower bound for red in HSV
			Scalar upperRed = new Scalar(10, 255, 255); // Upper bound for red in HSV

			// Create a mask for red colors
			Core.inRange(hsvImage, lowerRed, upperRed, redMask);

			// Also, include a second range for red (the hue wraps around 180 degrees in
			// OpenCV)
			Scalar lowerRed2 = new Scalar(170, 120, 70);
			Scalar upperRed2 = new Scalar(180, 255, 255);
			Mat redMask2 = new Mat();
			Core.inRange(hsvImage, lowerRed2, upperRed2, redMask2);

			// Combine the two red masks
			Core.add(redMask, redMask2, redMask);

			// Show the original frame and the mask
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
