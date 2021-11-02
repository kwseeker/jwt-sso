# JWT原理与场景

`JWT（Json Web Token）`是一种**协议规范[RFC7519](https://www.rfc-editor.org/info/rfc7519)** 。



## 1 Java实现

直接看规范太枯燥，还是先找个框架结合一个例子看吧。

### 1.1 框架选择

实现很多，官方推荐的有**auth0 java-jwt**、**jose4j**、**nimbus-jose-jwt**、**jjwt**、**fusionauth**、**vertx-auth-jwt**。

看网文最受欢迎的是**nimbus-jose-jwt**, 官方仓库开源在`BitBucket`上。

后面有时间的话再亲自看看实现都有何不同。

### 1.2 nimbus-jose-jwt

```shell
# 当前(2021-11-02)最新源码稳定分支是9.15.2 
git checkout -b f-learn 9.15.2
```

实现还是有点复杂的，先看主要的，即**Token的生成**和**Token的校验**。

参考源码 `SignedJWTTest$testSignAndVerify`方法。

此方法执行流程：

1. 使用Java标准库的`KeyPairGenerator`生成公私钥对；项目中不用每次都生成，只生成一次就行；

   ```java
   KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
   kpg.initialize(2048);
   ```

   获取的是`RSAKeyPairGenerator`实例，实例包含四个重要参数

   ```java
   //密钥长度，长度越大加密强度越大，如果设置比较短的长度还是可能被暴力破解
   private int keySize;
   private final KeyType type;
   private AlgorithmId rsaId;
   private SecureRandom random;
   ```

2. 创建了`JWTClaimsSet` `JWSHeader`实例，并用它们创建了`SignedJWT`实例；

   `JWTClaimsSet`定义JWT的`Payload`部分，其实是个`LinkedHashMap`, 用于存放声明(`claim`), 声明是关于一个主题的一条信息，是键值对的形式（键总是字符串，值可以是任意JSON值）；JWT声明分为三种：已注册声明、公共声明、私有声明。

   ```shell
   # 在RFC5719第4章上有详细说明
   # 已注册声明
   iss		签发人信息，大小写敏感，StringOrURI，可选
   sub		主题，要么iss下本地惟一，要么全局惟一，大小写敏感，StringOrURI，可选
   aud		收件人，有区分大小写的字符串构成的数组， StringOrURI，可选
   exp		失效时间，（可能有时差，可以留几分钟缓和余地）必须是number类型包含NumericDate值，可选
   nbf		生效时间，（可能有时差，可以留几分钟缓和余地）必须是number类型包含NumericDate值，可选
   iat		 JWT 所颁发的时间，用于计算这个 JWT 已经使用的期限，必须是number类型包含NumericDate值，可选
   jti		JWT 的唯一标识符，用于避免 JWT 被重复发送，大小写敏感，可选
   ```

   另两种声明没看明白什么意思。

   `JWSHeader`定义JWT的`Header`部分，`Header`参数也分为已注册头部参数、公共头部参数、私有头部参数，更多参考RFC7515。

   ```
   alg		算法类型
   jku		报头参数（JWK Set URL）
   jwk		JSON Web Key
   kid		Key ID
   x5u		X.509 URL
   x5c		X.509 Certificate Chain
   x5t		X.509 Certificate SHA-1 Thumbprint
   x5t#S256		X.509 Certificate SHA-256 Thumbprint
   typ		Type
   cty		Content Type
   crit	Critical
   ```

   

### 1.3 知识点图谱

下面列举使用JWT必须掌握的知识点：

+ **加密算法**

  + **非对称加密**

    + **RSA** (使用最广泛,三个人的名字首字母)

      利用极大整数难以因数分解的原理。

  + **对称加密**

    + **HS256**

      

+ **JWS & JWE**

  是JWT的两种实现方式（具有不同的格式规范）。

  `JWS (JSON Web Signature)`的Token由`Header`、`Payload`、`Signature`三部分组成; 

  分别用于存储基本信息（如类型typ、签名算法alg等）、业务相关信息（如JWT的签发者iss、面向用户sub、接收者aud、过期时间exp、签发时间戳iat、以及拓展的业务数据等）、签名字符串。

  JWS的主要目的是保证了数据在传输过程中不被修改，验证数据的完整性。

  `JWE (JSON Web Encryption)`的Token由`JOSE Header`、`JWE Encypted Key`、`Initialization Vector`、`Ciphertext`、`Authentication Tag`五部分组成。

  JWE相对于JWS将业务数据等信息也进行了加密，兼顾数据的安全性与完整性。

+ 

## 2 规范主要内容

+ `JWT`被编码为`JSON`对象，作为`JWS`的`payload`或`JWE`的`plaintext`
+ 好





## 3 附录

## 3.1 参考资料

+ 《密码学原理与Java实现》

+ [RSA算法原理(二)](https://www.ruanyifeng.com/blog/2013/07/rsa_algorithm_part_two.html)

+ [RFC7519 (JWT)](https://www.rfc-editor.org/info/rfc7519) [RFC7515 (JWS)](https://www.rfc-editor.org/info/rfc7515) [RFC7516 (JWE)](https://www.rfc-editor.org/info/rfc7516) 

   

