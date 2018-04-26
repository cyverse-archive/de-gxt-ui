import React from 'react';

import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';

import ToolDetailsTest from './apps/details/ToolDetails.stories'
import CategoryTreeTest from './apps/details/CategoryTree.stories'
import CopyTextAreaTest from './util/CopyTextArea.stories'
import BasicDetailsTest from './data/details/BasicDetails.stories'
import InfoTypeSelectionListTest from './data/details/InfoTypeSelectionList.stories'
import TagTest from './data/details/Tag.stories'
import TagPanelTest from './data/details/TagPanel.stories'
import AppStatsTest from './apps/admin/AppStats.stories'

storiesOf('apps/details', module).add('CategoryTree', () => <CategoryTreeTest logger={action('hierarchy')} />);
storiesOf('apps/details', module).add('ToolDetails', () => <ToolDetailsTest/>);
storiesOf('util', module).add('CopyTextArea', () => <CopyTextAreaTest/>);

storiesOf('apps/admin/AppStats', module).add('with test stats', () => <AppStatsTest/>);

storiesOf('data/BasicDetails', module).add('with test diskresource details', () => <BasicDetailsTest logger={action('size')}/>);
storiesOf('data/InfoTypeSelectionList',module).add('with test diskresource details', () =>  <InfoTypeSelectionListTest />);
storiesOf('data/Tag', module).add('with test diskresource details', () => <TagTest logger={action('tag')}/>);
storiesOf('data/TagPanel', module).add('witht test diskresource details', () => <TagPanelTest logger={action('tag')}/>);
