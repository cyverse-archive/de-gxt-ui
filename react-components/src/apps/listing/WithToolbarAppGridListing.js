import React from "react";
import AppListingHeader from "./AppListingHeader";
import FilterSortToolbar from "./FilterSortToolbar";
import { AppGridListing } from "../listing";
import { makeStyles } from "@material-ui/core";
import { palette, LoadingMask } from "@cyverse-de/ui-lib";
import PropTypes from "prop-types";

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
        parentId,
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
                    baseDebugId={parentId}
                    typeFilter={typeFilter}
                    onTypeFilterChange={onTypeFilterChange}
                    onSortChange={onSortChange}
                    viewType={viewType}
                    disableTypeFilter={disableTypeFilter}
                />
                <AppGridListing
                    viewType={viewType}
                    enableMenu={true}
                    {...props}
                />
            </LoadingMask>
        </div>
    );
}

WithToolbarAppGridListing.propTypes = {
    parentId: PropTypes.string.isRequired,
    apps: PropTypes.array.isRequired,
    selectedApps: PropTypes.array.isRequired,
    handleAppSelection: PropTypes.func.isRequired,
    onAppNameClick: PropTypes.func,
    onAppInfoClick: PropTypes.func,
    onCommentsClick: PropTypes.func,
    onFavoriteClick: PropTypes.func,
    getAppsSorting: PropTypes.func,
    onRatingDeleteClick: PropTypes.func,
    onRatingClick: PropTypes.func,
    searchText: PropTypes.string,
    heading: PropTypes.string.isRequired,
    typeFilter: PropTypes.string,
    viewType: PropTypes.string,
    loading: PropTypes.bool.isRequired,
    sortField: PropTypes.string.isRequired,
    onTypeFilterChange: PropTypes.func.isRequired,
};
