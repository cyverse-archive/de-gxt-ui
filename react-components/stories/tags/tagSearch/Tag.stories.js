import React, {Component} from 'react';
import Tag from '../../../src/tags/tagSearch/Tag';

class TagTest extends Component {
    render() {

        const editLogger = this.props.editLogger || ((selection) => {
            console.log(selection);
        });

        const deleteLogger = this.props.deleteLogger || ((selection) => {
            console.log(selection);
        });

        const tag = {
            value: 'This is a Tag',
            description: 'A description of some sort'
        };

        return (
            <Tag tag={tag}
                 onClick={editLogger}
                 onRequestDelete={deleteLogger}/>
        );
    }
}

export default TagTest;
