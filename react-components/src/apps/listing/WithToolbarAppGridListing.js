import React from "react";
import AppListingHeader from "./AppListingHeader";
import FilterSortToolbar from "./FilterSortToolbar";
import { AppGridListing } from "../listing";
import { makeStyles } from "@material-ui/core";
import { palette, LoadingMask } from "@cyverse-de/ui-lib";

const useStyles = makeStyles((theme) => ({
    container: {
        width: "100%",
        height: "100%",
        marginTop: 0,
        overflow: "auto",
        backgroundColor: palette.white,
    },
}));

export default function WithToolbarAppGridListing(props) {
    const {
        heading,
        baseDebugId,
        typeFilter,
        onTypeFilterChange,
        onSortChange,
        viewType,
        disableTypeFilter,
        loading,
    } = props;
    const classes = useStyles();
    return (
        <div className={classes.container}>
            <LoadingMask loading={loading}>
                <AppListingHeader heading={heading} />
                <FilterSortToolbar
                    baseDebugId={baseDebugId}
                    typeFilter={typeFilter}
                    onTypeFilterChange={onTypeFilterChange}
                    onSortChange={onSortChange}
                    viewType={viewType}
                    disableTypeFilter={disableTypeFilter}
                />
                <AppGridListing
                    baseDebugID={baseDebugId}
                    viewType={viewType}
                    enableMenu={true}
                    {...props}
                />
            </LoadingMask>
        </div>
    );
}
