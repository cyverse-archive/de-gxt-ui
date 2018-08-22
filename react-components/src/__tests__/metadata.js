import React from 'react';
import ReactDOM from 'react-dom';

import { getDefaultTheme, MuiThemeProvider } from "../lib";

import {
    EditDataCiteMetadataTemplateTest,
    EditNestedAttrMetadataTemplateTest,
} from "../../stories/metadata/MetadataTemplate.stories";

it('renders EditNestedAttrMetadataTemplateTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <EditNestedAttrMetadataTemplateTest/>
        </MuiThemeProvider>,
        div,
    );
    ReactDOM.unmountComponentAtNode(div);
});

it('renders EditDataCiteMetadataTemplateTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <EditDataCiteMetadataTemplateTest/>
        </MuiThemeProvider>,
        div,
    );
    ReactDOM.unmountComponentAtNode(div);
});
