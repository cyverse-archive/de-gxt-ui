/**
 * @author sriram
 */
import React, {Component} from "react";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import Divider from "@material-ui/core/Divider";
import DEHyperlink from "../../../src/util/hyperlink/DEHyperLink";
import styles from "../style";
import injectSheet from "react-jss";
import ids from "../ids";
import withI18N, {getMessage} from "../../util/I18NWrapper";
import intlData from "../messages";
import build from "../../util/DebugIDUtil";

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
        let id = event.currentTarget.id;
        let presenter = this.props.presenter;
        switch (id) {
            case build(ids.DESKTOP, ids.PREFERENCES_LINK):
                presenter.onPreferencesClick();
                break;
            case build(ids.DESKTOP, ids.COLLABORATORS_LINK):
                presenter.onCollaboratorsClick();
                break;

            case build(ids.DESKTOP, ids.USER_MANUAL_LINK):
                presenter.onDocumentationClick();
                break;

            case build(ids.DESKTOP, ids.INTRO_LINK):
                presenter.onIntroClick();
                break;
            case build(ids.DESKTOP, ids.ABOUT_LINK):
                presenter.onAboutClick();
                break;

            case build(ids.DESKTOP, ids.LOGOUT_LINK):
                presenter.doLogout(false);
                break;
            default:
                break; //do nothing
        }
    }

    render() {
        const {anchorEl} = this.state;
        const classes = this.props.classes;
        return (
            <span>
                    <img className={classes.menuIcon}
                         src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAMAAABHPGVmAAAACXBIWXMAAC4jAAAuIwF4pT92AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAP9QTFRF////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////fb62zwAAAFR0Uk5TAAEFBgcICRgZGhwdHiQnKS0vMDFER0pLUlRZWmltenuEhYqLjJOUlZeYnKGkpqqrrra3vsDCxcfJyszP0NbX2Nna29zh5O/w8fLz9PX29/j5/P3+yIHx3AAAAeRJREFUeAHt1OtS2lAQwPGDEoSgwRjveEERSBS8KCixBJKoBBUEuvv+z9JADs6Z6rQy/ZKt5/cC/5ndnWXSf0iSJEkyDMvyvFHIdU1T10lGUqnU6SmiCMCyFIVOhCcajQZ+ol5XeIdIRJjUeOyExmPkTNOkEzEMnDk7W1pioXS6Wp2tH3SdSsSy3hNMUKthpFKhEvE8vvJwVAJVBcCJdptKZDTCCcdhv/F9nBgOqUXoR1yXLz6dZoJsFoDXqURMEyPVKhOcn2OkVJIRga4DYKRWU9VoH7MEAKyuEonwP8wB+CEA5MrlMmN0IopSr9fxEzc3yWSSTIRnFMsCQAFAucwLlCL8liuVdnsYcpxSiV8uyYgUI8vL+Xz+5OT6utMJgp+hIOh0rq6Kxd3dbDZLI2IYxaLn4R+57vHx2lp8I4nEzo5t45c1m9vbiUT8Irnc3R1+9Prq+z9CDw/9Pn50e6tp8YpsbLy8iD+92SwUCpubmQwTZDJbW0dHtm0D4Lvn5/X1bxbJ5QYD5B4fDw5U9W//4PDw6Qm5fl/T4hK5vMSpbndvb2GBfcni4v5+r4dTFxdxiQQBhnq9lRU2F02LzqXbjUsEp+7v2dxaLZwiGSEQGQxac3t7i2PkX8iIjEiSJEkSMb8AcEQvAJi3fQMAAAAASUVORK5CYII="
                         alt="User Menu" onClick={this.onUserMenuClick}>
                    </img>
                <Menu id='userMenu' anchorEl={anchorEl}
                      open={Boolean(anchorEl)}
                      onClose={this.handleClose}>
                    <MenuItem id={build(ids.DESKTOP, ids.PREFERENCES_LINK)}
                              onClick={this.onMenuItemSelect}>
                        <DEHyperlink text={getMessage("preferences")}/>
                    </MenuItem>
                    <MenuItem id={build(ids.DESKTOP, ids.COLLABORATORS_LINK)}
                              onClick={this.onMenuItemSelect}>
                        <DEHyperlink text={getMessage("collaboration")}/>
                    </MenuItem>
                      <Divider />
                    <MenuItem id={build(ids.DESKTOP, ids.USER_MANUAL_LINK)}
                              onClick={this.onMenuItemSelect}>
                        <DEHyperlink text={getMessage("documentation")}/>
                    </MenuItem>
                    <MenuItem id={build(ids.DESKTOP, ids.INTRO_LINK)} onClick={this.onMenuItemSelect}>
                        <DEHyperlink text={getMessage("introduction")}/>
                    </MenuItem>
                    <MenuItem id={build(ids.DESKTOP, ids.ABOUT_LINK)} onClick={this.onMenuItemSelect}>
                        <DEHyperlink text={getMessage("about")}/>
                    </MenuItem>
                     <Divider />
                    <MenuItem id={build(ids.DESKTOP, ids.LOGOUT_LINK)} onClick={this.onMenuItemSelect}>
                        <DEHyperlink text={getMessage("logout")}/>
                    </MenuItem>
                </Menu>
            </span>
        );
    }
}

export default injectSheet(styles)(withI18N(UserMenu, intlData));