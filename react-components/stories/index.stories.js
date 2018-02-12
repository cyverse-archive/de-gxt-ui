import React from 'react';

import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';

import ToolDetailsTest from './apps/details/ToolDetails.stories'
import CategoryTreeTest from './apps/details/CategoryTree.stories'

storiesOf('ToolDetails', module).add('with test app', () => <ToolDetailsTest/>);
storiesOf('CategoryTree', module).add('with test hierarchy', () => <CategoryTreeTest logger={action('hierarchy')} />);
