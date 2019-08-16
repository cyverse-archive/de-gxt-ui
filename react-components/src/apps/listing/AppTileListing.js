import React from "react";
import PropTypes from "prop-types";

import {
    Grid,
    makeStyles,
    Paper,
    Typography,
    withStyles,
} from "@material-ui/core";
import {
    AppTile,
    build,
    getMessage,
    palette,
    withI18N,
} from "@cyverse-de/ui-lib";
import FormControl from "@material-ui/core/FormControl";
import ids from "./ids";
import InputLabel from "@material-ui/core/InputLabel";
import Select from "@material-ui/core/Select";
import OutlinedInput from "@material-ui/core/OutlinedInput";
import MenuItem from "@material-ui/core/MenuItem";
import appType from "../../appType";
import { injectIntl } from "react-intl";
import intlData from "../../apps/messages";

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

const sort = {
    name: "Name",
    integrator: "Integrator",
    rating: "Rating",
};
function AppTileListing(props) {
    const {
        apps,
        intl,
        baseDebugID,
        heading,
        typeFilter,
        onTypeFilterChange,
    } = props;
    const classes = useStyles();
    return (
        <Paper>
            <div className={classes.header}>
                <Typography>{heading}</Typography>
            </div>
            <div className={classes.toolbar}>
                <form autoComplete="off">
                    <FormControl
                        id={build(baseDebugID, ids.SORT)}
                        className={classes.dropDown}
                    >
                        <InputLabel className={classes.dropDownLabel}>
                            {getMessage("sort")}
                        </InputLabel>
                        <Select
                            value={props.viewFilter}
                            onChange={(e) =>
                                props.onViewFilterChange(e.target.value)
                            }
                            input={<OutlinedInput labelWidth={0} name="sort" />}
                            style={{ minWidth: 120 }}
                        >
                            <MenuItem
                                id={build(
                                    baseDebugID,
                                    ids.SORT + ids.FIELD.NAME
                                )}
                                value={sort.name}
                            >
                                {sort.name}
                            </MenuItem>
                            <MenuItem
                                id={build(
                                    baseDebugID,
                                    ids.SORT + ids.FIELD.INTEGRATOR
                                )}
                                value={sort.integrator}
                            >
                                {sort.integrator}
                            </MenuItem>
                            <MenuItem
                                id={build(
                                    baseDebugID,
                                    ids.SORT + ids.FIELD.RATING
                                )}
                                value={sort.rating}
                            >
                                {sort.rating}
                            </MenuItem>
                        </Select>
                    </FormControl>
                    <FormControl
                        id={build(baseDebugID, ids.APP_TYPE)}
                        className={classes.dropDown}
                    >
                        <InputLabel className={classes.dropDownLabel}>
                            {getMessage("type")}
                        </InputLabel>
                        <Select
                            value={typeFilter}
                            onChange={(e) => onTypeFilterChange(e.target.value)}
                            input={<OutlinedInput labelWidth={0} name="type" />}
                            style={{ minWidth: 120 }}
                        >
                            <MenuItem
                                id={build(
                                    baseDebugID,
                                    ids.LISTING.TYPE + ids.ALL
                                )}
                                value={"All"}
                            >
                                {appType.all}
                            </MenuItem>
                            <MenuItem
                                id={build(
                                    baseDebugID,
                                    ids.LISTING.TYPE + ids.AGAVE
                                )}
                                value={"Agave"}
                            >
                                {appType.agave}
                            </MenuItem>
                            <MenuItem
                                id={build(
                                    baseDebugID,
                                    ids.LISTING.TYPE + ids.DE
                                )}
                                value={"DE"}
                            >
                                {appType.de}
                            </MenuItem>
                            <MenuItem
                                id={build(
                                    baseDebugID,
                                    ids.LISTING.TYPE + ids.INTERACTIVE
                                )}
                                value={"Interactive"}
                            >
                                {appType.interactive}
                            </MenuItem>
                            <MenuItem
                                id={build(
                                    baseDebugID,
                                    ids.LISTING.TYPE + ids.OSG
                                )}
                                value={"OSG"}
                            >
                                {appType.osg}
                            </MenuItem>
                        </Select>
                    </FormControl>
                </form>
            </div>
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
