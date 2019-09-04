import React from "react";
import { build, getMessage, withI18N } from "@cyverse-de/ui-lib";
import ids from "./ids";
import viewType from "../model/viewType";
import appType from "../../appType";
import { injectIntl } from "react-intl";
import intlData from "../../apps/messages";
import {
    FormControl,
    InputLabel,
    MenuItem,
    OutlinedInput,
    Select,
} from "@material-ui/core";

/**
 * @author sriram
 *
 * A toolbar for sorting and filter apps
 */

const sort = {
    name: "Name",
    integrator: "Integrator",
    rating: "Rating",
};

function FilterSortToolbar(props) {
    const {
        baseDebugID,
        classes,
        typeFilter,
        onTypeFilterChange,
        view,
        onSortChange,
    } = props;

    return (
        <div className={classes.toolbar}>
            <form autoComplete="off">
                {view === viewType.TILE && (
                    <FormControl
                        id={build(baseDebugID, ids.SORT)}
                        className={classes.dropDown}
                    >
                        <InputLabel className={classes.dropDownLabel}>
                            {getMessage("sort")}
                        </InputLabel>
                        <Select
                            value={props.viewFilter}
                            onChange={(e) => props.onSortChange(e.target.value)}
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
                )}
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
