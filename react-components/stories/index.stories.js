import React from 'react';

import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';

import CopyTextAreaTest from './apps/details/CopyTextArea.stories'
import ToolDetailsTest from './apps/details/ToolDetails.stories'
import CategoryTreeTest from './apps/details/CategoryTree.stories'

storiesOf('CopyTextArea', module).add('with test text', () => <CopyTextAreaTest/>);
storiesOf('CategoryTree', module).add('with test hierarchy', () => <CategoryTreeTest logger={action('hierarchy')} />);
storiesOf('ToolDetails', module).add('with test app', () => <ToolDetailsTest/>);
