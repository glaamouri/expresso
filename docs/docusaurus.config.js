// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'Expresso',
  tagline: 'Java Expression Evaluator',
  favicon: 'img/favicon.ico',
  url: 'https://expresso.ghassen.work',
  baseUrl: '/',
  organizationName: 'glaamouri',
  projectName: 'expresso',

  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          routeBasePath: '/',
        },
        blog: false,
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      image: 'img/logo.svg',
      metadata: [
        { name: 'description', content: 'Expresso is a lightweight Java expression evaluator library.' },
        { name: 'keywords', content: 'Java, Expression Evaluator, Expresso, Library' },
        { name: 'og:title', content: 'Expresso - Java Expression Evaluator' },
        { name: 'og:description', content: 'Expresso is a lightweight Java expression evaluator library.' },
        { name: 'og:image', content: 'https://expresso.ghassen.work/img/logo.svg' },
        { name: 'og:url', content: 'https://expresso.ghassen.work' },
      ],
    
      navbar: {
        title: 'Expresso',
        logo: {
          alt: 'Expresso Logo',
          src: 'img/logo.svg',
        },
        items: [
          {
            type: 'docSidebar',
            sidebarId: 'docSidebar',
            position: 'left',
            label: 'Documentation',
          },
          {
            href: 'https://github.com/glaamouri/expresso',
            label: 'GitHub',
            position: 'right',
          },
        ],
      },
      footer: {
        style: 'dark',
        links: [
          {
            title: 'Docs',
            items: [
              {
                label: 'Overview',
                to: '/',
              },
              {
                label: 'Getting Started',
                to: '/getting-started',
              },
              {
                label: 'Syntax',
                to: '/syntax',
              },
            ],
          },
          {
            title: 'Features',
            items: [
              {
                label: 'Null-Safety Operators',
                to: '/null-safety',
              },
              {
                label: 'Functions',
                to: '/functions',
              },
              {
                label: 'Error Handling',
                to: '/error-handling',
              },
            ],
          },
          {
            title: 'More',
            items: [
              {
                label: 'Examples',
                to: '/examples',
              },
              {
                label: 'GitHub',
                href: 'https://github.com/glaamouri/expresso',
              },
            ],
          },
        ],
        copyright: `Copyright © ${new Date().getFullYear()} Expresso. Built with Docusaurus.`,
      },
      prism: {
        theme: require('prism-react-renderer/themes/github'),
        darkTheme: require('prism-react-renderer/themes/dracula'),
        additionalLanguages: ['java'],
      },
      colorMode: {
        defaultMode: 'dark',
        disableSwitch: false,
        respectPrefersColorScheme: true,
      },
    }),
};
module.exports = config; 