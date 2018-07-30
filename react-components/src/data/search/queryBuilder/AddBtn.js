import AddIcon from '@material-ui/icons/Add';
import Button from '@material-ui/core/Button';
import React from 'react';

/**
 * A button used by QueryBuilder.  Clicking it adds a Condition
 */
export default function AddBtn(props) {
    return (
        <Button variant='fab'
                color='primary'
                mini
                {...props}>
            <AddIcon />
        </Button>
    )
}