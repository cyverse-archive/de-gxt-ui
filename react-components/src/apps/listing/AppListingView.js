import React, { useEffect, useState } from "react";
import AppTileListing from "./AppTileListing";
import view from "../model/viewType";
import WithToolbarAppGridListing from "./WithToolbarAppGridListing";
import { build } from "@cyverse-de/ui-lib";
import ids from "./ids";

/**
 *
 * @author sriram
 *
 * A component that will show a listing of type App with AppTile or AppGrid display
 */

export default function AppListingView(props) {
    const { viewType, presenter, apps, baseId } = props;
    const [selectedApps, setSelectedApps] = useState([]);

    //reset selection when categories /  Hierarchy / search results change
    useEffect(() => {
        resetAppSelection();
    }, [apps]);

    const handleAppSelection = (app) => {
        const selectedIndex = selectedApps.indexOf(app);
        let newSelected = [];

        if (selectedIndex === -1) {
            newSelected = newSelected.concat(selectedApps, app);
        } else if (selectedIndex === 0) {
            newSelected = newSelected.concat(selectedApps.slice(1));
        } else if (selectedIndex === selectedApps.length - 1) {
            newSelected = newSelected.concat(selectedApps.slice(0, -1));
        } else if (selectedIndex > 0) {
            newSelected = newSelected.concat(
                selectedApps.slice(0, selectedIndex),
                selectedApps.slice(selectedIndex + 1)
            );
        }
        setSelectedApps(newSelected);
        presenter.onAppSelectionChanged(newSelected);
    };

    const handleSelectAll = (checked) => {
        checked ? setSelectedApps(apps) : resetAppSelection();
    };

    const resetAppSelection = () => {
        setSelectedApps([]);
        presenter.onAppSelectionChanged([]);
    };

    const onTypeFilterChange = (filter) => {
        presenter.onTypeFilterChanged(filter);
    };

    const isSelected = (id) => {
        if (selectedApps && selectedApps.length > 0) {
            return selectedApps.filter((app) => app.id === id).length > 0;
        }
        return false;
    };

    const onSortChange = (sortField, sortDir) => {
        presenter.onRequestSort(sortField, sortDir);
    };

    const onAppInfoClick = (app) => {
        presenter.onAppInfoSelected(app);
    };

    const onCommentsClick = (app) => {
        presenter.onAppCommentSelected(app);
    };

    const onFavoriteClick = (app) => {
        presenter.onAppFavoriteSelected(app);
    };

    const onAppNameClick = (app) => {
        presenter.onAppNameSelected(app);
    };

    const onRatingClick = (event, app, score) => {
        presenter.onAppRatingSelected(app, score);
    };

    const onRatingDeleteClick = (app) => {
        presenter.onAppRatingDeselected(app);
    };

    if (viewType === view.TILE) {
        return (
            <AppTileListing
                {...props}
                parentId={build(baseId, ids.LISTING.TILE_VIEW)}
                onTypeFilterChange={onTypeFilterChange}
                onSortChange={onSortChange}
                onAppInfoClick={onAppInfoClick}
                onCommentsClick={onCommentsClick}
                onFavoriteClick={onFavoriteClick}
                handleAppSelection={handleAppSelection}
                selectedApps={selectedApps}
                resetAppSelection={resetAppSelection}
                isSelected={isSelected}
                onAppNameClick={onAppNameClick}
                onRatingClick={onRatingClick}
                onRatingDeleteClick={onRatingDeleteClick}
            />
        );
    } else if (viewType === view.TABLE) {
        return (
            <WithToolbarAppGridListing
                parentId={build(baseId, ids.LISTING.GRID_VIEW)}
                onTypeFilterChange={onTypeFilterChange}
                handleAppSelection={handleAppSelection}
                selectedApps={selectedApps}
                onSortChange={onSortChange}
                resetAppSelection={resetAppSelection}
                isSelected={isSelected}
                onAppNameClick={onAppNameClick}
                onAppInfoClick={onAppInfoClick}
                onRatingClick={onRatingClick}
                onRatingDeleteClick={onRatingDeleteClick}
                handleSelectAll={handleSelectAll}
                {...props}
            />
        );
    } else {
        return "UnSupported View!";
    }
}
