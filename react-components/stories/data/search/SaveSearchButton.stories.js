import React from 'react';
import {SaveSearchButton} from '../../../src/data/search';

class SaveSearchButtonTest extends React.Component {

    render() {
        const handleSave = this.props.logger || ((selection) => {
            console.log(selection);
        });

        const onChange = (event) => {
            return event.target.value;
        };

        const value = 'OLDNAME';

        return (
            <SaveSearchButton handleSave={handleSave}
                              value={value}
                              onChange={onChange}/>
        )
    }
}

export default SaveSearchButtonTest;