import React, { useState } from "react";

import { Grid, makeStyles, Paper, Typography } from "@material-ui/core";
import { AppTile, palette, withI18N, LoadingMask } from "@cyverse-de/ui-lib";
import { injectIntl } from "react-intl";
import intlData from "../../apps/messages";
import FilterSortToolbar from "./FilterSortToolbar";
import VerticalMenuItems from "./VerticalMenuItems";
import AppListingHeader from "./AppListingHeader";

/**
 *
 * @author sriram
 *
 * A component that will show a listing of type App with AppTile display
 */

const useStyles = makeStyles((theme) => ({
    container: {
        width: "100%",
        height: "100%",
        marginTop: 0,
        overflow: "auto",
        backgroundColor: palette.white,
    },
}));

function AppTileListing(props) {
    const {
        apps,
        intl,
        baseDebugID,
        heading,
        typeFilter,
        presenter,
        viewType,
        loading,
        sortField,
        onTypeFilterChange,
        onSortChange,
        onAppInfoClick,
        onCommentsClick,
        onFavoriteClick,
        disableTypeFilter,
        handleAppSelection,
        isSelected,
        onAppNameClick,
    } = props;
    const classes = useStyles();

    const [anchorEl, setAnchorEl] = useState("");

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    return (
        <div className={classes.container}>
            <LoadingMask loading={loading}>
                <AppListingHeader heading={heading} />
                <FilterSortToolbar
                    baseDebugId={baseDebugID}
                    typeFilter={typeFilter}
                    onTypeFilterChange={onTypeFilterChange}
                    onSortChange={onSortChange}
                    viewType={viewType}
                    sortField={sortField}
                    disableTypeFilter={disableTypeFilter}
                />
                <Grid container>
                    {apps &&
                        apps.length > 0 &&
                        apps.map((app) => {
                            const external = app.app_type !== "DE";
                            const menuItems = () => (
                                <VerticalMenuItems
                                    isExternal={external}
                                    isFavorite={app.is_favorite}
                                    handleAppInfoClick={() =>
                                        onAppInfoClick(app)
                                    }
                                    handleCommentsClick={() =>
                                        onCommentsClick(app)
                                    }
                                    handleFavoriteClick={() =>
                                        onFavoriteClick(app)
                                    }
                                    handleMenuClose={handleMenuClose}
                                />
                            );
                            return (
                                <Grid key={app.id} item>
                                    <AppTile
                                        uuid={app.id}
                                        name={app.name}
                                        creator={app.integrator_name}
                                        rating={app.rating}
                                        type={app.app_type}
                                        isPublic={app.is_public}
                                        isBeta={app.beta}
                                        isDisabled={app.disabled}
                                        isExternal={external}
                                        MenuItems={menuItems}
                                        selected={isSelected(app.id)}
                                        onAppSelected={() =>
                                            handleAppSelection(app)
                                        }
                                        onAppNameClicked={() =>
                                            onAppNameClick(app)
                                        }
                                        handleMenuClose={() => {
                                            setAnchorEl(null);
                                        }}
                                        handleMenuClick={(event) => {
                                            setAnchorEl(event.currentTarget);
                                        }}
                                        anchorEl={anchorEl}
                                    />
                                </Grid>
                            );
                        })}
                </Grid>
            </LoadingMask>
        </div>
    );
}

export default withI18N(injectIntl(AppTileListing), intlData);
