import React, { useState } from "react";
import { injectIntl } from "react-intl";
import intlData from "../../apps/messages";
import { getMessage, palette, withI18N } from "@cyverse-de/ui-lib";
import MenuItem from "@material-ui/core/MenuItem";
import { makeStyles } from "@material-ui/core";
import InfoIcon from "@material-ui/icons/InfoOutlined";
import UnFavoriteIcon from "@material-ui/icons/FavoriteBorderOutlined";
import FavoriteIcon from "@material-ui/icons/Favorite";
import CommentsIcon from "@material-ui/icons/CommentOutlined";

/**
 *
 * @author sriram
 *
 * Menu items for Apps Tile and table view
 *
 */
const useStyles = makeStyles((theme) => ({
    menuItem: {
        padding: 5,
    },
    toolbarItemColor: {
        color: palette.darkBlue,
    },
}));

function VerticalMenuItems(props) {
    const {
        isExternal,
        isFavorite,
        handleAppInfoClick,
        handleFavoriteClick,
        handleCommentsClick,
        handleMenuClose,
    } = props;
    const [open, setOpen] = useState(false);
    const classes = useStyles();
    return (
        <React.Fragment>
            <MenuItem
                disabled={false}
                className={classes.menuItem}
                data-disabled={false}
                onClick={() => {
                    handleAppInfoClick();
                }}
            >
                <InfoIcon className={classes.toolbarItemColor} />
                {getMessage("appInfo")}
            </MenuItem>
            {!isFavorite && !isExternal && (
                <MenuItem
                    disabled={false}
                    className={classes.menuItem}
                    data-disabled={false}
                    onClick={() => {
                        handleFavoriteClick();
                    }}
                >
                    <FavoriteIcon className={classes.toolbarItemColor} />
                    {getMessage("addToFavorites")}
                </MenuItem>
            )}
            {isFavorite && !isExternal && (
                <MenuItem
                    disabled={false}
                    className={classes.menuItem}
                    data-disabled={false}
                    onClick={() => {
                        handleFavoriteClick();
                        handleMenuClose();
                    }}
                >
                    <UnFavoriteIcon className={classes.toolbarItemColor} />
                    {getMessage("removeFromFavorites")}
                </MenuItem>
            )}
            {!isExternal && (
                <MenuItem
                    disabled={false}
                    className={classes.menuItem}
                    data-disabled={false}
                    onClick={() => {
                        handleCommentsClick();
                        handleMenuClose();
                    }}
                >
                    <CommentsIcon className={classes.toolbarItemColor} />
                    {getMessage("comments")}
                </MenuItem>
            )}
            {isExternal && (
                <React.Fragment>
                    <MenuItem
                        disabled={false}
                        className={classes.menuItem}
                        data-disabled={false}
                    >
                        {getMessage("favoriteNotSupported")}
                    </MenuItem>
                    <MenuItem
                        disabled={false}
                        className={classes.menuItem}
                        data-disabled={false}
                    >
                        {getMessage("commentsNotSupported")}
                    </MenuItem>
                </React.Fragment>
            )}
        </React.Fragment>
    );
}

export default withI18N(injectIntl(VerticalMenuItems), intlData);
