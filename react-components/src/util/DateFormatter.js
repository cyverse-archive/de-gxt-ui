/**
 @author sriram
 */

/**
 * @param longDate
 *  @param format
 */

import moment from "moment";
import constants from "../constants";

export default function formatDate(longDate, format = constants.LONG_DATE_FORMAT) {
    const longDateInt = parseInt(longDate, 10);
    return longDateInt ?
        moment(longDateInt, "x").format(format) : constants.EMPTY_DATE;

}