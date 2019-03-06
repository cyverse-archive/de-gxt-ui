import {configure, addDecorator} from "@storybook/react";
import {withConsole} from "@storybook/addon-console";
import {getDefaultTheme, MuiThemeProvider} from "../src/lib";
import { withKnobs } from '@storybook/addon-knobs';


//redirect console error / logs / warns to action logger
addDecorator((storyFn, context) => withConsole()(storyFn)(context));


//wrap with mui theme
const themeDecorator = (storyFn) => (
    <MuiThemeProvider theme={getDefaultTheme()}>
        {storyFn()}
    </MuiThemeProvider>

);
addDecorator(themeDecorator);

addDecorator(withKnobs);


// automatically import all files ending in *.stories.js
const req = require.context('../stories', true, /.stories.js$/);
function loadStories() {
  req.keys().forEach((filename) => req(filename));
}

configure(loadStories, module);
