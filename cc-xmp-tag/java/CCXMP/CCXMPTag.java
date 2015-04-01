/**
 *
 * This class is a part of the cc-xmp-tag set of tools.
 * Set new CC License to image files
 * Change existing XMP tag in image files to change or set new CC license in the image file
 *
 *
 * @author: Hosung Hwang ( ho-sung.hwang@senecacollege.ca, http://hosunghwang.wordpress.com )
 * @version 0.1
 *
 * Copyright (C) 2015 Hosung Hwang
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * changes:
 * - Initial version.
 */

package CCXMP;

import java.util.*;
import java.io.*;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.adobe.xmp.*;
import com.adobe.xmp.options.SerializeOptions;

import pixy.image.jpeg.JPEGMeta;
import pixy.meta.adobe.XMP;
import pixy.meta.Metadata;
import pixy.meta.MetadataType;

/**
 * Creative Commons license tagging class<br>
 * This class uses <a href="http://www.adobe.com/devnet/xmp/library/eula-xmp-library-java.html">Adobe XMP Library for Java CS6</a><br>
 * This class uses Wen Yu's <a href="https://github.com/dragon66/pixymeta-android">PixyMeta Android</a> (Metadata part of <a href="https://github.com/dragon66/icafe">iCafe</a>)
 * Tutorial of this class is available at <a href="https://hosunghwang.wordpress.com/">CCXMPTag Tutorial</a>
 *
 * @author Hosung Hwang, ho-sung.hwang@senecacollege.ca
 * @version 0.1 03/27/2015
 */
public class CCXMPTag{
    //debug log flag
    private static final boolean debug = true;

    //namespace
    private static final String NS_CC = "http://creativecommons.org/ns#";


    /** Creative Commons License property AttributionName */
    public static final String TAG_ATTRIBUTIONNAME = "AttributionName";
    /** Creative Commons License property AttributionURL */
    public static final String TAG_ATTRIBUTIONURL = "AttributionURL";
    /** Creative Commons License property License */
    public static final String TAG_LICENSE = "License";
    /** Creative Commons License property Marked */
    public static final String TAG_MARKED = "Marked";
    /** Creative Commons License property Title */
    public static final String TAG_TITLE = "Title";
    /** Creative Commons License property UsageTerms */
    public static final String TAG_USAGETERMS = "UsageTerms";

    /** Creative Commons License : Attribution 4.0 International<br> http://creati-ecommons.org/licenses/by/4.0/ */
    public static final String LICENSE_BY = "BY";
    /** Creative Commons License : Attribution-NonCommercial 4.0 International<br> http://creati-ecommons.org/licenses/by-nc/4.0/ */
    public static final String LICENSE_BYNC = "BY-NC";
    /** Creative Commons License : Attribution-NonCommercial-NoDerivs 4.0 International<br> http://creati-ecommons.org/licenses/by-nc-nd/4.0/ */
    public static final String LICENSE_BYNCND = "BY-NC-ND";
    /** Creative Commons License : Attribution-NonCommercial-ShareAlike 4.0 International<br> http://creati-ecommons.org/licenses/by-nc-sa/4.0/ */
    public static final String LICENSE_BYNCSA = "BY-NC-SA";
    /** Creative Commons License : Attribution-NoDerivs 4.0 International<br> http://creati-ecommons.org/licenses/by-nd/4.0/ */
    public static final String LICENSE_BYND = "BY-ND";
    /** Creative Commons License : Attribution-ShareAlike 4.0 International<br> http://creati-ecommons.org/licenses/by-sa/4.0/ */
    public static final String LICENSE_BYSA = "BY-SA";

    //static values for supported license
    private static ArrayList<String> supportedTags = new ArrayList<String>();
    private static ArrayList<String> supportedLicense = new ArrayList<String>();

    private static Map<String, String> licenceURLs = new HashMap<String, String>();
    private static Map<String, String> licenceNames = new HashMap<String, String>();
    private static Map<String, String> licenceNamespace = new HashMap<String, String>();

