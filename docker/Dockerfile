# 使用OpenJDK作为基础镜像，选择适合的版本
FROM openjdk:17-jdk-alpine

# 设置工作目录
WORKDIR /opt/gateway

# 将JAR文件复制到容器中
COPY gateway/spark-gateway-0.0.1-SNAPSHOT.jar spark-gateway-0.0.1-SNAPSHOT.jar

# 暴露端口（Spring Cloud Gateway默认端口是8080，但具体看您的配置）
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "spark-gateway-0.0.1-SNAPSHOT.jar"]