import Button from '@material-ui/core/Button';
import DeleteIcon from '@material-ui/icons/Delete';
import React from 'react';

export default function DeleteBtn(props) {
    return (
        <Button variant='fab'
                color='secondary'
                mini
                {...props}>
            <DeleteIcon />
        </Button>
    )
}