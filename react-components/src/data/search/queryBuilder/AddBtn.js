import AddIcon from '@material-ui/icons/Add';
import Button from '@material-ui/core/Button';
import React from 'react';

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