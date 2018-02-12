import React from 'react';

import { storiesOf } from '@storybook/react';

import ToolDetailsTest from './apps/details/ToolDetails.stories'

storiesOf('ToolDetails', module).add('with test app', () => <ToolDetailsTest/>);
