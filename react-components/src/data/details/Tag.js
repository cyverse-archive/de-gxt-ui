/**
 * @author sriram
 */
import React, {Component} from "react";

class Tag extends Component {
    constructor(props) {
        super(props);
    }

    handleMouseOver = () => {
        document.getElementById(this.props.tag.value).style.display = 'block';
    }

    handleMouseOut = () => {
        document.getElementById(this.props.tag.value).style.display = 'none';
    }

    onTagClick = () => {
        this.props.presenter.onTagSelection(this.props.tag.id, this.props.tag.value)
    }

    render() {
        return (
            <div class={this.props.appearance.css().tagDivStyle()} onMouseOver={this.handleMouseOver}
                 onMouseOut={this.handleMouseOut}>
                <div class={this.props.appearance.css().tagStyle()}
                     onClick={this.onTagClick}>{this.props.tag.value}</div>
                <div title={this.props.appearance.removeTag()} id={this.props.tag.value}
                     class={this.props.appearance.css().tagRemoveStyle()}
                     onClick={() => this.props.removeTag(this.props.tag)}> X
                </div>
            </div>
        );
    }
}

export default Tag;