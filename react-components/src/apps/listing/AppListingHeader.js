import React from "react";
import { makeStyles, Typography } from "@material-ui/core";
import { LoadingMask, palette } from "@cyverse-de/ui-lib";

/**
 *
 * @author sriram
 *
 * A component that will show a App Listing header
 */

const useStyles = makeStyles((theme) => ({
    header: {
        height: 25,
        backgroundColor: palette.gray,
        paddingLeft: 5,
        position: "sticky",
        top: 0,
    },
}));

export default function AppListingHeader(props) {
    const { heading } = props;
    const classes = useStyles();
    return (
        <div className={classes.header}>
            <Typography>{heading}</Typography>
        </div>
    );
}
