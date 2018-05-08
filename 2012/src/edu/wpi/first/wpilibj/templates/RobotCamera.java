package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.camera.*;
import edu.wpi.first.wpilibj.image.*;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import java.io.IOException;

/**
 * Camera control and image processing.
 * @todo Add threading
 * @todo What if the camera isn't connected?
 * @todo Analyse images and produce a better algorithm for detecting the location of baskets.
 * @todo Re-calculate the formula for distance based on height of the actual baskets.
 * @author Wolfgang Faust
 */
public class RobotCamera {
	/**
	 * A set of thresholds for the target's colour. This may need to be
	 * changed if the light level, etc is changed.
	 * Format: RedLo,RedHi,BlueLo,BlueHi,GreenLo,GreenHi
	 */
	private static final int[] M_RGB_THRESHOLD = {0, 100, 0, 100, 145, 255};
	/**
	 * Camera settings:
	 * - Resolution:       320x240
	 * - Compression:      75
	 * - Colour level:     100
	 * - Brightness:       0
	 * - Sharpness:        100
	 * - Contrast:         100
	 * - White balance:    Fixed Outdoor 1
	 * - Exposure value:   50
	 * - Exposure control: Hold current
	 * - Enable backlight compensation: ON
	 */
	private static final AxisCamera M_CAMERA = AxisCamera.getInstance(ElectricalIDs.Network.AXIS_CAMERA);
	/**
	 * The field of view (in degrees) on the Axis camera.
	 */
	private static final int M_AXIS_FOV = 47;
	/**
	 * The heights of each of the baskets (in inches).
	 * Taken from FRC 2012 Game rules, section 2.2.2.
	 * The numbers in the section are to the edge of the rim; each of
	 * these numbers has 2in added to be to the backboard, not rim.
	 */
	private static final int[] BASKET_HEIGHTS = {30,63,63,100};
	/**
	 * Whether to enable debugging.
	 */
	private boolean DEBUG = false;

