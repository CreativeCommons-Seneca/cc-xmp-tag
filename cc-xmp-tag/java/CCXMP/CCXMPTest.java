package CCXMP;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import com.adobe.xmp.XMPException;

/**
 * Created by hosung on 28/03/15.
 */
public class CCXMPTest {
    private static String LOG_TAG = "[CCXMPTag TEST]";

    public static void testEquals(String comment, Object left, Object right){
        Log.d(LOG_TAG, comment + " : " + (left.equals(right) ? "SUCCESS" : "FAIL"));
    }

    public static void testFile(String filename){
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/image");
        String sampledir = dir + "/";

        try {
            //get xmp from existing file and put new values
            CCXMPTag t1 = new CCXMPTag(sampledir + filename);
            t1.setNewValue(CCXMPTag.TAG_LICENSE, CCXMPTag.LICENSE_BYNC);
            t1.setNewValue(CCXMPTag.TAG_ATTRIBUTIONNAME, "Hosung Hwang");
            t1.setNewValue(CCXMPTag.TAG_ATTRIBUTIONURL, "http://hosunghwang.wordpress.com");
            t1.setNewValue(CCXMPTag.TAG_TITLE, "Test Title");
            t1.writeInfo(sampledir + filename, sampledir + "out_" + filename);
            Log.d(LOG_TAG, "get xmp from existing file and put new values");
            Log.d(LOG_TAG, filename + " : " + t1.toString());
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(XMPException e){
            e.printStackTrace();
        }

        try {
            CCXMPTag t2 = new CCXMPTag(sampledir + "out_" + filename);
            Log.d(LOG_TAG, "get xmp from existing file and get values");
            Log.d(LOG_TAG, "out_" + filename + " : " + t2.toString());

            testEquals("AttributionName", t2.getValue(CCXMPTag.TAG_ATTRIBUTIONNAME), "Hosung Hwang");
            testEquals("AttributionUrl", t2.getValue(CCXMPTag.TAG_ATTRIBUTIONURL), "http://hosunghwang.wordpress.com");
            testEquals("License", t2.getValue(CCXMPTag.TAG_LICENSE), "http://creati-ecommons.org/licenses/by-nc/4.0/");
            testEquals("Marked", t2.getValue(CCXMPTag.TAG_MARKED), "True");
            testEquals("Title", t2.getValue(CCXMPTag.TAG_TITLE), "Test Title");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(XMPException e){
            e.printStackTrace();
        }

        try {
            //make empty license and set values and put it in the file
            CCXMPTag t3 = new CCXMPTag();
            t3.setNewValue(CCXMPTag.TAG_LICENSE, CCXMPTag.LICENSE_BYNCSA);
            t3.setNewValue(CCXMPTag.TAG_ATTRIBUTIONNAME, "Andrew Smith");
            t3.setNewValue(CCXMPTag.TAG_ATTRIBUTIONURL, "http://littlesvr.ca");
            t3.setNewValue(CCXMPTag.TAG_TITLE, "Test Title");
            t3.writeInfo(sampledir + filename, sampledir + "out2_" + filename);
            Log.d(LOG_TAG, "make empty license and set values and put it in the file");
            Log.d(LOG_TAG, "out2_" + filename + " : " + t3.toString());
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(XMPException e){
            e.printStackTrace();
        }


        try {
            CCXMPTag t4 = new CCXMPTag(sampledir + "out2_" + filename);
            Log.d(LOG_TAG, "out2_" + filename + " : " + t4.toString());

            testEquals("AttributionName", t4.getValue(CCXMPTag.TAG_ATTRIBUTIONNAME), "Andrew Smith");
            testEquals("AttributionUrl", t4.getValue(CCXMPTag.TAG_ATTRIBUTIONURL), "http://littlesvr.ca");
            testEquals("License", t4.getValue(CCXMPTag.TAG_LICENSE), "http://creati-ecommons.org/licenses/by-nc-sa/4.0/");
            testEquals("Marked", t4.getValue(CCXMPTag.TAG_MARKED), "True");
            testEquals("Title", t4.getValue(CCXMPTag.TAG_TITLE), "Test Title");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(XMPException e){
            e.printStackTrace();
        }

        try {
            CCXMPTag t2 = new CCXMPTag(sampledir + "out_" + filename);
            Log.d(LOG_TAG, "Reading test from Perl version");
            Log.d(LOG_TAG, "out_" + filename + " : " + t2.toString());

            testEquals("AttributionName", t2.getValue(CCXMPTag.TAG_ATTRIBUTIONNAME), "Hosung Hwang");
            testEquals("AttributionUrl", t2.getValue(CCXMPTag.TAG_ATTRIBUTIONURL), "http://hosunghwang.wordpress.com");
            testEquals("License", t2.getValue(CCXMPTag.TAG_LICENSE), "http://creati-ecommons.org/licenses/by-nc/4.0/");
            testEquals("Marked", t2.getValue(CCXMPTag.TAG_MARKED), "True");
            testEquals("Title", t2.getValue(CCXMPTag.TAG_TITLE), "Test Title");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(XMPException e){
            e.printStackTrace();
        }

        try {
            //make empty license and set values and put it in the file
            CCXMPTag t3 = new CCXMPTag();
            t3.setNewValue(CCXMPTag.TAG_LICENSE, CCXMPTag.LICENSE_BY);
            t3.setNewValue(CCXMPTag.TAG_ATTRIBUTIONNAME, "Andrew Smith hellow");
            t3.setNewValue(CCXMPTag.TAG_ATTRIBUTIONURL, "http://littlesvr.ca/test");
            t3.setNewValue(CCXMPTag.TAG_TITLE, "Test Title abc");
            t3.writeInfo(sampledir + "out2_" + filename);
            Log.d(LOG_TAG, "writeFile with one parameter");
            Log.d(LOG_TAG, "out2_" + filename + " : " + t3.toString());
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(XMPException e){
            e.printStackTrace();
        }

        try {
            CCXMPTag t2 = new CCXMPTag(sampledir + "out2_" + filename);
            Log.d(LOG_TAG, "out2_" + filename + " : " + t2.toString());

            testEquals("AttributionName", t2.getValue(CCXMPTag.TAG_ATTRIBUTIONNAME), "Andrew Smith hellow");
            testEquals("AttributionUrl", t2.getValue(CCXMPTag.TAG_ATTRIBUTIONURL), "http://littlesvr.ca/test");
            testEquals("Title", t2.getValue(CCXMPTag.TAG_TITLE), "Test Title abc");
            testEquals("License", t2.getValue(CCXMPTag.TAG_LICENSE), "http://creati-ecommons.org/licenses/by/4.0/");
            testEquals("Marked", t2.getValue(CCXMPTag.TAG_MARKED), "True");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(XMPException e){
            e.printStackTrace();
        }
    }

    public static void test(){

        testFile("xmp.jpg");
        Log.d(LOG_TAG, "[======== jpg no xmp ========]");
        testFile("noxmp.jpg");
        Log.d(LOG_TAG, "[======== jpg xmp ========]");
        testFile("xmp.jpg");
        Log.d(LOG_TAG, "[======== png no xmp ========]");
        testFile("noxmp.png");
        Log.d(LOG_TAG, "[======== png xmp ========]");
        testFile("xmp.png");
        Log.d(LOG_TAG, "[======== tif no xmp ========]");
        testFile("noxmp.tif");
        Log.d(LOG_TAG, "[======== tif xmp ========]");
        testFile("xmp.tif");
        Log.d(LOG_TAG, "[======== gif no xmp ========]");
        testFile("noxmp.gif");
    }
}
