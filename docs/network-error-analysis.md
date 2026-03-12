# Axios `Network Error` 问题排查（本项目）

## 结论（结合当前仓库配置）

前端报错：

- `AxiosError: Network Error`
- 堆栈在 `generated/core/request.ts` 的 `sendRequest`

这类错误通常说明**浏览器没有拿到任何 HTTP 响应**（不是 4xx/5xx 业务错误）。
你补充的启动日志已经给出一个明确根因：

- 后端启动阶段创建 `postEsDao` 失败；
- 失败原因是连接 Elasticsearch `localhost:9200` 超时；
- Spring 容器启动中断，Tomcat 停止；
- 最终前端请求 `http://localhost:8121/api/...` 无服务可连，触发 `Network Error`。

---

## 证据与项目内配置

### 1）前端 OpenAPI Base 写死为 `http://localhost:8121`

`generated/core/OpenAPI.ts`：

```ts
BASE: 'http://localhost:8121'
```

且接口路径本身带 `/api/...`，所以真实请求地址为：

- `http://localhost:8121/api/...`

### 2）后端端口和 context-path 默认与前端匹配

`oj-backend/src/main/resources/application.yml`：

- `server.port: 8121`
- `server.servlet.context-path: /api`

因此只要后端正常启动，地址本身是匹配的。

### 3）本次实际阻塞点：Elasticsearch 在启动阶段不可达

你给出的日志核心信息：

- `Error creating bean with name 'postEsDao'`
- `Timeout connecting to [localhost/127.0.0.1:9200]`

这与项目中启用 ES Repository 的行为一致：`PostEsDao` 是 `ElasticsearchRepository`，初始化时会访问 ES，ES 不可达就会导致启动失败。

### 4）README 中 8101 与默认 8121 的端口描述不一致

`oj-backend/README.md` 示例写的是 `8101`，而默认 `application.yml` 是 `8121`。
此项虽然不是你本次日志中的直接根因，但确实是常见误配来源。

---

## 为什么会直接看到 `Network Error` 而不是 500？

`generated/core/request.ts` 的逻辑是：

- 有 `axiosError.response`（例如 401/403/500）才走 HTTP 状态处理；
- 完全拿不到响应（连接失败 / 被拦截）会直接抛出 `Network Error`。

后端进程都没成功启动时，浏览器自然拿不到 HTTP 响应，因此表现为 `Network Error`。

---

## 已在仓库中落地的修复

为了避免“开发环境没起 ES 就整站起不来”，已增加配置：

- `application.yml`：默认关闭 ES Repository 初始化
  - `spring.data.elasticsearch.repositories.enabled: false`
- `application-test.yml`、`application-prod.yml`：显式保持开启
  - `spring.data.elasticsearch.repositories.enabled: true`

这样本地开发即使没有 ES，后端也可先启动，前端不再因为后端进程直接挂掉而报 Network Error。

---

## 建议排查顺序（按你当前问题优化）

1. 先看后端是否启动成功（你这次已定位为 ES 连接超时）。
2. 若开发环境不依赖 ES，确保 `spring.data.elasticsearch.repositories.enabled=false`。
3. 若需要 ES 功能，先启动 ES 并确认 `localhost:9200` 可连通。
4. 再检查前端失败请求 URL 是否为 `http://localhost:8121/api/...`。
5. 最后再检查跨机器 localhost、HTTPS/HTTP mixed content 等浏览器层问题。

