import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {getCyVerseTheme} from "../../../src/lib";
import SaveSearchDialog from "../../../src/diskResource/search/SaveSearchDialog";

class SaveSearchDialogTest extends React.Component {

    render() {
        const appearance = {
            saveBtn: () => 'Save',
            cancelBtn: () => 'Cancel',
            saveSearchBtnText: () => 'Save Search',
            filterName: () => 'Filter Name',
            requiredField: () => 'This field is required',
            saveSearchTitle: () => 'Save Filter'
        };

        const handleSave = this.props.logger || ((selection) => {
            console.log(selection);
        });

        return (

            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                <SaveSearchDialog appearance={appearance}
                                  handleSave={handleSave}
                                  originalName='OLDNAME'/>
            </MuiThemeProvider>
        )
    }
}

export default SaveSearchDialogTest;