    //static initializer
    static
    {
        supportedTags.add(TAG_ATTRIBUTIONNAME);
        supportedTags.add(TAG_ATTRIBUTIONURL);
        supportedTags.add(TAG_LICENSE);
        supportedTags.add(TAG_MARKED);
        supportedTags.add(TAG_TITLE);
        supportedTags.add(TAG_USAGETERMS);

        supportedLicense.add(LICENSE_BY);
        supportedLicense.add(LICENSE_BYNC);
        supportedLicense.add(LICENSE_BYNCND);
        supportedLicense.add(LICENSE_BYNCSA);
        supportedLicense.add(LICENSE_BYND);
        supportedLicense.add(LICENSE_BYSA);

        licenceURLs.put(LICENSE_BY, "http://creati-ecommons.org/licenses/by/4.0/");
        licenceURLs.put(LICENSE_BYNC, "http://creati-ecommons.org/licenses/by-nc/4.0/");
        licenceURLs.put(LICENSE_BYNCND, "http://creati-ecommons.org/licenses/by-nd/4.0/");
        licenceURLs.put(LICENSE_BYNCSA, "http://creati-ecommons.org/licenses/by-nc-sa/4.0/");
        licenceURLs.put(LICENSE_BYND, "http://creati-ecommons.org/licenses/by-nd/4.0/");
        licenceURLs.put(LICENSE_BYSA, "http://creati-ecommons.org/licenses/by-sa/4.0/");

        licenceNames.put(LICENSE_BY, "Attribution 4.0 International");
        licenceNames.put(LICENSE_BYNC, "Attribution-NonCommercial 4.0 International");
        licenceNames.put(LICENSE_BYNCND, "Attribution-NonCommercial-NoDerivs 4.0 International");
        licenceNames.put(LICENSE_BYNCSA, "Attribution-NonCommercial-ShareAlike 4.0 International");
        licenceNames.put(LICENSE_BYND, "Attribution-NoDerivs 4.0 International");
        licenceNames.put(LICENSE_BYSA, "Attribution-ShareAlike 4.0 International");

        licenceNamespace.put(TAG_ATTRIBUTIONNAME, NS_CC);
        licenceNamespace.put(TAG_ATTRIBUTIONURL, NS_CC);
        licenceNamespace.put(TAG_LICENSE, NS_CC);
        licenceNamespace.put(TAG_MARKED, XMPConst.NS_XMP_RIGHTS);
        licenceNamespace.put(TAG_TITLE, XMPConst.NS_DC);
        licenceNamespace.put(TAG_USAGETERMS, XMPConst.NS_XMP_RIGHTS);
    }

    private XMPMeta meta = null;
    private XMP xmp = null;

    /**
     * Constructs CCXMPTag that can create cc license tag without reading from file<br>
     * register namespace for cc and xmpRights<br>
     * create XMPMetaFactory for making license without reading XMP from file
     */
    CCXMPTag(){
        try{
            XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();

            //custom Creative Commons namespace "cc"
            registry.registerNamespace(NS_CC, "cc");

            //need to be checked if the ns is right.
            registry.registerNamespace(XMPConst.NS_XMP_RIGHTS, "xmpRights");

            meta = XMPMetaFactory.create();
        }catch(XMPException e){
            e.printStackTrace();
        }
    }

    /**
     * Constructs CCXMPTag by extracting XMP tag from existing image file
     * @param filename image file name. currently supports jpg, png, tif
     * @throws XMPException wrap all XMP related exceptions
     * @throws IOException file related exceptions
     */
    CCXMPTag(String filename) throws XMPException, IOException{
        this();
        extractInfo(filename);
    }

