import React from 'react';
import ReactDOM from 'react-dom';

import { getDefaultTheme, MuiThemeProvider } from "../lib";

import { EditDataCiteMetadataTest, EditMetadataTest } from "../../stories/metadata/EditMetadata.stories";
import {
    DataCiteMetadataTemplateViewNoValuesTest,
    DataCiteMetadataTemplateViewTest,
    EditDataCiteMetadataTemplateTest,
    EditNestedAttrMetadataTemplateTest,
    MetadataTemplateReadOnlyViewTest,
    MetadataTemplateViewTest,
} from "../../stories/metadata/MetadataTemplate.stories";

it('renders EditMetadataTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <EditMetadataTest/>
        </MuiThemeProvider>,
        div,
    );
    ReactDOM.unmountComponentAtNode(div);
});

it('renders EditDataCiteMetadataTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <EditDataCiteMetadataTest/>
        </MuiThemeProvider>,
        div,
    );
    ReactDOM.unmountComponentAtNode(div);
});

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

it('renders MetadataTemplateViewTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <MetadataTemplateViewTest/>
        </MuiThemeProvider>,
        div,
    );
    ReactDOM.unmountComponentAtNode(div);
});

it('renders MetadataTemplateReadOnlyViewTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <MetadataTemplateReadOnlyViewTest/>
        </MuiThemeProvider>,
        div,
    );
    ReactDOM.unmountComponentAtNode(div);
});

it('renders DataCiteMetadataTemplateViewNoValuesTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <DataCiteMetadataTemplateViewNoValuesTest/>
        </MuiThemeProvider>,
        div,
    );
    ReactDOM.unmountComponentAtNode(div);
});

it('renders DataCiteMetadataTemplateViewTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <DataCiteMetadataTemplateViewTest/>
        </MuiThemeProvider>,
        div,
    );
    ReactDOM.unmountComponentAtNode(div);
});
