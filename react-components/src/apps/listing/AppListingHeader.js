import React from "react";
import { makeStyles, Typography } from "@material-ui/core";
import { LoadingMask, palette } from "@cyverse-de/ui-lib";

/**
 *
 * @author sriram
 *
 * A component that will show the App Listing header
 */

const useStyles = makeStyles((theme) => ({
    header: {
        height: 25,
        backgroundColor: palette.lightGray,
        paddingLeft: 5,
    },
}));

export default function AppListingHeader(props) {
    const { heading } = props;
    const classes = useStyles();
    return (
        <div className={classes.header}>
            <Typography variant="subtitle2">{heading}</Typography>
        </div>
    );
}
