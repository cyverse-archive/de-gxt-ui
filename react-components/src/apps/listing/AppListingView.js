import React from "react";
import AppTileListing from "./AppTileListing";
import AppGridListing from "./AppGridListing";
import view from "../model/viewType";

/**
 *
 * @author sriram
 *
 * A component that will show a listing of type App with AppTile or AppGrid display
 */

export default function AppListingView(props) {
    const { viewType } = props;

    if (viewType === view.TILE) {
        return <AppTileListing {...props} />;
    } else if (viewType === view.TABLE) {
        return <AppGridListing {...props} />;
    } else {
        return "UnSupported View!";
    }
}
