/**
 *  @author sriram
 *
 **/

import React, { Component } from "react";

import AnalysesMenuItems from "./AnalysesMenuItems";

import MoreVertIcon from "@material-ui/icons/MoreVert";

import { Menu, IconButton } from "@material-ui/core";

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

        return (
            <div>
                <IconButton
                    id={this.props.baseDebugId}
                    aria-label="More"
                    aria-owns={open ? "long-menu" : null}
                    aria-haspopup="true"
                    onClick={this.handleDotMenuClick}
                >
                    <MoreVertIcon />
                </IconButton>
                <Menu
                    id={this.props.baseDebugId + ".menu"}
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
                    <AnalysesMenuItems
                        handleClose={this.handleDotMenuClose}
                        {...this.props}
                    />
                </Menu>
            </div>
        );
    }
}

export default DotMenu;
