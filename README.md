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

---

## 环境准备

在启动项目之前，需要安装以下软件：

| 软件 | 版本要求 | 用途 | 下载地址 |
|------|----------|------|----------|
| **JDK** | 17+ | 运行后端 | [Adoptium](https://adoptium.net/) 或 [Oracle](https://www.oracle.com/java/technologies/downloads/) |
| **Maven** | 3.8+ | 构建后端 | [Maven官网](https://maven.apache.org/download.cgi) |
| **MySQL** | 8.x | 数据库 | [MySQL官网](https://dev.mysql.com/downloads/mysql/) |
| **Redis** | 6.x+ | 缓存 | [Redis Windows](https://github.com/tporadowski/redis/releases) 或通过WSL安装 |
| **Node.js** | 18+ | 运行前端 | [Node.js官网](https://nodejs.org/) |
| **MinIO** | 最新版 | 文件存储(可选) | [MinIO官网](https://min.io/download) |

### 推荐开发工具

| 工具 | 用途 | 下载地址 |
|------|------|----------|
| **IntelliJ IDEA** | 后端开发 | [JetBrains](https://www.jetbrains.com/idea/) (社区版免费) |
| **VS Code** | 前端开发 | [VS Code](https://code.visualstudio.com/) |
| **Navicat** / **DBeaver** | 数据库管理 | [Navicat](https://www.navicat.com/) / [DBeaver](https://dbeaver.io/) (免费) |
| **Another Redis Desktop Manager** | Redis管理 | [GitHub](https://github.com/qishibo/AnotherRedisDesktopManager) |
| **Postman** / **Apifox** | API调试 | [Postman](https://www.postman.com/) / [Apifox](https://apifox.com/) |
| **微信开发者工具** | 小程序开发 | [微信官方](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html) |
| **HBuilderX** | uni-app开发 | [DCloud](https://www.dcloud.io/hbuilderx.html) |

---

## 详细启动步骤

### 第一步：数据库部署

#### 1.1 安装并启动MySQL

安装完成后确保MySQL服务已启动：

```bash
# Windows - 检查MySQL服务状态
net start | findstr MySQL

# 如果未启动，手动启动
net start MySQL80
```

#### 1.2 创建数据库并导入数据

**方式一：命令行**

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本（会自动创建blog_db数据库和所有表）
source D:/Blog/blog-backend/src/main/resources/schema.sql;

# 验证表是否创建成功
USE blog_db;
SHOW TABLES;
```

**方式二：Navicat / DBeaver 图形化工具**

1. 新建连接 -> MySQL -> 填入主机 `localhost`、端口 `3306`、用户名 `root`、密码
2. 右键连接 -> 新建数据库 -> 名称填 `blog_db`，字符集选 `utf8mb4`
3. 右键 `blog_db` 数据库 -> 运行SQL文件 -> 选择 `blog-backend/src/main/resources/schema.sql`
4. 刷新表列表，应该能看到 `tb_user`、`tb_article` 等12张表

#### 1.3 启动Redis

```bash
# Windows - 如果是MSI安装的Redis，服务会自动运行
# 检查Redis是否在运行
redis-cli ping
# 应返回 PONG

# 如果未运行，手动启动
redis-server
```

#### 1.4 （可选）启动MinIO

如果需要文件上传功能，需要启动MinIO：

```bash
# 下载minio.exe后运行
minio server D:\minio-data --console-address ":9001"
```

启动后访问 http://localhost:9001 ，默认账号密码都是 `minioadmin`。
创建一个名为 `blog-files` 的Bucket。

---

### 第二步：后端启动

#### 2.1 安装JDK 17

安装完成后验证：

```bash
java -version
# 应显示 java version "17.x.x"
```

#### 2.2 安装Maven

下载解压后，将 `bin` 目录加入系统 `PATH`，验证：

```bash
mvn -version
```

#### 2.3 启动后端

**方式一：IntelliJ IDEA（推荐）**

1. 打开 IDEA -> `Open` -> 选择 `D:\Blog\blog-backend` 目录
2. IDEA会自动识别 `pom.xml` 并下载依赖（首次需要几分钟）
3. 找到 `src/main/java/com/xilei/blog/BlogApplication.java`
4. 右键 -> `Run 'BlogApplication'`
5. 控制台看到 `Started BlogApplication in x seconds` 表示启动成功
6. 访问 http://localhost:8080/api/doc.html 查看API文档

**方式二：VS Code**

1. 安装扩展：`Extension Pack for Java`
2. 打开 `D:\Blog\blog-backend` 目录
3. 找到 `BlogApplication.java` -> 点击 `Run` 按钮

**方式三：命令行**

```bash
cd D:\Blog\blog-backend

# 首次运行需要下载依赖
mvn clean install -DskipTests

# 启动
mvn spring-boot:run
```

#### 2.4 验证后端启动

```bash
# 测试接口
curl http://localhost:8080/api/front/article/list?pageNum=1&pageSize=5
```

应返回JSON格式的文章列表数据。

---

### 第三步：前端启动

#### 3.1 安装Node.js

安装完成后验证：

```bash
node -v
npm -v
```

#### 3.2 启动前端

**方式一：VS Code（推荐）**

1. 打开 VS Code -> `File` -> `Open Folder` -> 选择 `D:\Blog\blog-frontend`
2. 打开终端（`` Ctrl+` ``）
3. 安装依赖：
   ```bash
   npm install
   ```
4. 启动开发服务器：
   ```bash
   npm run dev
   ```
5. 终端会显示 `Local: http://localhost:5173/`
6. 按住 Ctrl 点击链接，或在浏览器打开 http://localhost:5173

**方式二：WebStorm / IDEA**

1. 打开 `D:\Blog\blog-frontend` 目录
2. 内置终端执行 `npm install` 然后 `npm run dev`

**方式三：任意终端**

```bash
cd D:\Blog\blog-frontend
npm install
npm run dev
```

#### 3.3 访问前台

浏览器打开 http://localhost:5173 ，可以看到博客首页。

#### 3.4 访问后台

1. 点击右上角「登录」按钮
2. 输入用户名 `admin`，密码 `admin123`
3. 登录后点击头像 -> 「后台管理」
4. 后台地址：http://localhost:5173/admin

---

### 第四步：微信小程序启动（可选）

#### 4.1 使用HBuilderX

1. 下载安装 [HBuilderX](https://www.dcloud.io/hbuilderx.html)
2. 文件 -> 导入 -> 从本地目录导入 -> 选择 `D:\Blog\blog-mini`
3. 安装依赖：在HBuilderX终端执行 `npm install`
4. 运行 -> 运行到小程序模拟器 -> 微信开发者工具

#### 4.2 使用命令行 + 微信开发者工具

```bash
cd D:\Blog\blog-mini
npm install
npm run dev:mp-weixin
```

然后打开**微信开发者工具**：
1. 导入项目 -> 选择 `D:\Blog\blog-mini\dist\dev\mp-weixin` 目录
2. AppID 可以选择「测试号」
3. 即可在模拟器中预览小程序

---

## 访问地址汇总

| 服务 | 地址 | 说明 |
|------|------|------|
| Web前台 | http://localhost:5173 | 博客首页、文章详情、分类标签等 |
| Web后台 | http://localhost:5173/admin | 管理员后台（需登录） |
| API接口 | http://localhost:8080/api | 后端API根路径 |
| API文档 | http://localhost:8080/api/doc.html | Knife4j接口文档（可在线调试） |
| MinIO控制台 | http://localhost:9001 | 文件存储管理 |

### 默认账号

| 系统 | 用户名 | 密码 |
|------|--------|------|
| 博客后台 | `admin` | `admin123` |
| MinIO | `minioadmin` | `minioadmin` |

---

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

---

## Docker 一键部署

如果已安装 Docker Desktop，可以一键启动所有服务：

```bash
# 复制环境变量模板
cp blog-backend/.env.example blog-backend/.env
# 编辑 .env 修改密钥和密码

# 一键启动（MySQL + Redis + MinIO + 后端 + 前端）
docker-compose up -d

# 查看运行状态
docker-compose ps

# 查看日志
docker-compose logs -f blog-backend

# 停止所有服务
docker-compose down
```

---

## 常见问题

**Q: 后端启动报 `Communications link failure`**
A: MySQL未启动，检查MySQL服务是否运行。

**Q: 后端启动报 `Connection refused: localhost:6379`**
A: Redis未启动，运行 `redis-server` 或检查Redis服务。

**Q: 前端 `npm install` 报错**
A: 尝试删除 `node_modules` 和 `package-lock.json`，重新 `npm install`。

**Q: 前端启动后页面空白**
A: 确保后端已启动，前端通过 `/api` 代理请求后端，后端没启动会请求失败。

**Q: 登录后访问后台报 401**
A: Token过期或无效，重新登录即可。

---

## 作者

**Heileoi**
