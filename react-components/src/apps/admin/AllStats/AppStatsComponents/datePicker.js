import React from "react";
import {
    MuiPickersUtilsProvider,
    KeyboardDatePicker,
} from "@material-ui/pickers";
import DateFnsUtils from "@date-io/date-fns";
import { formatDateObject } from "@cyverse-de/ui-lib/src/util/DateFormatter";

function DatePicker(props) {
    const [selectedDate, setSelectedDate] = React.useState(new Date());
    const handleDateChange = (date) => {
        let formattedDate = formatDateObject(date, "YYYY-MM-DD");
        console.log(formattedDate);
        setSelectedDate(date);
    };

    return (
        <div className={props.className}>
            <MuiPickersUtilsProvider utils={DateFnsUtils}>
                <KeyboardDatePicker
                    disableToolbar
                    variant="inline"
                    format="MM/dd/yyyy"
                    id={props.id}
                    label={props.label}
                    value={selectedDate}
                    onChange={handleDateChange}
                    KeyboardButtonProps={{
                        "aria-label": "change date",
                    }}
                />
            </MuiPickersUtilsProvider>
        </div>
    );
}

export default DatePicker;
