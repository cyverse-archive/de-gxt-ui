import React, {Component} from 'react';
import PropTypes from 'prop-types';
import Chip from 'material-ui/Chip';

class Tag extends Component {

    constructor(props) {
        super(props);

        this.editTag = this.editTag.bind(this);
        this.deleteTag = this.deleteTag.bind(this);
    }

    editTag() {
        this.props.onClick(this.props.tag);
    }

    deleteTag() {
        this.props.onRequestDelete(this.props.tag);
    };

    render() {
        let tag = this.props.tag;
        return (
            <Chip
                onRequestDelete={this.deleteTag}
                onClick={this.editTag}>
                {tag.value}
            </Chip>
        );
    }
}

Tag.propTypes = {
    tag: PropTypes.shape({
        value: PropTypes.string.isRequired,
        description:PropTypes.string
    }),
    onClick: PropTypes.func.isRequired,
    onRequestDelete: PropTypes.func.isRequired
};

export default Tag;