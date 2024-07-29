# spring-web-area
提供全国地区相关功能，支持5级区域（1-省，2-市，3-县，4-乡，5-村），包括区域树查询、变更、搜索等、
```
cd existing_repo
git remote add origin http://xx.xx.xx.xx:9090/xxx-starter.git
git branch -M master
git push -uf origin master
```

## USE

- add config
```
spring:
datasource:
driver-class-name: com.mysql.cj.jdbc.Driver
url: jdbc:mysql://xx.xx.xx.xx:3309/zl_db?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&max_query_size=100000000
username: root
password: 123456


joelly:
    area:
enable: true
```



