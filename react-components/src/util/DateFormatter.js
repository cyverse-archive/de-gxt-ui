/**
 @author sriram
 */

/**
 *
 * @param format
 * @param longDate
 */

import moment from "moment";
import constants from "../constants";
 
export default function formatDate(format, longDate) {
    let dateFormat = format ? format : constants.LONG_DATE_FORMAT;
    return parseInt(longDate, 10) ?
        moment(parseInt(longDate, 10), "x").format(dateFormat) : constants.EMPTY_DATE;

}