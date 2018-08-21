import React, { Component } from 'react';
import IconButton from "@material-ui/core/IconButton";
import MoreVertIcon from "../../../node_modules/@material-ui/icons/MoreVert";
import Menu from "@material-ui/core/Menu";
import AnalysesMenu from "./AnalysesMenu";

const ITEM_HEIGHT = 48;

class DotMenu extends Component {

    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
    }

    handleDotMenuClick = event => {
        this.setState({anchorEl: event.currentTarget});
    };

    handleDotMenuClose = () => {
        this.setState({anchorEl: null});
    };

    render() {
        const {anchorEl} = this.state;
        const open = Boolean(anchorEl);

        return (
            <div>
                <IconButton
                    aria-label="More"
                    aria-owns={open ? 'long-menu' : null}
                    aria-haspopup="true"
                    onClick={this.handleDotMenuClick}
                >
                    <MoreVertIcon/>
                </IconButton>
                <Menu
                    id="dot-menu"
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
                    <AnalysesMenu {...this.props}
                    />
                </Menu>
            </div>
        );
    }
}

export default DotMenu;