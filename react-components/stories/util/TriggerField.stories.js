import TriggerField from '../../src/util/TriggerField';

import Button from '@material-ui/core/Button';
import React, { Component } from 'react';

class TriggerFieldTest extends Component {
    render() {
        const handleSearch = this.props.logger || ((selection) => {
            console.log(selection);
        });

        return (
            <div>
                <label>Type at least 3 characters, then wait a second</label>
                <TriggerField handleSearch={handleSearch}>
                    <Button>Test Success!</Button>
                </TriggerField>
            </div>
        )
    }
}

export default TriggerFieldTest;