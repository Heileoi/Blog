# Heileoi个人博客系统

一个基于 Spring Boot 3 + Vue 3 + uni-app 的企业级个人博客系统，支持Web端和微信小程序。

## 技术栈

### 后端
- **框架**: Spring Boot 3.2.5 + Java 17
- **持久层**: MyBatis-Plus 3.5.6
- **数据库**: MySQL 8.x + Redis
- **安全**: Spring Security + JWT + 接口限流 + XSS防护
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
│   │   ├── common/              # 通用类（Result, PageResult, 操作日志注解/切面）
│   │   ├── config/              # 配置类（Security, Redis, MinIO, CORS等）
│   │   ├── controller/
│   │   │   ├── admin/           # 后台管理接口
│   │   │   └── front/           # 前台展示接口
│   │   ├── dto/                 # 数据传输对象
│   │   ├── entity/              # 实体类
│   │   ├── enums/               # 枚举类
│   │   ├── exception/           # 异常处理
│   │   ├── mapper/              # MyBatis Mapper接口
│   │   ├── security/            # 安全认证（JWT过滤器、限流、安全头）
│   │   ├── service/             # 业务逻辑层
│   │   │   └── impl/            # Service实现类（含Redis缓存）
│   │   └── utils/               # 工具类（JWT、安全、IP、XSS清理）
│   ├── src/main/resources/
│   │   ├── application.yml      # 应用配置（环境变量外部化）
│   │   ├── application-prod.yml # 生产环境配置
│   │   └── schema.sql           # 数据库初始化脚本
│   ├── .env.example             # 环境变量模板
│   └── Dockerfile
│
├── blog-frontend/               # Web前端项目
│   ├── src/
│   │   ├── api/                 # API接口封装
│   │   ├── assets/              # 静态资源
│   │   ├── layout/              # 布局组件
│   │   ├── router/              # 路由配置（含权限守卫）
│   │   ├── stores/              # Pinia状态管理
│   │   ├── utils/               # 工具函数（请求拦截、401/429处理）
│   │   └── views/
│   │       ├── admin/           # 后台管理页面
│   │       └── front/           # 前台展示页面（含骨架屏加载）
│   ├── Dockerfile
│   └── nginx.conf
│
├── blog-mini/                   # 微信小程序项目 (uni-app)
│   ├── api/                     # API接口封装
│   ├── pages/                   # 页面
│   ├── store/                   # Pinia状态管理
│   ├── utils/                   # 工具函数
│   ├── static/                  # 静态资源
│   └── package.json
│
├── docker-compose.yml           # Docker一键部署
└── README.md
```

## 核心功能

### 前台功能 (Web + 小程序)
- 文章列表（分页、搜索、分类/标签筛选）
- 文章详情（Markdown渲染、点赞、评论）
- 分类浏览、标签云
- 文章归档
- 友情链接、公告
- 关于页面

### 后台功能 (仅Web端)
- 仪表盘（数据统计、趋势图表）
- 文章管理（发布、编辑、删除、置顶、推荐、状态管理）
- 分类管理（树形结构）
- 标签管理
- 评论管理（审核、删除）
- 文件管理（MinIO上传）
- 用户管理（状态、角色管理）
- 公告管理、友链管理
- 个人信息管理
- **操作日志审计**（AOP自动记录）

### 安全特性
- JWT 无状态认证 + BCrypt 密码加密
- **接口限流**: 登录 10次/分, 注册 5次/时, 评论 10次/分
- **XSS防护**: 评论内容HTML标签过滤
- **安全响应头**: X-Frame-Options, HSTS, X-Content-Type-Options, Referrer-Policy
- **CORS可配置**: 通过环境变量指定允许的来源域名
- **环境变量外部化**: 数据库密码、JWT密钥、MinIO密钥等全部走环境变量
- **操作日志**: AOP自动记录管理员的所有写操作

### 性能优化
- **Redis多级缓存**: 热门文章(10min)、分类(1h)、标签(1h)、公告(10min)、友链(1h)
- **缓存自动失效**: 写操作触发CacheEvict，保证数据一致性
- **Redis防重复**: 文章浏览计数(5min去重)、点赞去重(永久)
- **Nginx优化**: gzip压缩、静态资源缓存30天

## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.x
- Redis
- Node.js 18+
- MinIO（可选，用于文件存储）

### 本地开发

```bash
# 1. 初始化数据库
mysql -u root -p < blog-backend/src/main/resources/schema.sql

# 2. 启动后端
cd blog-backend
mvn spring-boot:run

# 3. 启动前端
cd blog-frontend
npm install
npm run dev
```

### Docker 部署

```bash
# 复制环境变量模板
cp blog-backend/.env.example blog-backend/.env
# 修改 .env 中的密钥和密码

# 一键启动（MySQL + Redis + MinIO + 后端 + 前端）
docker-compose up -d
```

### 微信小程序启动
```bash
cd blog-mini
npm install
npm run dev:mp-weixin
```
然后用微信开发者工具打开 `dist/dev/mp-weixin` 目录。

## 访问地址

| 服务 | 地址 |
|------|------|
| Web前台 | http://localhost:5173 |
| Web后台 | http://localhost:5173/admin |
| API文档 | http://localhost:8080/api/doc.html |
| MinIO控制台 | http://localhost:9001 |

### 默认账号
- 用户名: `admin`
- 密码: `admin123`

## 环境变量

| 变量 | 说明 | 默认值 |
|------|------|--------|
| `DB_URL` | 数据库连接 | `jdbc:mysql://localhost:3306/blog_db` |
| `DB_USERNAME` | 数据库用户名 | `root` |
| `DB_PASSWORD` | 数据库密码 | `123456` |
| `REDIS_HOST` | Redis地址 | `localhost` |
| `JWT_SECRET` | JWT密钥(生产必改) | 内置默认值 |
| `MINIO_ENDPOINT` | MinIO地址 | `http://localhost:9000` |
| `MINIO_ACCESS_KEY` | MinIO用户 | `minioadmin` |
| `MINIO_SECRET_KEY` | MinIO密码 | `minioadmin` |
| `CORS_ALLOWED_ORIGINS` | CORS来源(逗号分隔) | `http://localhost:5173` |

## 作者

**wangxilei**
