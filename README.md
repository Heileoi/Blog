# 熙磊个人博客系统

一个基于 Spring Boot 3 + Vue 3 + uni-app 的企业级个人博客系统，支持Web端和微信小程序。

## 技术栈

### 后端
- **框架**: Spring Boot 3.2.5 + Java 21
- **持久层**: MyBatis-Plus 3.5.6
- **数据库**: MySQL 8.x + Redis
- **安全**: Spring Security + JWT
- **文件存储**: MinIO
- **API文档**: Knife4j (Swagger增强)
- **工具**: Hutool, Lombok

### Web前端
- **框架**: Vue 3 + Vite
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **图表**: ECharts

### 微信小程序
- **框架**: uni-app (Vue 3)
- **状态管理**: Pinia
- **支持平台**: 微信小程序、H5

## 项目结构

```
├── blog-backend/                # 后端项目
│   ├── src/main/java/com/xilei/blog/
│   │   ├── common/              # 通用类（Result, PageResult）
│   │   ├── config/              # 配置类（Security, Redis, MinIO, CORS等）
│   │   ├── controller/
│   │   │   ├── admin/           # 后台管理接口
│   │   │   └── front/           # 前台展示接口
│   │   ├── dto/                 # 数据传输对象
│   │   ├── entity/              # 实体类
│   │   ├── enums/               # 枚举类
│   │   ├── exception/           # 异常处理
│   │   ├── mapper/              # MyBatis Mapper接口
│   │   ├── security/            # 安全认证模块
│   │   ├── service/             # 业务逻辑层
│   │   │   └── impl/            # Service实现类
│   │   └── utils/               # 工具类
│   └── src/main/resources/
│       ├── application.yml      # 应用配置
│       └── schema.sql           # 数据库初始化脚本
│
├── blog-frontend/               # Web前端项目
│   ├── src/
│   │   ├── api/                 # API接口封装
│   │   ├── assets/              # 静态资源
│   │   ├── layout/              # 布局组件
│   │   ├── router/              # 路由配置
│   │   ├── stores/              # Pinia状态管理
│   │   ├── utils/               # 工具函数
│   │   └── views/
│   │       ├── admin/           # 后台管理页面
│   │       └── front/           # 前台展示页面
│   └── package.json
│
├── blog-mini/                   # 微信小程序项目 (uni-app)
│   ├── api/                     # API接口封装
│   ├── pages/                   # 页面
│   │   ├── index/               # 首页
│   │   ├── article/             # 文章详情
│   │   ├── category/            # 分类
│   │   ├── tag/                 # 标签
│   │   ├── archive/             # 归档
│   │   ├── about/               # 关于
│   │   ├── search/              # 搜索
│   │   └── friend-link/         # 友链
│   ├── store/                   # Pinia状态管理
│   ├── utils/                   # 工具函数
│   ├── static/                  # 静态资源
│   ├── manifest.json            # 应用配置
│   ├── pages.json               # 页面路由配置
│   └── package.json
│
└── README.md
```

## 功能模块

### 前台功能 (Web + 小程序)
- 首页文章列表（分页、搜索）
- 文章详情（Markdown渲染、点赞、评论）
- 分类浏览
- 标签云
- 文章归档
- 友情链接
- 关于页面

### 后台功能 (仅Web端)
- 仪表盘（数据统计、趋势图表）
- 文章管理（发布、编辑、删除、置顶、状态管理）
- 分类管理（增删改查）
- 标签管理（增删改查）
- 评论管理（审核、删除）
- 文件管理（上传、删除）
- 用户管理（状态、角色管理）
- 公告管理
- 友链管理（审核）
- 个人信息管理

## 快速开始

### 环境要求
- JDK 21+
- MySQL 8.x
- Redis
- Node.js 18+
- MinIO（可选，用于文件存储）
- 微信开发者工具（小程序开发）

### 后端启动
1. 创建数据库并执行 `blog-backend/src/main/resources/schema.sql`
2. 修改 `application.yml` 中的数据库和Redis配置
3. 运行 `BlogApplication.java`

### Web前端启动
```bash
cd blog-frontend
npm install
npm run dev
```

### 微信小程序启动
```bash
cd blog-mini
npm install
npm run dev:mp-weixin
```
然后用微信开发者工具打开 `dist/dev/mp-weixin` 目录。

### 访问地址
- Web前台: http://localhost:5173
- Web后台: http://localhost:5173/admin
- API文档: http://localhost:8080/api/doc.html

### 默认账号
- 用户名: admin
- 密码: admin123

## 作者

**王熙磊** - 资深Java开发工程师
