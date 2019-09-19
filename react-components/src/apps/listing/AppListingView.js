import React, { useState } from "react";
import AppTileListing from "./AppTileListing";
import view from "../model/viewType";
import WithToolbarAppGridListing from "./WithToolbarAppGridListing";

/**
 *
 * @author sriram
 *
 * A component that will show a listing of type App with AppTile or AppGrid display
 */

export default function AppListingView(props) {
    const { viewType, presenter } = props;
    const [selectedApps, setSelectedApps] = useState([]);

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

    const resetAppSelection = () => {
        setSelectedApps([]);
    };

    const onTypeFilterChange = (filter) => {
        presenter.onTypeFilterChanged(filter);
    };

    const isSelected = (id) => {
        console.log("isSelected=>" + id);
        if (selectedApps && selectedApps.length > 0) {
            return selectedApps.filter((app) => app.id === id).length > 0;
        }
        return false;
    };

    const onSortChange = () => {};
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

    if (viewType === view.TILE) {
        return (
            <AppTileListing
                {...props}
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
            />
        );
    } else if (viewType === view.TABLE) {
        return (
            <WithToolbarAppGridListing
                onTypeFilterChange={onTypeFilterChange}
                handleAppSelection={handleAppSelection}
                selectedApps={selectedApps}
                resetAppSelection={resetAppSelection}
                isSelected={isSelected}
                onAppNameClick={onAppNameClick}
                onAppInfoClick={onAppInfoClick}
                {...props}
            />
        );
    } else {
        return "UnSupported View!";
    }
}
