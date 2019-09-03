import React from "react";

import { Grid, makeStyles, Paper, Typography } from "@material-ui/core";
import { AppTile, palette, withI18N } from "@cyverse-de/ui-lib";
import { injectIntl } from "react-intl";
import intlData from "../../apps/messages";
import FilterSortToolbar from "./FilterSortToolbar";
import viewType from "../model/viewType";

/**
 *
 * @author sriram
 *
 * A component that will show a listing of type App with AppTile display
 */

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
    },
    header: {
        height: 25,
        backgroundColor: palette.lightGray,
        paddingLeft: 5,
    },
    toolbar: {
        backgroundColor: palette.lightGray,
        borderBottom: "solid 2px",
        borderColor: palette.gray,
        height: 50,
    },
    dropDown: {
        height: 40,
        flexDirection: "unset",
        padding: 5,
    },
    dropDownLabel: {
        padding: 5,
        fontSize: 10,
    },
}));

function AppTileListing(props) {
    const onTypeFilterChange = () => {};

    const { apps, intl, baseDebugID, heading, typeFilter } = props;
    const classes = useStyles();
    return (
        <Paper>
            <div className={classes.header}>
                <Typography>{heading}</Typography>
            </div>
            <FilterSortToolbar
                classes={classes}
                baseDebugId={baseDebugID}
                typeFilter={typeFilter}
                onTypeFilterChange={onTypeFilterChange}
                view={viewType.TABLE}
            />
            <Grid container className={classes.root} spacing={1}>
                {apps.map((app) => (
                    <Grid key={app.id} item sm>
                        <AppTile
                            uuid={app.id}
                            name={app.name}
                            creator={app.integrator_name}
                            rating={app.rating}
                            type={app.app_type}
                            isPublic={app.is_public}
                            isBeta={app.beta}
                            isDisabled={app.disabled}
                            isExternal={app.app_type !== "DE"}
                        />
                    </Grid>
                ))}
            </Grid>
        </Paper>
    );
}

export default withI18N(injectIntl(AppTileListing), intlData);
