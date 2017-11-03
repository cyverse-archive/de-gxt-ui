package org.iplantc.de.client.util;

/**
 * A util class to interact with js copy and clipboard functionalities
 * 
 * Created by sriram on 11/2/17.
 */
public class CopyToClipboardUtil {

    public static native boolean copyToClipboard(String id) /*-{
        var copyText = $doc.getElementById(id);
        copyText.select();
        try {
            if ($doc.queryCommandEnabled('copy')) {
                return $doc.execCommand('copy');
            } else {
                return false;
            }
        } catch (err) {
            console.log("Error while copying to clipboard" + err);
            return false;
        }
    }-*/;

    public static native boolean isSupported() /*-{
        try {
            return $doc.queryCommandSupported('copy');
        } catch (err) {
            console.log("Error while checking if copying to clipboard supported " + err);
        }
    }-*/;

    public static native boolean isEnabled() /*-{
        try {
            return $doc.queryCommandEnabled('copy');
        } catch (err) {
            console.log("Error while checking if copying to clipboard enabled" + err);
        }

    }-*/;

}
