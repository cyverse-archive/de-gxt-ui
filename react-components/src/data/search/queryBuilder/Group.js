import build from "../../../util/DebugIDUtil";
import AddBtn from "./AddBtn";
import Condition from "./Condition";
import DeleteBtn from "./DeleteBtn";
import ids from "../ids";

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
        helperProps,
        helperProps: {
            parentId,
            classes
        }
    } = props;

    let baseId = build(parentId, fields.name);

    return (
        <Fragment>
            <AddBtn onClick={() => fields.push({})}
                    id={build(baseId, ids.addConditionBtn)}/>
            {!root && <DeleteBtn onClick={onRemove}
                                 id={build(parentId, ids.deleteConditionBtn)}/>}
            {fields.map((field, index) => (
                <div key={index} className={classes.condition}>
                    <Condition field={field}
                               parentId={baseId}
                               onRemove={() => fields.remove(index)}
                               helperProps={helperProps}/>
                </div>
            ))}
        </Fragment>
    )
}

export default Group;