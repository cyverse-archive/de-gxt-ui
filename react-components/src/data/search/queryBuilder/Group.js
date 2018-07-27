import AddBtn from "./AddBtn";
import DeleteBtn from "./DeleteBtn";
import Condition from "./Condition";

import React, {Fragment} from 'react';

function Group(props) {
    let {
        root,
        fields,
        classes,
        onRemove,
        ...custom
    } = props;

    return (
        <Fragment>
            <AddBtn onClick={() => fields.push({})}/>
            {!root && <DeleteBtn onClick={onRemove}/>}
            {fields.map((field, index) => (
                <div key={index}>
                    <Condition field={field}
                               classes={classes}
                               onRemove={() =>fields.remove(index)}
                               {...custom}/>
                </div>
            ))}
        </Fragment>
    )
}

export default Group;