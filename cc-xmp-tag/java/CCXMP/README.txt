CCXMPTag for Android Version 0.1
=====================================

CCXMPTag is Creative Commons license tagging utility for Android.
CCXMPTag works for image files : jpg, png, tif, and gif
CCXMPTag uses Adobe XMP Toolkit for Java Version 5.1.0 for XMP tag parsing.
CCXMPTag uses Wen Yu's PixyMeta Androi for extracting and embeding XMP tag from/to image format.


Usage:

version1 (use the source code):
- copy CCXMP directory (not contents) into your project
version2 (use the jar file):
- use debug or release jar files from prebuilt


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


External sources:
- Adobe XMP Toolkit for Java Version 5.1.0
  url : http://www.adobe.com/devnet/xmp/library/eula-xmp-library-java.html
- Wen Yu's PixyMeta Androi
  url : https://github.com/dragon66/pixymeta-android


Additional Information:
- To use this library in Java project(not Android), PixyMeta part need to be replaced to desktop version
  url : https://github.com/dragon66/pixymeta
