package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

@Service
@Transactional
public class RedisMysqlService {
    @Value("${db.userdb.datasource.url}")
    private String connectionUrl;

    public void RedisMysql() {
        // Redis 配置
        String redisHost = "localhost";
        int redisPort = 6379;
        String redisKey = "sql_statements";
        int batchSize = 50; // 设置批处理大小

        // 连接到 Redis
        try (Jedis jedis = new Jedis(redisHost, redisPort)) {
            // 从 Redis 获取前50条待执行的 SQL 语句
            List<String> sqlStatements = jedis.lrange(redisKey, 0, batchSize - 1);

            // 使用 JDBC 连接到 MySQL
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 Statement statement = connection.createStatement()) {

                for (String sql : sqlStatements) {
                    statement.addBatch(sql);
                }
                statement.executeBatch();
            }


            jedis.ltrim(redisKey, batchSize, -1);
        } catch (Exception e) {

            throw new RuntimeException("Failed to execute batch SQL statements", e);
        }
    }
}


