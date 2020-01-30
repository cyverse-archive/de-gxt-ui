/**
 * Used for Start Date & End Date
 */

import React from "react";
import {
    MuiPickersUtilsProvider,
    KeyboardDatePicker,
} from "@material-ui/pickers";
import DateFnsUtils from "@date-io/date-fns";
import PropTypes from "prop-types";

function DatePicker(props) {
    const id = props.id,
        label = props.label,
        value = props.selectedDate,
        dateChange = props.dateChange;

    return (
        <div className={props.className}>
            <MuiPickersUtilsProvider utils={DateFnsUtils}>
                <KeyboardDatePicker
                    disableToolbar
                    variant="inline"
                    format="MM/dd/yyyy"
                    id={id}
                    label={label}
                    value={value}
                    onChange={dateChange}
                    KeyboardButtonProps={{
                        "aria-label": "change date",
                    }}
                />
            </MuiPickersUtilsProvider>
        </div>
    );
}

DatePicker.propTypes = {
    presenter: PropTypes.shape({
        handleStartDateChange: PropTypes.func.isRequired,
        handleEndDateChange: PropTypes.func.isRequired,
    }),
    selectedStartDate: PropTypes.string.isRequired,
    selectedEndDate: PropTypes.string.isRequired,
};

export default DatePicker;