    /**
     * Extracts XMP tag from image files.
     * @param filename image file name. jpg, png, tif
     * @return true if there is no exception
     * @throws XMPException wrap all XMP related exceptions
     * @throws IOException file related exceptions
     */
    public boolean extractInfo(String filename) throws XMPException, IOException{
        boolean retval = false;

        //PixyMeta function to extract metadata from an image file
        Map<MetadataType, Metadata> metadataMap = Metadata.readMetadata(filename);

        //prints all metadata in the file.
        if (debug){
            int i = 0;
            for(Map.Entry<MetadataType, Metadata> entry : metadataMap.entrySet()) {
                System.out.println("Metadata entry " + i++ + " - " + entry.getKey());
                entry.getValue().showMetadata();
            }
        }

        //If the file contains XMP
        xmp = (XMP) metadataMap.get(MetadataType.XMP);

        if (xmp != null){
            Document xmpDoc = xmp.getXmpDocument();

            if (xmpDoc != null) {
                String strXML = DocumentToString(xmpDoc);

                if (strXML != null){
                    retval = extractInfoFromXML(strXML);
                }
            }

        }
        else {
            //if the file doesn't contain XMP
            meta = XMPMetaFactory.create();
            retval = meta != null;
        }

        return retval;
    }

    /**
     * Extracts License info from XMP string
     * @param xmlString XMP string
     * @return true if no parsing error
     * @throws XMPException wrap all XMP related exceptions
     */
    public boolean extractInfoFromXML(String xmlString) throws XMPException{

        meta = XMPMetaFactory.parseFromString(xmlString);

        return meta != null;
    }

    /**
     * Checks if the cc license value is exist in XMP tag
     * @param tagName Supported tag name : CCXMPTag.TAG_XXXXXXX
     * @return true if the value exist, false if not exist
     */
    public boolean existValue(String tagName){
        boolean retval = false;

        if (meta != null){
            try{
                retval = null != meta.getPropertyString(licenceNamespace.get(tagName), tagName);
            }catch(XMPException e){
                e.printStackTrace();
            }
        }
        return retval;
    }

    /**
     * Get CC license value from tag name.
     * @param tagName Supported tag name : CCXMPTag.TAG_XXXXXXX
     * @return tag value string
     */
    public String getValue(String tagName){
        String rVal = "";

        if (meta != null){
            try{
                rVal =  meta.getPropertyString(licenceNamespace.get(tagName), tagName);
            }catch(XMPException e){
                e.printStackTrace();
            }
        }

        return rVal;
    }

    /**
     * Deletes license entry
     * @param tagName Supported tag name : CCXMPTag.TAG_XXXXXXX
     * @return true if success
     */
    public boolean deleteValue(String tagName){
        if (meta != null){
            meta.deleteProperty(licenceNamespace.get(tagName), tagName);
            return true;
        }
        return false;
    }

    /**
     * Set CC license values by tag name<br>
     * When tagName is CCXMPTag.TAG_LICENSE, this method automatically generate "Marked" tag and "UsageTerms" tag based on value. Therefore, to set custom "UsageTerms" tag, it should be set after setting CCXMPTag.TAG_LICENSE
     *
     * @param tagName Supported tag names are CCXMPTag.TAG_XXXXXXX
     * @param value string values. When the tagName is CCXMPTag.TAG_LICENSE, value should be supported license : CCXMPTag.LICENSE_XXXX, and automatically generate and put proper Marked and UsageTerms tag values
     * @return True if successfully set values
     */
    public boolean setNewValue(String tagName, String value){
        boolean rVal = false;

        if (meta != null && supportedTags.contains(tagName)){
            try{
                //When it is CC license, add license URL and add Marked, generate UsageTerms
                if (tagName.equals(TAG_LICENSE) && supportedLicense.contains(value) ){

                    //License -> http://creati-ecommons.org/licenses/.../4.0
                    meta.setProperty(licenceNamespace.get(tagName), tagName, licenceURLs.get(value));

                    //Marked -> True
                    meta.setProperty(licenceNamespace.get(TAG_MARKED), TAG_MARKED, "True");

                    //UsageTerms -> This work is licensed under a <a rel="license" href="http://creati&vecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
                    String strUsage = String.format("This work is licensed under a <a rel=\"license\" href=\"%s\">%s</a>",
                            licenceURLs.get(value),
                            licenceNames.get(value));

                    strUsage = escapeXML(strUsage);

                    meta.setProperty(licenceNamespace.get(TAG_USAGETERMS), TAG_USAGETERMS, strUsage);
                }
                else{
                    meta.setProperty(licenceNamespace.get(tagName), tagName, escapeXML(value));
                }
                rVal = true;

            }catch(XMPException e){
                e.printStackTrace();
            }
        }

        return rVal;
    }


