/**
 @author sriram
 */
import React, {Component} from "react";
import Tag from "../../../src/data/details/Tag";

class TagTest extends Component {

    render() {
        const logger = this.props.logger || ((tag) => {
                console.log(tag);
            });

        const tag = {id: 1, value: 'testag'};

        return (
                <Tag tag={tag} removeTag={logger} onClick={logger}/>
        );
    }
}

export default TagTest;
