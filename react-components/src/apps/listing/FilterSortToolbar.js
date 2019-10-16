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
import PropTypes from "prop-types";

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
        sortDir,
        disableTypeFilter,
    } = props;
    const classes = useStyles();
    return (
        <div className={classes.toolbar}>
            <form autoComplete="off">
                {viewType === view.TILE && (
                    <FormControl
                        className={classes.dropDown}
                        variant="outlined"
                    >
                        <InputLabel className={classes.dropDownLabel}>
                            {getMessage("sort")}
                        </InputLabel>
                        <Select
                            value={sortField}
                            onChange={(e) =>
                                onSortChange(e.target.value, sortDir)
                            }
                            inputProps={{
                                name: "sort",
                                id: build(baseDebugID, ids.SORT),
                            }}
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
                <FormControl variant="outlined" className={classes.dropDown}>
                    <InputLabel className={classes.dropDownLabel}>
                        {getMessage("appType")}
                    </InputLabel>
                    <Select
                        value={typeFilter}
                        onChange={(e) => onTypeFilterChange(e.target.value)}
                        inputProps={{
                            name: "type",
                            id: build(baseDebugID, ids.APP_TYPE),
                        }}
                        style={{ minWidth: 120 }}
                        disabled={disableTypeFilter}
                    >
                        <MenuItem
                            id={build(baseDebugID, ids.LISTING.TYPE, ids.ALL)}
                            value={appType.all}
                        >
                            {appType.all}
                        </MenuItem>
                        <MenuItem
                            id={build(
                                baseDebugID,
                                ids.LISTING.TYPE + ids.AGAVE
                            )}
                            value={appType.agave}
                        >
                            {appType.agave}
                        </MenuItem>
                        <MenuItem
                            id={build(baseDebugID, ids.LISTING.TYPE + ids.DE)}
                            value={appType.de}
                        >
                            {appType.de}
                        </MenuItem>
                        <MenuItem
                            id={build(
                                baseDebugID,
                                ids.LISTING.TYPE + ids.INTERACTIVE
                            )}
                            value={appType.interactive}
                        >
                            {appType.interactive}
                        </MenuItem>
                        <MenuItem
                            id={build(baseDebugID, ids.LISTING.TYPE + ids.OSG)}
                            value={appType.osg}
                        >
                            {appType.osg}
                        </MenuItem>
                    </Select>
                </FormControl>
            </form>
        </div>
    );
}

FilterSortToolbar.propTypes = {
    baseDebugID: PropTypes.string.isRequired,
    typeFilter: PropTypes.string,
    onTypeFilterChange: PropTypes.func.isRequired,
    viewType: PropTypes.string.isRequired,
    onSortChange: PropTypes.func.isRequired,
    sortField: PropTypes.string.isRequired,
    disableTypeFilter: PropTypes.bool,
};

export default withI18N(injectIntl(FilterSortToolbar), intlData);
