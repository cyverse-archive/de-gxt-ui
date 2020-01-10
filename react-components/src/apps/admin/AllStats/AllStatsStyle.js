import { palette } from "@cyverse-de/ui-lib";
import { createStyles } from "@material-ui/core";

const styles = (theme) =>
    createStyles({
        datePicker: {
            float: "left",
            marginRight: 40,
        },

        datePickers: {
            borderBottom: "solid 60px",
            borderBottomColor: palette.lightGray,
            borderTop: "solid 10px",
            borderTopColor: palette.lightGray,
            borderLeft: "solid 50px",
            borderLeftColor: palette.lightGray,
        },

        applyFilterBtn: {
            borderColor: palette.lightGray,
            float: "left",
            marginTop: 10,
        },

        appSelectBar: {
            width: "100%",
            float: "left",
            backgroundColor: palette.lightGray,
        },

        appSelectText: {
            borderLeft: "solid 50px",
            borderLeftColor: palette.lightGray,
            borderTop: "solid 32px",
            borderTopColor: palette.lightGray,
            borderBottom: "solid 20px",
            borderBottomColor: palette.lightGray,
            backgroundColor: palette.lightGray,
            float: "left",
        },

        appCountSelect: {
            width: "150px",
            height: "75px",
            borderBottom: "solid 8px",
            borderBottomColor: palette.lightGray,
            borderLeft: "solid 10px",
            borderLeftColor: palette.lightGray,
            backgroundColor: palette.lightGray,
            borderRight: "solid 100px",
            float: "left",
        },
    });

export default styles;
