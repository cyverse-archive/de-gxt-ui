/**
 * @author sriram
 */
import React, {Component} from "react";
import IconButton from "material-ui-next/IconButton";
import Menu, {MenuItem} from "material-ui-next/Menu";
import Typography from "material-ui-next/Typography";
import {FormattedMessage, IntlProvider} from "react-intl";
import Divider from 'material-ui-next/Divider';

class UserMenu extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
        this.onUserMenuClick = this.onUserMenuClick.bind(this);
        this.onMenuItemSelect = this.onMenuItemSelect.bind(this);
    }

    handleClose = () => {
        this.setState({anchorEl: null});
    };

    onUserMenuClick(event) {
        console.log(event.currentTarget);
        this.setState({anchorEl: document.getElementById(this.props.anchor)});
    }

    onMenuItemSelect(event) {
        console.log("selected item =>" + event.currentTarget);
    }

    render() {
        const appearance = this.props.appearance;
        const {anchorEl} = this.state;
        const link = {
            color: '#0971AB',
            cursor: 'pointer',
            textAlign: 'left',
            fontSize: '11px',
        }
        return (
            <IntlProvider locale='en' defaultLocale='en' messages={this.props.messages}>
            <span>
                    <img style={this.props.iconStyle}
                         src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAMAAABHPGVmAAAACXBIWXMAAC4jAAAuIwF4pT92AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAP9QTFRF////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////fb62zwAAAFR0Uk5TAAEFBgcICRgZGhwdHiQnKS0vMDFER0pLUlRZWmltenuEhYqLjJOUlZeYnKGkpqqrrra3vsDCxcfJyszP0NbX2Nna29zh5O/w8fLz9PX29/j5/P3+yIHx3AAAAeRJREFUeAHt1OtS2lAQwPGDEoSgwRjveEERSBS8KCixBJKoBBUEuvv+z9JADs6Z6rQy/ZKt5/cC/5ndnWXSf0iSJEkyDMvyvFHIdU1T10lGUqnU6SmiCMCyFIVOhCcajQZ+ol5XeIdIRJjUeOyExmPkTNOkEzEMnDk7W1pioXS6Wp2tH3SdSsSy3hNMUKthpFKhEvE8vvJwVAJVBcCJdptKZDTCCcdhv/F9nBgOqUXoR1yXLz6dZoJsFoDXqURMEyPVKhOcn2OkVJIRga4DYKRWU9VoH7MEAKyuEonwP8wB+CEA5MrlMmN0IopSr9fxEzc3yWSSTIRnFMsCQAFAucwLlCL8liuVdnsYcpxSiV8uyYgUI8vL+Xz+5OT6utMJgp+hIOh0rq6Kxd3dbDZLI2IYxaLn4R+57vHx2lp8I4nEzo5t45c1m9vbiUT8Irnc3R1+9Prq+z9CDw/9Pn50e6tp8YpsbLy8iD+92SwUCpubmQwTZDJbW0dHtm0D4Lvn5/X1bxbJ5QYD5B4fDw5U9W//4PDw6Qm5fl/T4hK5vMSpbndvb2GBfcni4v5+r4dTFxdxiQQBhnq9lRU2F02LzqXbjUsEp+7v2dxaLZwiGSEQGQxac3t7i2PkX8iIjEiSJEkSMb8AcEQvAJi3fQMAAAAASUVORK5CYII="
                         alt="User Menu" onClick={this.onUserMenuClick}>
                    </img>
                <Menu id='userMenu' anchorEl={anchorEl}
                      open={Boolean(anchorEl)}
                      onClose={this.handleClose}>
                    <MenuItem onClick={this.onMenuItemSelect}> <Typography
                        style={link}><FormattedMessage id="preferences"/></Typography> </MenuItem>
                    <MenuItem onClick={this.onMenuItemSelect}> <Typography
                        style={link}><FormattedMessage id="collaboration"/></Typography></MenuItem>
                      <Divider />
                    <MenuItem onClick={this.onMenuItemSelect}> <Typography
                        style={link}><FormattedMessage id="documentation"/></Typography></MenuItem>
                    <MenuItem onClick={this.onMenuItemSelect}> <Typography
                        style={link}><FormattedMessage id="introduction"/></Typography></MenuItem>
                    <MenuItem onClick={this.onMenuItemSelect}> <Typography
                        style={link}><FormattedMessage id="about"/></Typography></MenuItem>
                     <Divider />
                    <MenuItem onClick={this.onMenuItemSelect}> <Typography
                        style={link}><FormattedMessage id="logout"/></Typography> </MenuItem>
                </Menu>
            </span>
            </IntlProvider>
        );
    }
}

export default UserMenu;