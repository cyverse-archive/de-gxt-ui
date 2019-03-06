// you can use this file to add your custom webpack plugins, loaders and anything you like.
// This is just the basic way to add additional webpack configurations.
// For more information refer the docs: https://storybook.js.org/configurations/custom-webpack-config

// IMPORTANT
// When you add this file, we won't add the default configurations which is similar
// to "React Create App". This only has babel loader to load JavaScript.
const eslintFormatter = require('react-dev-utils/eslintFormatter');
const path = require('path');



module.exports = {
  plugins: [
      
  ],
  module: {
    rules: [
        // First, run the linter.
        // It's important to do this before Babel processes the JS.
        {
            test: /\.(js|jsx)$/,
            enforce: 'pre',
            use: [
                {
                    options: {
                        formatter: eslintFormatter,
                        eslintPath: require.resolve('eslint'),

                    },
                    loader: require.resolve('eslint-loader'),
                },
            ],
            include: path.resolve(__dirname, '../'),
        },
    ],
  },
};
