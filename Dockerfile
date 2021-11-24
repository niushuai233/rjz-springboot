# 基础镜像
FROM openjdk:8-jre-alpine

# 维护者信息
MAINTAINER niushuai233 shuai.niu@foxmail.com

# 拷贝jar
ADD target/rjz.jar /rjz.jar

# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone

# 指明暴漏端口信息
EXPOSE  9080

# 入口程序
ENTRYPOINT ["java", "-jar", "/rjz.jar"]
