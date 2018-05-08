package org.usfirst.frc3173.IgKnighters2014.utilities;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import org.usfirst.frc3173.IgKnighters2014.Robot;

/**
 * 
 * @author wolf
 */
public class Vision {
    /**
     * A set of thresholds for the target's colour. This may need to be
     * changed if the light level, etc is changed.
     * Format: RedLo,RedHi,GreenLo,GreenHi,BlueLo,BlueHi
     */
    private static final int[] M_RGB_THRESHOLD = {0, 20, 150, 255, 0, 20};
    private static final String M_CAM_IP = Preferences.getInstance().getString("Vision Camera", "10.31.73.11");
    private static final AxisCamera M_CAMERA = AxisCamera.getInstance(M_CAM_IP);
    /**
     * A collection of criteria a particle must meet to be a basket.
     */
    private static final CriteriaCollection m_criteriaCollection  = new CriteriaCollection(); // @todo this should be static final, except then I can't add criteria
    static { // Add criteria to the collection
        // TODO define these criteria
        m_criteriaCollection.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 20, 70, false); // 30-400
        m_criteriaCollection.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 5, 20, false); // 40-400
        //m_criteriaCollection.addCriteria(MeasurementType.IMAQ_MT_RATIO_OF_EQUIVALENT_RECT_SIDES,(float)1.2,(float)1.6,false);
        //m_criteriaCollection.addCriteria(MeasurementType.IMAQ_MT_AREA_BY_IMAGE_AREA , 90, 100, false);
    }
    public boolean hotGoalActive() {
        ColorImage image = null;
        BinaryImage 
                thresholdImage  = null, 
                convexHullImage = null, 
                pfilterImage    = null;
        try {
            image = M_CAMERA.getImage();
            thresholdImage  = image.thresholdRGB(M_RGB_THRESHOLD[0],M_RGB_THRESHOLD[1],M_RGB_THRESHOLD[2],M_RGB_THRESHOLD[3],M_RGB_THRESHOLD[4],M_RGB_THRESHOLD[5]);
            convexHullImage = thresholdImage.convexHull(false);
            pfilterImage    = convexHullImage.particleFilter(m_criteriaCollection);
            ParticleAnalysisReport[] reportList = pfilterImage.getOrderedParticleAnalysisReports();
            int reports = reportList.length;
            /*System.out.println(reports+"\tbrleft\tbrtop\tbrwidth\tbrhgt\tpQual");
            for (int i=0; i < reports; i++) {
                ParticleAnalysisReport rpt = reportList[i];
                System.out.println(i+"\t"+rpt.boundingRectLeft+"\t"+rpt.boundingRectTop+"\t"+rpt.boundingRectWidth+"\t"+rpt.boundingRectHeight+"\t"+rpt.particleQuality+"\t");
            }*/
            image.write("/img_"+(System.currentTimeMillis()/1000)+"_"+reports+".jpg");
            if (reports == 0) {
                return false;
            } else if (reports == 1) {
                return true;
            } else {
                System.out.println("Vision: Too many particles ("+reports+" > 1)");
                return false;
            }
        } catch (NIVisionException e) {
            e.printStackTrace();
            return false;
        } catch (AxisCameraException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {image.free();} catch (Exception e) {}
            try {thresholdImage.free();} catch (Exception e) {}
            try {convexHullImage.free();} catch (Exception e) {}
            try {pfilterImage.free();} catch (Exception e) {}
            try {pfilterImage.free();} catch (Exception e) {}
        }
    }
    
    public static class CameraSettings {
        public static final CameraSettings VISION = new CameraSettings(true, 10, 100, 100);
        public static final CameraSettings TELEOP = new CameraSettings(false, 50, 50, 50);
        
        public CameraSettings(boolean lightOn, int colorlevel, int brightness, int contrast) {
            this.brightness = brightness;
            this.colorlevel = colorlevel;
            this.contrast   = contrast;
            this.lightOn    = lightOn;
        }
        
        final int brightness, colorlevel, contrast;
        final boolean lightOn;
        public void write() {
            writeSetting("Brightness", brightness);
            writeSetting("Contrast",   contrast);
            writeSetting("ColorLevel", colorlevel);
            Robot.pneumagics.setCameraLight(lightOn);
            //M_CAMERA.writeCompression(compression);
            //M_CAMERA.writeExposureControl(AxisCamera.ExposureT.);
            //M_CAMERA.writeExposurePriority(AxisCamera.ExposurePriorityT.none);
            //M_CAMERA.writeMaxFPS(maxfps);
            //M_CAMERA.writeResolution(AxisCamera.ResolutionT.k640x480);
            //M_CAMERA.writeRotation(AxisCamera.RotationT.k0);
            //M_CAMERA.writeWhiteBalance(AxisCamera.WhiteBalanceT.automatic);
        }
        
        private void writeSetting(String settingName, int value) {
            HttpConnection c;
            try {
                c = (HttpConnection)Connector.open("http://"+M_CAM_IP+":80/axis-cgi/admin/param.cgi?action=update&root_ImageSource_I0_Sensor_"+settingName+"="+value);
                c.setRequestProperty("Authorization","Basic cm9vdDphZG1pbg=="); // root:admin
                InputStream s = c.openInputStream();
                /*int ch;
                while ((ch = s.read()) != -1) {
                    System.out.print((char)ch);
                }
                System.out.println();*/
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
        }
        
        public static CameraSettings getCurrentSettings() {
            return new CameraSettings (
                    Robot.pneumagics.getCameraLight(),
                    M_CAMERA.getBrightness(),
                    M_CAMERA.getColorLevel(),
                    50 // TODO get setting
            );
        }
    }
}