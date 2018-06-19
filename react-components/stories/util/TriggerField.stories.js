import TriggerField from '../../src/util/TriggerField';

import Button from '@material-ui/core/Button';
import React, { Component } from 'react';

class TriggerFieldTest extends Component {
    render() {
        const handleSearch = this.props.logger('Search') || ((selection) => {
            console.log(selection);
        });

        return (
            <TriggerField handleSearch={handleSearch}>
                <Button>Test Success!</Button>
            </TriggerField>
        )
    }
}

export default TriggerFieldTest;