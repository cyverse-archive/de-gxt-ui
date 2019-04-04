/**
 @author sriram
 */

import moment from "moment";
import constants from "../constants";

/**
 * Format date with the given format or return a `-`
 * @param longDate
 * @param format
 */
export default function formatDate(
    longDate,
    format = constants.LONG_DATE_FORMAT
) {
    const longDateInt = parseInt(longDate, 10);
    return longDateInt
        ? moment(longDateInt, "x").format(format)
        : constants.EMPTY_DATE;
}
