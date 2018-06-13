/**
 * @author sriram
 */
import React, {Component} from "react";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import DEHyperlink from "../../../src/util/hyperlink/DEHyperLink";
import styles from "../style";
import injectSheet from "react-jss";
import ids from "../ids";
import intlData from "../messages";
import withI18N, {getMessage} from "../../util/I18NWrapper";
import build from "../../util/DebugIDUtil";
import tourStrings from "../NewUserTourStrings";

class Help extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
        this.onMenuItemSelect = this.onMenuItemSelect.bind(this);
        this.helpBtn = React.createRef();
    }

    componentDidMount() {
        this.helpBtn.current.setAttribute("data-intro", tourStrings.introHelp);
        this.helpBtn.current.setAttribute("data-position", "left");
        this.helpBtn.current.setAttribute("data-step", "6");
    }

    handleClick = event => {
        console.log(document.getElementById(this.props.anchor));
        this.setState({anchorEl: document.getElementById(this.props.anchor)});
    };

    handleClose = () => {
        this.setState({anchorEl: null});
    };

    onMenuItemSelect(event) {
        let id = event.currentTarget.id;
        let presenter = this.props.presenter;
        switch (id) {
            case build(ids.DESKTOP, ids.FAQS_LINK):
                presenter.onFaqSelect();
                break;
            case build(ids.DESKTOP, ids.FORUMS_LINK):
                presenter.onForumsBtnSelect();
                break;
            case build(ids.DESKTOP, ids.FEEDBACK_LINK):
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
                          src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAADMElEQVRIiY2WT2hUVxjFf+fxkCBiBhlEZJgJopAGqtSV7g2GErqQVlcqjFJKkQZEKa24ECluAq6klOIgiLtqIZQgiqAgRsRuuohZSMhAFiIyzIiUIsHTxbvvzsvkTe2FYc67797vz/nOd+8T/zF6rUYdmMJMgieQtmOD9ApYBB4Y7glWRpvtUhsqMQqw0+YC8lGhqgEw2rgcoIP5DXEZWB10lJRsmLJZAL4VqtqAQQi7NMhtiK/BC8D04Mt1DnrX66dt/y5cVx618jQzjI3J/tdjati3u636N0WbymipA3xu6zZipJQIeA90gDXDZkFlCAPvDV8J5kab7XyBasDPwiNZkBu4eA4cxnxi2CPYZzhlu0Mhm4A3AdeAGkDSa9XBvmhUB2V8Szhm7o7hMLBo8Qj7BVCtNNs3JI4Z/gllij/sGnCp22qQ2IwhfRkJE/mywL9WhTtATbBX0hhwKFTloWBFEpLCHuUBHhHsSkBTwLaC3eAlFycToJ+ASxAXfQCoNNtrwDuH2LNdMY8KMJ0Cky6YxTkIshSp4cc4b5D4C6DXalRt6tFteN/HnkwRE8KZ/yifzFrIOnuK7/QMeNptNRLgPKKqKGQiVZkTjSeC7eQ9WkJRgZaMGnEe+Bs4Y/usICmjKMRTTbCTPt8OigvPjnzmc3cMT8BXZF8VpADqL1uHMSSINzk3UshEChQJXMDwJzAFOouURN4kyjGdFLQI7A5hgoTsWGiUpyuwZyR9ANLYWBLDsGEpAe4XJBmaK8eF+ayEI8AW/ucQfpCA72J1P0YR5g+LTw2fgR8PpyXit4b5BHiJmOtTBLJRoWrCSPql0myvVprtZVs3B86fMjyPtSSAbqsxJnvBsKPfTeRNlXfFrOH7oJxfsU8Mq4HhtaSDo832chq4WkHMgG7JpI7nCrGzDd8BB4BNwP5C5zGA1zAzwHJmuzC6rcY5wRXsNKooirpwDgxXzprhB8FsfnWuuzAEs+DjSK83qGjIfVkYb4CTReMbMgDoXW+A2AVcBqYxW50fUy7b4begefBF0MvBS3/I7Ri/LsaNvxCaBMaBirNm7Aov2dxHmsMsVU6Vf7b8C7hYjiWl9m09AAAAAElFTkSuQmCC"
                          alt="Help"
                          onClick={this.handleClick}
                          ref={this.helpBtn}></img>
                     <Menu id='helpMenu' anchorEl={anchorEl}
                           open={Boolean(anchorEl)}
                           onClose={this.handleClose}>
                         <MenuItem id={build(ids.DESKTOP, ids.FAQS_LINK)} onClick={this.onMenuItemSelect}>
                             <DEHyperlink text={getMessage("faqLink")}/>
                         </MenuItem>
                         <MenuItem id={build(ids.DESKTOP, ids.FORUMS_LINK)}
                                   onClick={this.onMenuItemSelect}>
                             <DEHyperlink text={getMessage("forumsLink")}/>
                         </MenuItem>
                         <MenuItem id={build(ids.DESKTOP, ids.FEEDBACK_LINK)}
                                   onClick={this.onMenuItemSelect}>
                             <DEHyperlink text={getMessage("feedbackLink")}/>
                         </MenuItem>
                     </Menu>
                </span>
        );
    }
}

export default injectSheet(styles)(withI18N(Help, intlData));