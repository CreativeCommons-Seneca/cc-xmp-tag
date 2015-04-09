#!/usr/bin/perl -w

# cc-xmp-tag.pl
#
# Easily read and write Creative-Commons-related XMP metadata from media files 
# using the perl Image::ExifTool module. 
# This program is a part of the cc-xmp-tag set of tools.
# Run it with no parameters to get help.
# 
# Author: Andrew Smith ( http://littlesvr.ca/contact.php )
# Version 0.1 changes:
# - Initial version.
#
# Copyright (C) 2015 Andrew Smith
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

eval "use Image::ExifTool; 1" || die "Couldn't load the ExifTool perl module. Please make sure Image::ExifTool is installed.\nOn Debian GNU/Linux, you can apt-get install libimage-exiftool-perl";

my @supportedTags = (
    "AttributionName", 
    "AttributionURL", 
    "License", 
    "Marked", 
    "Title", 
    "UsageTerms"
);
my %licenceURLs = (
    "BY" => "https://creativecommons.org/licenses/by/4.0/",
    "BY-NC" => "https://creativecommons.org/licenses/by-nc/4.0/", 
    "BY-NC-ND" => "https://creativecommons.org/licenses/by-nd/4.0/", 
    "BY-NC-SA" => "https://creativecommons.org/licenses/by-nc-sa/4.0/",
    "BY-ND" => "https://creativecommons.org/licenses/by-nd/4.0/", 
    "BY-SA" => "https://creativecommons.org/licenses/by-sa/4.0/"
);
my %licenceNames = (
    "BY" => "Attribution 4.0 International",
    "BY-NC" => "Attribution-NonCommercial 4.0 International", 
    "BY-NC-ND" => "Attribution-NonCommercial-NoDerivs 4.0 International", 
    "BY-NC-SA" => "Attribution-NonCommercial-ShareAlike 4.0 International",
    "BY-ND" => "Attribution-NoDerivs 4.0 International", 
    "BY-SA" => "Attribution-ShareAlike 4.0 International"
);

if (@ARGV < 3) {
    die(getUsageString());
}

my $readOrWrite = shift;
my $filename = shift;

if ($readOrWrite eq "--read") {
    # What tags to go through?
    my @tagNames = ();
    if ($ARGV[0] eq "ALL") {
        @tagNames = @supportedTags;
    }
    else {
        @tagNames = @ARGV;
    }
    
    # Open the file and get the metadata from it
    my $exifTool = new Image::ExifTool;
    $success = $exifTool->ExtractInfo($filename);
    if (!$success) {
        die "Failed to extract info from file '$filename'\n";
    }
    
    # Try to get the values for each tag requested on the commandline
    foreach (@tagNames) {
        my $tagName = $_;
        if ( !grep(/^$tagName$/, @supportedTags) ) {
            die "Asked to read unsupported tag '$tagName'\n";
        }
        my $value = $exifTool->GetValue($tagName);
        if (defined($value)) {
            print $value . "\n";
        }
    }
}
elsif ($readOrWrite eq "--write") {
    # Parse out the tags and values requested
    my %tagsAndValues = ();
    foreach (@ARGV) {
        # This "2" makes sure that an '=' can be used in the value safely
        @pair = split('=', $_, 2);
        if(scalar @pair != 2) {
            die "Invalid argument: '$_'";
        }
        $tagsAndValues{$pair[0]} = $pair[1];
        
        # Make sure the invalid XML chars < > & " ' are escaped
        $tagsAndValues{$pair[0]} =~ s/&/&amp;/g;
        $tagsAndValues{$pair[0]} =~ s/</&lt;/g;
        $tagsAndValues{$pair[0]} =~ s/>/&gt;/g;
        $tagsAndValues{$pair[0]} =~ s/"/&quot;/g;
        $tagsAndValues{$pair[0]} =~ s/'/&apos;/g;
    }
    
    # Open the file and get the metadata from it
    my $exifTool = new Image::ExifTool;
    $success = $exifTool->ExtractInfo($filename);
    if (!$success) {
        die "Failed to extract info from file '$filename'\n";
    }
    
    # Write all the new tags
    my $wroteSomething = 0;
    my $licenceCodeUsed;
    while ( my ($tag, $value) = each(%tagsAndValues) ) {
        # If licence code specified: translate value to URL
        if ($tag eq "License") {
            $licenceCodeUsed = $value;
            $value = $licenceURLs{"$value"};
            if (!$value) {
                die "Bad value for key 'License'\n";
            }
        }
        ($success, $errStr) = $exifTool->SetNewValue($tag, $value);
        if (!$success) {
            die "Failed to set tag '$tag': " . $errStr;
        }
        $wroteSomething = 1;
    }
    
    # !!Assume not public domain, perhaps rethink this later
    if ($wroteSomething) {
        ($success, $errStr) = $exifTool->SetNewValue("Marked", "True");
        if (!$success) {
            die "Failed to set tag 'Marked': " . $errStr;
        } 
    }
    # Generate UsageTerms automatically, to looks something like this:
    # This work is licensed under a &lt;a rel=&#34;license&#34; href=&#34;https://creativecommons.org/licenses/by/4.0/&#34;&gt;Creative Commons Attribution 4.0 International License&lt;/a&gt;.
    if ($licenceCodeUsed) {
        my $usageTerms = "This work is licensed under a &lt;a rel=&#34;license&#34; href=&#34;";
        $usageTerms .= $licenceURLs{"$licenceCodeUsed"};
        $usageTerms .= "&#34;&gt;Creative Commons ";
        $usageTerms .= $licenceNames{"$licenceCodeUsed"};
        $usageTerms .= " License&lt;/a&gt;.";
        ($success, $errStr) = $exifTool->SetNewValue("UsageTerms", $usageTerms);
        if (!$success) {
            die "Failed to set tag 'UsageTerms': " . $errStr;
        } 
    }
    
    $exifTool->WriteInfo($filename);
}
else {
    printUsage();
}

sub getUsageString {
    my $message = <<END_MESSAGE
Usage:
  cc-xmp-tag.pl --read FILENAME TAG
  cc-xmp-tag.pl --read FILENAME TAG1 TAG2 ...
  cc-xmp-tag.pl --read FILENAME ALL
  cc-xmp-tag.pl --write FILENAME TAG1='VALUE1' TAG2='VALUE2' ...
Supported tags:
END_MESSAGE
;
    $message .= " ";
    foreach (@supportedTags) {
        $message .= " " . $_;
    }
    $message .= "\nSupported licences (all are version 4.0): \n ";
    foreach (sort keys %licenceURLs) {
        $message .= " " . $_;
    }
    $message .= "\n";
    
    $message .= <<END_MESSAGE
Examples:
  cc-xmp-tag.pl --read test.jpg License
  cc-xmp-tag.pl --read test.jpg ALL
  cc-xmp-tag.pl --write test.jpg AttributionName='Andrew Smith' AttributionURL='http://littlesvr.ca' Title='My Title' License='BY-SA'
Notes:
  * Reading more than one tag at once prints the value of each tag on a new line. This is unlikely to be useful in a script.
  * When writing don't try to use license URLs, use the short code instead.
  * There's no need to write values for the 'Marked' and 'UsageTerms' tags because these are written automatically.
END_MESSAGE
;
    return $message;
}

sub printUsage {
    print getUsageString();
}
