import React from "react";
import { build, getMessage, palette, withI18N } from "@cyverse-de/ui-lib";
import ids from "./ids";
import view from "../model/viewType";
import appType from "../../appType";
import { injectIntl } from "react-intl";
import intlData from "../../apps/messages";
import {
    FormControl,
    InputLabel,
    makeStyles,
    MenuItem,
    OutlinedInput,
    Select,
} from "@material-ui/core";
import AppFields from "./AppFields";

/**
 * @author sriram
 *
 * A toolbar for sorting and filter apps
 */

const useStyles = makeStyles((theme) => ({
    toolbar: {
        backgroundColor: palette.lightGray,
        borderBottom: "solid 1px",
        borderColor: palette.gray,
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
function FilterSortToolbar(props) {
    const {
        baseDebugID,
        typeFilter,
        onTypeFilterChange,
        viewType,
        onSortChange,
        sortField,
        disableTypeFilter,
    } = props;
    const classes = useStyles();
    return (
        <div className={classes.toolbar}>
            <form autoComplete="off">
                {viewType === view.TILE && (
                    <FormControl
                        id={build(baseDebugID, ids.SORT)}
                        className={classes.dropDown}
                    >
                        <InputLabel className={classes.dropDownLabel}>
                            {getMessage("sort")}
                        </InputLabel>
                        <Select
                            value={sortField}
                            onChange={(e) => onSortChange(e.target.value)}
                            input={<OutlinedInput labelWidth={0} name="sort" />}
                            style={{ minWidth: 120 }}
                        >
                            <MenuItem
                                id={build(
                                    baseDebugID,
                                    ids.SORT + ids.FIELD.NAME
                                )}
                                value={AppFields.NAME.key}
                            >
                                {AppFields.NAME.fieldName}
                            </MenuItem>
                            <MenuItem
                                id={build(
                                    baseDebugID,
                                    ids.SORT + ids.FIELD.INTEGRATOR
                                )}
                                value={AppFields.INTEGRATOR.key}
                            >
                                {AppFields.INTEGRATOR.fieldName}
                            </MenuItem>
                            <MenuItem
                                id={build(
                                    baseDebugID,
                                    ids.SORT + ids.FIELD.RATING
                                )}
                                value={AppFields.RATING.key}
                            >
                                {AppFields.RATING.fieldName}
                            </MenuItem>
                        </Select>
                    </FormControl>
                )}
                <FormControl
                    id={build(baseDebugID, ids.APP_TYPE)}
                    className={classes.dropDown}
                >
                    <InputLabel className={classes.dropDownLabel}>
                        {getMessage("appType")}
                    </InputLabel>
                    <Select
                        value={typeFilter}
                        onChange={(e) => onTypeFilterChange(e.target.value)}
                        input={<OutlinedInput labelWidth={0} name="type" />}
                        style={{ minWidth: 120 }}
                        disabled={disableTypeFilter}
                    >
                        <MenuItem
                            id={build(baseDebugID, ids.LISTING.TYPE + ids.ALL)}
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
                            id={build(baseDebugID, ids.LISTING.TYPE + ids.DE)}
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
                            id={build(baseDebugID, ids.LISTING.TYPE + ids.OSG)}
                            value={"OSG"}
                        >
                            {appType.osg}
                        </MenuItem>
                    </Select>
                </FormControl>
            </form>
        </div>
    );
}

export default withI18N(injectIntl(FilterSortToolbar), intlData);
