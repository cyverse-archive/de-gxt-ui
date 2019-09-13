import React from "react";
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
    const { viewType } = props;

    if (viewType === view.TILE) {
        return <AppTileListing {...props} />;
    } else if (viewType === view.TABLE) {
        return <WithToolbarAppGridListing {...props} />;
    } else {
        return "UnSupported View!";
    }
}
