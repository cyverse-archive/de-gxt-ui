import EditTagDialog from '../../../src/data/search/EditTagDialog';

import React, { Component } from 'react';

class EditTagDialogTest extends Component {
    render() {
        const handleSave = this.props.saveTagLogger || ((selection) => {
            console.log(selection);
        });

        const handleClose = this.props.closeDlgLogger || ((selection) => {
            console.log(selection);
        });

        const tag = {
            id: '1',
            value: 'Apples',
            description: 'old description'
        };

        return (
            <EditTagDialog open={true}
                           tag={tag}
                           handleSave={handleSave}
                           handleClose={handleClose}/>
        )
    }
}

export default EditTagDialogTest;