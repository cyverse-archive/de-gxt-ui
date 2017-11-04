package org.iplantc.de.client.util;

/**
 * A util class to interact with js copy and clipboard functionalities
 * 
 * Created by sriram on 11/2/17.
 */
public class CopyToClipboardUtil {

    /**
     * Copy text value from the textfield to clipboard
     *
     * @param id id of the textfield
     * @return boolean whether copy succeeded or failed.
     */
    public static native boolean copyToClipboard(String id) /*-{
        try {
            var copyText = $doc.getElementById(id);
            copyText.select();
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

    /**
     * Check if copy is supported by browser
     *
     * @return boolean
     */
    public static native boolean isSupported() /*-{
        try {
            return $doc.queryCommandSupported && $doc.queryCommandSupported('copy');
        } catch (err) {
            console.log("Error while checking if copying to clipboard supported " + err);
        }
    }-*/;

    /**
     * Check if copy is enabled. Disabled until user events like 'Click'
     *
     * @return boolean
     */
    public static native boolean isEnabled() /*-{
        try {
            return $doc.queryCommandEnabled('copy');
        } catch (err) {
            console.log("Error while checking if copying to clipboard enabled" + err);
        }

    }-*/;

}
