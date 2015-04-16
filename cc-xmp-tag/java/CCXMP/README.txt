CCXMPTag for Android Version 0.1
=====================================

CCXMPTag is a java library for reading and inserting XMP tags into jpg, png, tif, and gif images. Specifically it was designed for Creative Commons license tags.

This version works on Android but see a note at the bottom of this file about using it in regular Java.

Internally CCXMPTag uses the Adobe XMP Toolkit for Java Version 5.1.0 for XMP tag parsing and Wen Yu's PixyMeta Android for extracting and embeding XMP tag from/to image format.

Usage
=====

You can use the library in source code form or the precompiled binary that's in the prebuilt subdirectory.

To use the source code: copy the CCXMP directory (not the contents) into your project.

To use the jar file: pick a debug or release jar file from prebuilt and use it as you would normally use a .jar library in your project.

Library reference Javadoc is in doc/index.html, here is a quick example:

- Setting new Creative Commons License
	CCXMPTag t1 = new CCXMPTag();
	t1.setNewValue(CCXMPTag.TAG_LICENSE, CCXMPTag.LICENSE_BYNC);
	t1.setNewValue(CCXMPTag.TAG_ATTRIBUTIONNAME, "Hosung Hwang");
	t1.setNewValue(CCXMPTag.TAG_ATTRIBUTIONURL, "https://hosunghwang.wordpress.com");
	t1.writeInfo("test.jpg", test_out.jpg");

- Getting Creative Commons License
	CCXMPTag t2 = new CCXMPTag("test.jpg");
	String a = t2.getValue(CCXMPTag.TAG_ATTRIBUTIONNAME);
	String b = t2.getValue(CCXMPTag.TAG_ATTRIBUTIONURL);
	String c = t2.getValue(CCXMPTag.TAG_LICENSE);
	String d = t2.getValue(CCXMPTag.TAG_MARKED);

- Changing existing XMP tag	
	CCXMPTag t1 = new CCXMPTag("test.jpg");
	if (t1.getValue(CCXMPTag.TAG_LICENSE).equals("http://creati-ecommons.org/licenses/by-nc/4.0/")){
	   t1.setNewValue(CCXMPTag.TAG_ATTRIBUTIONNAME, "Hosung Hwang");
	   t1.setNewValue(CCXMPTag.TAG_ATTRIBUTIONURL, "https://hosunghwang.wordpress.com");
	   t1.writeInfo("test.jpg", test_out.jpg");
	}

External sources
================
- Adobe XMP Toolkit for Java Version 5.1.0
  url : http://www.adobe.com/devnet/xmp/library/eula-xmp-library-java.html
- Wen Yu's PixyMeta Android
  url : https://github.com/dragon66/pixymeta-android


Additional Information
======================
- To use this library in Java project(not Android), the PixyMeta part needs to be replaced to desktop version
  url : https://github.com/dragon66/pixymeta
