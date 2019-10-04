import React, { Component } from "react";
import { MoreVert } from "@material-ui/icons";
import EditCommentsMenuItems from "./EditCommentsMenuItems.js";
import { injectIntl } from "react-intl";
import messages from "./messages";
import { Menu, IconButton } from "@material-ui/core";
import { formatMessage, withI18N } from "@cyverse-de/ui-lib";
const ITEM_HEIGHT = 48;

class DotMenu extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
    }

    handleDotMenuClick = (event) => {
        this.setState({ anchorEl: event.currentTarget });
    };

    handleDotMenuClose = () => {
        this.setState({ anchorEl: null });
    };

    render() {
        const { anchorEl } = this.state;
        const open = Boolean(anchorEl);
        const { intl } = this.props;
        return (
            <div>
                <IconButton
                    aria-Label={formatMessage(intl, "moreDD")}
                    aria-owns={open ? "long-menu" : null}
                    aria-haspopup="true"
                    onClick={this.handleDotMenuClick}
                    classes={{ root: this.props.className }}
                >
                    <MoreVert />
                </IconButton>
                <Menu
                    anchorEl={anchorEl}
                    open={open}
                    onClose={this.handleDotMenuClose}
                    PaperProps={{
                        style: {
                            maxHeight: ITEM_HEIGHT * 4.5,
                            width: 200,
                        },
                    }}
                >
                    <EditCommentsMenuItems
                        handleClose={this.handleDotMenuClose}
                        {...this.props}
                    />
                </Menu>
            </div>
        );
    }
}

export default withI18N(injectIntl(DotMenu), messages);
