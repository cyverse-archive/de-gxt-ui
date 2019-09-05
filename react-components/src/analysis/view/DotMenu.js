/**
 *  @author sriram
 *
 **/

import React, { Component } from "react";

import AnalysesMenuItems from "./AnalysesMenuItems";

import Menu from "@material-ui/core/Menu";

import IconButton from "@material-ui/core/IconButton";
import MoreVertIcon from "@material-ui/icons/MoreVert";

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
