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
        const divStyle = {
            maxWidth: '100%',
            border: '1px solid #d9d9d9',
            backgroundColor: '#DB6619',
            whiteSpace: 'nowrap',
            float: 'left',
            fontSize: '10px',
            padding: '2px',
            cursor: 'pointer',
        };

        const tagStyle = {
            fontWeight: 'bold',
            float: 'left',
            paddingRight: '1px',
            color: 'white',
        };

        const removeStyle = {
            fontWeight: 'bold',
            cursor: 'pointer',
            display: 'none',
            float: 'right',
            fontSize: '12px',
            borderLeft: '1px solid',
            paddingLeft: '1px',
        };

        return (
            <div style={divStyle} onMouseOver={this.handleMouseOver}
                 onMouseOut={this.handleMouseOut}>
                <div style={tagStyle}
                     onClick={this.onTagClick}>{this.props.tag.value}</div>
                <div title='Remove this tag' id={this.props.tag.value} style={removeStyle}
                     onClick={() => this.props.removeTag(this.props.tag)}> X
                </div>
            </div>
        );
    }
}

export default Tag;