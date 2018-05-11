import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {getCyVerseTheme} from "../../../src/lib";
import {SaveSearchButton} from '../../../src/diskResource/search';

class SaveSearchButtonTest extends React.Component {

    render() {
        const handleSave = this.props.logger || ((selection) => {
            console.log(selection);
        });

        return (

            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                <SaveSearchButton handleSave={handleSave}
                                  originalName='OLDNAME'/>
            </MuiThemeProvider>
        )
    }
}

export default SaveSearchButtonTest;