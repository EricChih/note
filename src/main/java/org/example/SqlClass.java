package org.example;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Random;
@Service
public class SqlClass {
    public void createsql() {
        // Redis 配置
        String redisHost = "localhost";
        int redisPort = 6379;
        String redisKey = "sql_statements";
        Jedis jedis = new Jedis(redisHost, redisPort);

        Random random = new Random();
        // 中文字符数组，用于生成用户名
        String[] chineseChars = {"王", "李", "张", "刘", "陈", "杨", "赵", "黄", "周", "吴"};

        for (int i = 1; i <= 100; i++) {
            // 生成随机中文用户名和年龄
            String username = generateRandomChineseName(chineseChars, 3);
            int age = random.nextInt(100); // 假设年龄范围是 0 到 99

            // 创建对应的 SQL 插入语句
            String sql = "INSERT INTO user (username, age) VALUES ('" + username + "', " + age + ");";

            // 将 SQL 语句写入 Redis 的列表
            jedis.rpush(redisKey, sql);
        }

        // 关闭 Redis 连接
        jedis.close();
    }

    // 生成随机中文名字的辅助方法
    private static String generateRandomChineseName(String[] chineseChars, int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        // 生成姓
        sb.append(chineseChars[random.nextInt(chineseChars.length)]);
        // 生成名
        for (int i = 1; i < length; i++) {
            sb.append(chineseChars[random.nextInt(chineseChars.length)]);
        }
        return sb.toString();
    }
}
