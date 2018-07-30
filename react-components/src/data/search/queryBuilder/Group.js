import AddBtn from "./AddBtn";
import DeleteBtn from "./DeleteBtn";
import Condition from "./Condition";

import React, { Fragment } from 'react';

/**
 * A component which allows users to create different groupings in QueryBuilder
 * such as Any and All
 */
function Group(props) {
    let {
        root,
        fields,
        onRemove,
        helperProps
    } = props;

    return (
        <Fragment>
            <AddBtn onClick={() => fields.push({})}/>
            {!root && <DeleteBtn onClick={onRemove}/>}
            {fields.map((field, index) => (
                <div key={index}>
                    <Condition field={field}
                               onRemove={() =>fields.remove(index)}
                               helperProps={helperProps}/>
                </div>
            ))}
        </Fragment>
    )
}

export default Group;