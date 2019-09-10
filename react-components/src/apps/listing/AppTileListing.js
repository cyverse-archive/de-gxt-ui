import React from "react";

import { Grid, makeStyles, Paper, Typography } from "@material-ui/core";
import { AppTile, palette, withI18N } from "@cyverse-de/ui-lib";
import { injectIntl } from "react-intl";
import intlData from "../../apps/messages";
import FilterSortToolbar from "./FilterSortToolbar";
import viewType from "../model/viewType";
import VerticalMenuItems from "./VerticalMenuItems";

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
    const { apps, intl, baseDebugID, heading, typeFilter, presenter } = props;
    const classes = useStyles();

    const onTypeFilterChange = () => {};
    const onSortChange = () => {};
    const onAppInfoClick = () => {};
    const onCommentsClick = () => {};
    const onFavoriteClick = () => {};
    console.log("apps=> " + apps);
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
                onSortChange={onSortChange}
                view={viewType.TABLE}
            />

            <Grid container className={classes.root}>
                {(apps && apps.length > 0) && apps.map((app) => {
                    const external = app.app_type !== "DE";
                    const menuItems = () => (
                        <VerticalMenuItems
                            isExternal={external}
                            isFavorite={app.is_favorite}
                            handleAppInfoClick={onAppInfoClick}
                            handleCommentsClick={onCommentsClick}
                            handleFavoriteClick={onFavoriteClick}
                        />
                    );
                    return (
                        <Grid key={app.id} item>
                            <AppTile
                                uuid={app.id}
                                name={app.name}
                                creator={app.integrator_name}
                                rating={app.rating}
                                type={app.app_type}
                                isPublic={app.is_public}
                                isBeta={app.beta}
                                isDisabled={app.disabled}
                                isExternal={external}
                                MenuItems={menuItems}
                            />
                        </Grid>
                    );
                })}
            </Grid>
        </Paper>
    );
}

export default withI18N(injectIntl(AppTileListing), intlData);
