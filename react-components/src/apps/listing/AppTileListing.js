import React, { useState } from "react";

import { Grid, makeStyles, Typography } from "@material-ui/core";
import {
    AppTile,
    getMessage,
    LoadingMask,
    palette,
    stableSort,
    withI18N,
} from "@cyverse-de/ui-lib";
import intlData from "../../apps/messages";
import FilterSortToolbar from "./FilterSortToolbar";
import AppListingHeader from "./AppListingHeader";
import appType from "../../appType";

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
    const [menuOpen, setMenuOpen] = useState(false);
    const {
        apps,
        parentId,
        heading,
        typeFilter,
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
        getAppsSorting,
        onRatingDeleteClick,
        onRatingClick,
        searchRegexPattern,
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
                    sortField={sortField}
                    disableTypeFilter={disableTypeFilter}
                />
                {(!apps || apps.length === 0) && (
                    <div>
                        {" "}
                        <Typography component="p">
                            {getMessage("noApps")}
                        </Typography>
                    </div>
                )}
                <Grid container>
                    {apps &&
                        apps.length > 0 &&
                        stableSort(apps, getAppsSorting("asc", sortField)).map(
                            (app) => {
                                const external = app.app_type !== appType.de;
                                return (
                                    <Grid key={app.id} item>
                                        <AppTile
                                            baseDebugId={parentId}
                                            uuid={app.id}
                                            name={app.name}
                                            creator={app.integrator_name}
                                            rating={app.rating}
                                            type={app.app_type}
                                            isPublic={app.is_public}
                                            isBeta={app.beta}
                                            isDisabled={app.disabled}
                                            isExternal={external}
                                            selected={isSelected(app.id)}
                                            onAppSelected={() => {
                                                handleAppSelection(app);
                                            }}
                                            onAppNameClick={() =>
                                                onAppNameClick(app)
                                            }
                                            onRatingChange={(event, score) => {
                                                onRatingClick(
                                                    event,
                                                    app,
                                                    score
                                                );
                                            }}
                                            onDeleteRatingClick={() =>
                                                onRatingDeleteClick(app)
                                            }
                                            onAppInfoClick={() =>
                                                onAppInfoClick(app)
                                            }
                                            onCommentsClick={() =>
                                                onCommentsClick(app)
                                            }
                                            onFavoriteClick={() =>
                                                onFavoriteClick(app)
                                            }
                                            isFavorite={app.is_favorite}
                                            searchRegexPattern={
                                                searchRegexPattern
                                            }
                                        />
                                    </Grid>
                                );
                            }
                        )}
                </Grid>
            </LoadingMask>
        </div>
    );
}

export default withI18N(AppTileListing, intlData);