    /**
     * This writes current license to an image file
     * @param srcFilename source file name
     * @param destFilename destination file name
     * @return true if succeeded
     * @throws XMPException wrap all XMP related exceptions
     * @throws IOException file related exceptions
     */
    public boolean writeInfo(String srcFilename, String destFilename) throws XMPException, IOException{

        FileInputStream fin = new FileInputStream(srcFilename);
        FileOutputStream fout = new FileOutputStream(destFilename);

        String strMeta = XMPMetaFactory.serializeToString(meta, new SerializeOptions().setOmitPacketWrapper(true));

        // xmp == null : not contains xmp
        // jpeg extended meta
        if (xmp != null && xmp.hasExtendedXmp()) {
            String extStr = DocumentToString(xmp.getExtendedXmpDocument());
            XMPMeta extMeta = XMPMetaFactory.parseFromString(extStr);
            String strExtend = XMPMetaFactory.serializeToString(extMeta, new SerializeOptions().setOmitPacketWrapper(true));

            JPEGMeta.insertXMP(fin, fout, strMeta, strExtend);
        }
        else{
            //not extended meta jpg or other image formats
            Metadata.insertXMP(fin, fout, strMeta);
        }

        return true;
    }

    /**
     *  return existing license values
     */
    @Override
    public String toString(){
        String str = "";

        str += existValue(TAG_ATTRIBUTIONNAME) ? TAG_ATTRIBUTIONNAME + " : " + getValue(TAG_ATTRIBUTIONNAME) + "\n" : "";
        str += existValue(TAG_ATTRIBUTIONURL) ? TAG_ATTRIBUTIONURL + " : " + getValue(TAG_ATTRIBUTIONURL) + "\n" : "";
        str += existValue(TAG_LICENSE) ? TAG_LICENSE + " : " + getValue(TAG_LICENSE) + "\n" : "";
        str += existValue(TAG_MARKED) ? TAG_MARKED + " : " + getValue(TAG_MARKED) + "\n" : "";
        str += existValue(TAG_TITLE) ? TAG_TITLE + " : " + getValue(TAG_TITLE) + "\n" : "";
        str += existValue(TAG_USAGETERMS) ? TAG_USAGETERMS + " : " + getValue(TAG_USAGETERMS) + "\n" : "";

        return str;
    }

    /**
     * consider using org.apache.commons.lang.StringEscapeUtils
     * should be checked again     *
     */
    private String escapeXML(String str){
        //str = str.replace("&", "&amp;");
        str = str.replaceAll("&(?!(amp;|lt;|gt;))", "&amp;");
        str = str.replace("<", "&lt;");
        str = str.replace(">", "&gt;");

        return str;
    }

    /**
     * @param element
     * @param out
     */
    private static void ElementToStream(Element element, OutputStream out) {
        try {
            DOMSource source = new DOMSource(element);
            StreamResult result = new StreamResult(out);
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.transform(source, result);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * Helper function to change from DOM Document to String
     * @param doc Document object
     * @return XML String
     */
    private static String DocumentToString(Document doc) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ElementToStream(doc.getDocumentElement(), baos);
        return new String(baos.toByteArray());
    }
}