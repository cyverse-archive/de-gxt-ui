import React from 'react';

import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';

import ToolDetailsTest from './apps/details/ToolDetails.stories'
import CategoryTreeTest from './apps/details/CategoryTree.stories'
import CopyTextAreaTest from './util/CopyTextArea.stories'
import BasicDetailsTest from './data/details/BasicDetails.stories'
import InfoTypeSelectionListTest from './data/details/InfoTypeSelectionList.stories'
import TagTest from './data/details/TagTest.stories'
import TagPanelTest from './data/details/TagPanelTest.stories'
import AppStatsTest from './apps/admin/AppStats.stories'

storiesOf('apps/details', module).add('CategoryTree', () => <CategoryTreeTest logger={action('hierarchy')} />);
storiesOf('apps/details', module).add('ToolDetails', () => <ToolDetailsTest/>);
storiesOf('util', module).add('CopyTextArea', () => <CopyTextAreaTest/>);

storiesOf('AppStats', module).add('with test stats', () => <AppStatsTest/>);

storiesOf('BasicDetails', module).add('with test diskresource details', () => <BasicDetailsTest logger={action('size')}/>);
storiesOf('InfoTypeSelectionList',module).add('with test diskresource details', () =>  <InfoTypeSelectionListTest />);
storiesOf('Tag', module).add('with test diskresource details', () => <TagTest logger={action('tag')}/>);
storiesOf('TagPanel', module).add('witht test diskresource details', () => <TagPanelTest logger={action('tag')}/>);
