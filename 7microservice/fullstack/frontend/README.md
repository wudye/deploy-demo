# steps:
*   ng new frontend --style=css --routing=true --skip-git
*   cd frontend
*   npm install tailwindcss @tailwindcss/postcss postcss --force
*   Create a .postcssrc.json file
*     {
*     "plugins": {
*     "@tailwindcss/postcss": {}
*     }
*     }
*   Add an @import to ./src/styles.css that imports Tailwind CSS.
*   @import "tailwindcss";
*   npm install angular-auth-oidc-client
*   ng serve
# 创建组件（自动生成 .ts, .html, .css, .spec.ts 4 个文件）
ng generate component shared/header
# 简写
ng g c shared/header

# 创建服务
ng generate service services/auth
ng g s services/auth

# 创建模块
ng generate module admin
ng g m admin

# 创建指令
ng generate directive shared/loading
ng g d shared/loading


命令	简写	说明
ng generate component	ng g c	生成组件
ng generate service	ng g s	生成服务
ng generate module	ng g m	生成模块
ng generate directive	ng g d	生成指令
ng generate pipe	ng g p	生成管道
ng generate interface	ng g i	生成接口


ng generate class config/auth --type=config --skip-tests


# Frontend

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 21.0.4.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Vitest](https://vitest.dev/) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
