import React from 'react';

import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';

import ToolDetailsTest from './apps/details/ToolDetails.stories'
import CategoryTreeTest from './apps/details/CategoryTree.stories'
import CopyTextAreaTest from './util/CopyTextArea.stories'

storiesOf('apps/details', module).add('CategoryTree', () => <CategoryTreeTest logger={action('hierarchy')} />);
storiesOf('apps/details', module).add('ToolDetails', () => <ToolDetailsTest/>);
storiesOf('util', module).add('CopyTextArea', () => <CopyTextAreaTest/>);