	/**
	 * A collection of criteria a particle must meet to be a basket.
	 */
	private static final CriteriaCollection m_criteriaCollection  = new CriteriaCollection(); // @todo this should be static final, except then I can't add criteria
	static { // Add criteria to the collection
		m_criteriaCollection.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false); // 30-400
		m_criteriaCollection.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false); // 40-400
		m_criteriaCollection.addCriteria(MeasurementType.IMAQ_MT_RATIO_OF_EQUIVALENT_RECT_SIDES,(float)1.2,(float)1.6,false);
		m_criteriaCollection.addCriteria(MeasurementType.IMAQ_MT_AREA_BY_IMAGE_AREA , 90, 100, false);
	}
	private RobotLog log;
	/**
	 * The relay controlling the LED strips around the camera.
	 */
	private Relay k_cameraLights;
	/**
	 * A count of how many images have been processed, used for creating unique IDs.
	 */
	private long procCnt = 0;
	/**
	 * initialise the camera, etc.
	 * @todo Fiddle with CriteriaCollection.
	 * @param debug Whether to enable debugging.
	 * @param cameraLights the Relay for the lights around the Camera (direction must be set)
	 */
	public RobotCamera (Relay cameraLights,boolean debug) {
		DEBUG = debug;
		try {
			if (DEBUG) log = new RobotLog("/tmp/vision.debug.txt");
		} catch (IOException ex) {
			ex.printStackTrace();
			DEBUG=false;
			System.out.println("RobotCamera: Debug logging disabled (Exception occured while accessing log file; see stack trace).");
		}
		k_cameraLights = cameraLights;
		/*Thread tracker = new Thread(){
			public void run(){
				while (true) {
					
				}
			}  
		};*/
	}
	/**
	 * Initialize the camera, etc.
	 * Debugging is disabled.
	 */
	public RobotCamera (Relay cameraLights) {
		this(cameraLights,false);
    }
	/**
	 * Get the angle offset to the target. Left is positive, right is negative.
	 * @param particle The particle representing the target
	 * @return The angle of offset.
	 */
	private float calcAngle(ParticleAnalysisReport particle) {
		int offset = particle.center_mass_x; // The center of mass of the particle
		int middle = particle.imageWidth/2;  // The middle of the image
		                                     // These two should be as close together as possible
		int diff = offset-middle;
		float degPerPix = (float)M_AXIS_FOV/(float)particle.imageWidth;
		return degPerPix*diff;
	}
	/**
	 * Calculate the distance to a target with a certain width as viewed
	 * by the camera. This uses a formula generated by actual values.
	 * @todo Use height, not width; also recalculate anyhow to use values based
	 * on hoops & camera in correct positions.
	 * @param particle The target to calculate the distance to
	 * @return The distance (in inches) to the actual target.
	 */
	private int calcDistance(ParticleAnalysisReport particle) {
		int width = particle.boundingRectWidth;
		// This formula was generated in GeoGebra based on
		// actual data collected.
		// See the file distgraph.ggb for the data & calculation
		// It has also been parenthesized *very* carefully!
		// Can't use Math.pow because it's not in this version of Java.lang
		return (int)((-(0.00001*width*width*width) + (0.00365*width*width) - (0.53828*width) + 34.46692)*12);
	}
	/**
	 * Returns true when a fresh image is available.
	 * @return Whether a fresh image is available.
	 */
    public boolean freshImage() {
        return M_CAMERA.freshImage();
    }
	/**
	 * Get the latest image from the camera.
	 * @throws AxisCameraException if no image is available.
	 * @throws NIVisionException if no image is available.
	 * @return the latest image, or @throws AxisCameraException or @throws AxisCameraException if no image is available.
	 */
    public ColorImage getImage() throws NIVisionException, AxisCameraException{
            return M_CAMERA.getImage();
    }
	/**
	 * Get an image from the camera. if possible, wait for a fresh image.
	 * if this does not happen within .5seconds, returns the latest image
	 * available.
	 * @throws AxisCameraException if no image is available.
	 * @throws NIVisionException if no image is available.
	 * @return the latest ColorImage, or @throws AxisCameraException or @throws AxisCameraException if no image is available.
	 */
	public ColorImage getFreshImage()throws AxisCameraException, NIVisionException{
		return getFreshImage(500);
	}
	/**
	 * Get an image from the camera. if possible, wait for a fresh image.
	 * if this does not happen before the timeout, returns the latest image
	 * available.
	 * @param timeout How long to wait (in ms) before giving up and returning an older image.
	 * @throws AxisCameraException if no image is available.
	 * @throws NIVisionException if no image is available.
	 * @return the latest ColorImage, or @throws AxisCameraException or @throws AxisCameraException if no image is available.
	 */
	public ColorImage getFreshImage(int timeout) throws AxisCameraException, NIVisionException{
		long u=System.currentTimeMillis();
		while(!freshImage()){
			if (System.currentTimeMillis()-u>timeout) { break; }
		}
		return getImage();
	}
	/**
	 * Locate all of the backboard-shaped particles in the image.
	 * @throws AxisCameraException if no image is available.
	 * @throws NIVisionException if no image is available.
	 * @return An array of ParticleAnalysisReports in the image
	 */
	private ParticleAnalysisReport[] getParticles() throws AxisCameraException, NIVisionException{
		k_cameraLights.set(Relay.Value.kOn);  // Turn lights on
		ColorImage image = getFreshImage(); // Take picture
		k_cameraLights.set(Relay.Value.kOff); // Turn lights off
		try {
			// Filter the backboards out of the image.
			// Note: convex hulling is best done *before* eroding, so 
			// we don't take out thin backboards.
			BinaryImage thresholdImage = image.thresholdRGB(M_RGB_THRESHOLD[0],M_RGB_THRESHOLD[1],M_RGB_THRESHOLD[2],M_RGB_THRESHOLD[3],M_RGB_THRESHOLD[4],M_RGB_THRESHOLD[5]);   // keep only red objects
			BinaryImage convexHullImage = thresholdImage.convexHull(false);          // fill in occluded rectangles
			BinaryImage bigObjectsImage = convexHullImage.removeSmallObjects(false, 10);  // remove small artifacts
			BinaryImage filteredImage = bigObjectsImage.particleFilter(m_criteriaCollection);           // find filled in rectangles
			
			// Get all the backboard particles.
			ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports();  // get list of results
			
			// Free everything; otherwise we'll run out of memory.
			filteredImage.free();
			convexHullImage.free();
			bigObjectsImage.free();
			thresholdImage.free();
			image.free();
			
			if (DEBUG) {
				int i;
				log.println(reports.length+" unordered particle reports during processing loop "+procCnt+":");
				for (i=0;i<reports.length;i++) {
					log.println(reports[i].toString());
				}
				log.println();
			}
			
			return reports;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Get the particles, ordered into a best-guess array where the index is the
	 * hoop number.
	 * @return The ordered particles.
	 * @throws Exception if it can't see the baskets properly.
	 * @todo completely rewrite this for more accuracy.
	 */
	private ParticleAnalysisReport[] getOrderedParticles() throws Exception {
		ParticleAnalysisReport[] unorderedParticles = getParticles();
		ParticleAnalysisReport[]   orderedParticles = new ParticleAnalysisReport[4];
		
		if (unorderedParticles.length == 0) // No particles.
			return orderedParticles;
		
		//Get the various particles
		ParticleAnalysisReport topmost    = topmostParticle(unorderedParticles);
		ParticleAnalysisReport leftmost   = leftmostParticle(unorderedParticles);
		ParticleAnalysisReport rightmost  = rightmostParticle(unorderedParticles);
		ParticleAnalysisReport bottommost = bottommostParticle(unorderedParticles);
		
		if (unorderedParticles.length > 4) { // Too many particles!
			throw new Exception("Too many particles! ("+unorderedParticles.length+" seen)");
		} else if (unorderedParticles.length == 4) {orderedParticles[Basket.TOP]    = topmost;
			orderedParticles[Basket.LEFT]   = leftmost;
			orderedParticles[Basket.RIGHT]  = rightmost;
			orderedParticles[Basket.BOTTOM] = bottommost;
		} else if (unorderedParticles.length == 3) {
			// @todo figure out which baskets we're seeing
			if (topmost.equals(leftmost) || topmost.equals(rightmost)) {
				// Topmost is also right- or leftmost; therefore, we're seeing the bottom three & not the top
				orderedParticles[Basket.TOP]    = null; // No top basket
				orderedParticles[Basket.LEFT]   = leftmost;
				orderedParticles[Basket.RIGHT]  = rightmost;
				orderedParticles[Basket.BOTTOM] = bottommost;
			} else {
				// We can see the top basket
				orderedParticles[Basket.TOP] = topmost;
				// Now figure out which other two we're seeing
				if (bottommost.equals(leftmost) || bottommost.equals(rightmost)) {
					// Bottommost is also right- or leftmost; therefore, we're seeing the top three & not the bottom
					orderedParticles[Basket.BOTTOM] = null;
					orderedParticles[Basket.LEFT]   = leftmost;
					orderedParticles[Basket.RIGHT]  = rightmost;
				} else {
					// We have the bottom basket
					orderedParticles[Basket.BOTTOM] = bottommost;
					// We're missing the left or right basket; figure out which one.
					if (leftmost.equals(topmost) || leftmost.equals(bottommost)) {
						// We're missing the left, but have the right
						orderedParticles[Basket.LEFT]  = null;
						orderedParticles[Basket.RIGHT] = rightmost;
					} else {
						// We have the left, but not the right
						orderedParticles[Basket.LEFT]  = leftmost;
						orderedParticles[Basket.RIGHT] = null;
					}
				}
			}
		} else if (unorderedParticles.length == 2) {
			// This code doen't even try to figure out which is where; it just
			// reports the higher one as the top and the lower one as the bottom.
			// This produces the intended behaviour.
			// The if here just handles the case where two are seen at the exact same
			// height.
			orderedParticles[Basket.LEFT]  = null;
			orderedParticles[Basket.RIGHT] = null;
			if (topmost.equals(bottommost)) {
				orderedParticles[Basket.TOP]    = leftmost;
				orderedParticles[Basket.BOTTOM] = rightmost;
			} else {
				orderedParticles[Basket.TOP]    = topmost;
				orderedParticles[Basket.BOTTOM] = bottommost;
			}
		} else if (unorderedParticles.length == 1) {
			// Return this one as target 0
			orderedParticles[0] = unorderedParticles[0];
		} else if (unorderedParticles.length == 0) {
			throw new Exception("No baskets seen!");
		} else {
			throw new Exception ("ERROR: CAN'T HAPPEN! ("+unorderedParticles.length+" particles seen)");
		}
		return orderedParticles;
	}
	/**
	 * Find the leftmost particle in an array of ParticleAnalysisReports
	 * @param particles The array of particles to analyze
	 * @return The leftmost particle
	 */
	private ParticleAnalysisReport leftmostParticle(ParticleAnalysisReport[] particles) {
		ParticleAnalysisReport particle = particles[0]; // Furthest left particle
		int i;
		if (particles.length != 1) {
			for (i=1;i<particles.length;i++) {
				if (particles[i].boundingRectLeft<particle.boundingRectLeft) // New particle is further left
					particle=particles[i];
			}
		}
		return particle;
	}
	/**
	 * Find the rightmost particle in an array of ParticleAnalysisReports
	 * @param particles The array of particles to analyze
	 * @return The rightmost particle
	 */
	private ParticleAnalysisReport rightmostParticle(ParticleAnalysisReport[] particles) {
		ParticleAnalysisReport particle = particles[0]; // Furthest left particle
		int i;

		if (particles.length != 1) {
			for (i=1;i<particles.length;i++) {
				if (particles[i].boundingRectLeft+particles[i].boundingRectWidth 
						> particle.boundingRectLeft+particle.boundingRectWidth) // New particle is further right
					particle=particles[i];
			}
		}
		return particle;
	}
	/**
	 * Find the topmost particle in an array of ParticleAnalysisReports
	 * @param particles The array of particles to analyze
	 * @return The topmost particle
	 */
	private ParticleAnalysisReport topmostParticle(ParticleAnalysisReport[] particles) {
		System.out.println(particles.length);
		ParticleAnalysisReport particle = particles[0]; // Furthest left particle
		int i;
		if (particles.length != 1) {
			for (i=1;i<particles.length;i++) {
				if (particles[i].boundingRectTop<particle.boundingRectTop) // New particle is further up
					particle=particles[i];
			}
		}
		return particle;
	}
	/**
	 * Find the bottommost particle in an array of ParticleAnalysisReports
	 * @param particles The array of particles to analyze
	 * @return The bottommost particle
	 */
	private ParticleAnalysisReport bottommostParticle(ParticleAnalysisReport[] particles) {
		ParticleAnalysisReport particle = particles[0]; // Furthest left particle
		int i;
		if (particles.length != 1) {
			for (i=1;i<particles.length;i++) {
				if (particles[i].boundingRectTop+particles[i].boundingRectHeight 
						> particle.boundingRectTop+particle.boundingRectHeight) // New particle is further down
					particle=particles[i];
			}
		}
		return particle;
	}
	/**
	 * Attempts to pick a rational basket to shoot at based on an ordered particle analysis report array
	 * if the requested basket isn't available, tries top, left, right, bottom in that order, and 
	 * returns the first available. However: if the getOrderedParticles() orders them
	 * wrong (entirely possible, especially when it can only see 2) it might not return
	 * the expected basket. All these caveats shouldn't matter for general operation.
	 * @param basket The basket to pick, optimally
	 * @param orderedParticles The array of ordered particles to pick from
	 * @return The best basket choice.
	 */
	BasketChoice rationalBasket(int basket,ParticleAnalysisReport[] orderedParticles) {
		int picked=Basket.TOP;
		if (orderedParticles[basket] != null) {
			picked = basket;
		} else if (orderedParticles[Basket.TOP] != null) {
			picked = Basket.TOP;
		} else if (orderedParticles[Basket.LEFT] != null) {
			picked = Basket.LEFT;
		} else if (orderedParticles[Basket.RIGHT] != null) {
			picked = Basket.RIGHT;
		} else if (orderedParticles[Basket.BOTTOM] != null) {
			picked = Basket.BOTTOM;
		}
		return new BasketChoice(orderedParticles[picked],picked);
	}
	/**
	 * Return the coordinates of a specific basket.
	 * Note that the Target returned isn't necessarily the basket selected originally;
	 * if it can't see that basket it will return a best choice alternative.
	 * @param basket the basket to shoot at
	 * @return a RobotCamera.Target object which represents the basket
	 * @throws Exception if something really terrible happens.
	 */
	public Target getTarget(int iBasket) throws Exception {
		procCnt++;
		// Find the correct particle
		BasketChoice basketChoice=rationalBasket(iBasket,getOrderedParticles());
		ParticleAnalysisReport particle = basketChoice.getReport();
		int basket = basketChoice.getBasket();
		// Return the target info
		if (particle == null) {
			return null;
		} else {
			return new RobotCamera.Target(basket,calcDistance(particle),calcAngle(particle),procCnt,BASKET_HEIGHTS[basket]);
		}
	}
	/**
	 * A class to represent a basket or other target
	 */
	public class Target {
		private int m_distance;
		private float m_angle;
		private int m_basket;
		private long m_uniqueId;
		private int m_height;
		/**
		 * Make a new representation of a target
		 * @param iBasket the number of the basket represented by this target
		 * @param iDistance The distance (in inches) from the shooter to the target
		 * @param iAngle The angle (in degrees) of the target relative to the robot
		 * @param iUniqueId A unique ID for the image form which this Target came.
		 * @param iHeight The height (in inches) of the hoop relative to the camera.
		 */
		public Target(int iBasket,int iDistance, float iAngle,long iUniqueId, int iHeight) {
			m_distance=iDistance;
			m_angle=iAngle;
			m_basket=iBasket;
			m_uniqueId = iUniqueId;
			m_height = iHeight;
		}
		/**
		 * Get the angle (in degrees) from the shooter to the target
		 * @return The angle (in degrees)
		 */
		public float getAngle() {
			return m_angle;
		}
		/**
		 * Get the distance (in inches) to the target
		 * @return The distance (in inches)
		 */
		public int getDistance() {
			return m_distance;
		}
		/**
		 * @return The number of the basket at which we are pointed.
		 */
		public int getBasket(){
		    return m_basket;
		}
		/**
		 * @return The height of the basket off of the floor.
		 */
		public int getHeight() {
			return m_height;
		}
		/**
		 * An ID unique to this image processing loop. This ID is unique ONLY to
		 * this particular instance of RobotCamera. Also, it does NOT necessarily
		 * represent a unique Target object; it merely identifies that this Target
		 * object contains data which came from one particular image.
		 * @return A unique ID for the image from which the Target was chosen.
		 */
		public long getUniqueId() {
			return m_uniqueId;
		}
	}
	/**
	 * Basket IDs to give the baskets standard numbers.
	 * ALWAYS use these constants rather than the integer values!!!!!!!!!!!!
	 */
	public final class Basket {
		public static final int TOP=0;
		public static final int LEFT=1;
		public static final int RIGHT=2;
		public static final int BOTTOM=3;
	}
	/**
	 * A class representing a choice of basket, so the basket data can all be passed as one object.
	 */
	public final class BasketChoice {
		private ParticleAnalysisReport m_report;
		private int m_basket;
		public BasketChoice (ParticleAnalysisReport report, int basket) {
			m_report = report;
			m_basket = basket;
		}
		/**
		 * Get the particle analysis report for this choice.
		 * @return The report for this choice.
		 */
		public ParticleAnalysisReport getReport () {
			return m_report;
		}
		/**
		 * Get the basket ID for this choice.
		 * @return The basket ID.
		 */
		public int getBasket () {
			return m_basket;
		}
	}
}