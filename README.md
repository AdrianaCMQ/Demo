# O-quiz-final

请使用 Spring Boot、Spring Data JPA、Flyway、Spring Boot Testing、Lombok 等框架，开发一个 Spring Boot Web Application，提供 API 供前端 app 调用。

## 环境说明

> 提醒：`docker` 不要使用 `Docker Desktop`，如果有，请立即删除

- Mysql: 8.+
    - 容器安装
        - 安装 `colima`，详见 [这里](https://github.com/abiosoft/colima#installation)，安装完成后启动 `colima`：```colima start```
        - 安装 `docker`, `docker-compose` 客户端, 可通过 ```brew install docker docker-compose``` 进行安装
    - 数据库容器目录创建
      - 切换到项目目录 
      - 运行命令 ```mkdir data && mkdir test_data```
    - 数据库容器启停
        - 启动开发及测试环境 `mysql` 容器：```docker-compose up```，如希望后台运行，可运行 ```docker-compose up -d```，此时如想查看容器状态，可使用 ```docker ps -a```
        - 关闭开发及测试环境 `mysql` 容器：```docker-compose down```
    - 数据库说明，详见[这里](./docker-compose.yml)

## 需求说明

### 技术要求

* 请统一使用默认的 8080 端口，无需使用 HTTPS；
* 使用 Flyway 作为 Database Migration 工具，完成数据表的创建操作
* API 需通过 Swagger 生成接口文档
* 实现过程中请参照 EOT 技术维度要求，重视实现细节

### 测试要求

* 根据 Controller、Service、Repository 各层特点，选用合适的测试策略（不需要编写系统测试）
* 请完成系统测试、集成测试、单元测试的编写

### 功能需求

一个电商系统中，有订单和订单项两个关键概念。

- 订单表示用户购买商品时的一种行为，包含了订单号、收件人、收件地址、手机号、订单项信息
- 订单项则表示一个订单中包含的具体商品信息，一个订单项中包括订单项编号、商品编号、商品名称、商品单价、商品数量、商品分类信息

#### 需求一

实现创建订单功能 API，业务规则如下

- 订单中包含订单项
- 下单后除返回收件人、收件地址、手机号、订单项信息外，还需返回订单号、下单时间与订单总金额信息
    - 下单时间由后端生成
    - 订单号与订单项编号采用自增规则生成
    - 订单总金额为所有订单项中（商品单价 * 商品数量）累加和，保留小数点 2 位，只舍不入（例如，总金额计算为 390.937，记为 390.93）
- 字段是否必填，请结合对业务的理解进行设置
- 商品分类字段取值为
    - electronics
    - women's clothing
    - men's clothing

- 实现商品列表获取 API，规则如下（请注意，此 API 不需要编写测试）
    - 通过第三方系统 https://fakestoreapi.com 获取商品列表，商品列表 API 请参照 https://fakestoreapi.com/products
    - 不需支持分页与排序

##### 术语表

| 中文   | 英文        |
|------|-----------|
| 订单   | order     |
| 订单号  | id        |
| 收件人  | addressee |
| 收件地址 | address   |
| 手机号  | mobile    |
| 下单时间 | createdAt |


| 中文    | 英文        |
|-------|-----------|
| 订单项   | orderItem |
| 订单项编号 | id        |
| 商品编号  | productId |
| 商品名称  | title     |
| 商品单价  | price     |
| 商品数量  | quantity  |
| 商品分类  | category  |



