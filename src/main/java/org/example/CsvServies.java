package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;


@Service
@Transactional
@EnableScheduling
public class CsvServies {

    @Value("${db.userdb.datasource.url}")
    private String connectionUrl;


    public void readCsvFile(String filePath) {
        System.out.println(connectionUrl);
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            // 跳過標題行
            br.readLine();

            // 允許多個操作作為同一個指令
            conn.setAutoCommit(false);

            String sql = "INSERT INTO user (username, age) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    stmt.setString(1, data[0]); // username
                    stmt.setInt(2, Integer.parseInt(data[1].trim())); // age
                    stmt.executeUpdate();
                }

                // 寫完之後再確認提交
                conn.commit();
            } catch (SQLException ex) {
                // 如果有錯誤，回滾
                conn.rollback();
                throw ex;
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

//    @Scheduled(cron = "10 * * * * ?")
    public void exportCSV() {
        String csvFilePath = "C:\\Users\\Eric\\Desktop\\test\\user.csv";
        try (
                Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();

                FileOutputStream fos = new FileOutputStream(csvFilePath);
                // 使用 OutputStreamWriter 並指定 UTF-8 编码，同時寫入 BOM
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)
        ) {
            String sql = "SELECT username, age FROM user";

            ResultSet result = statement.executeQuery(sql);

            osw.write('\ufeff');

            osw.append("UserName");
            osw.append(",");
            osw.append("Age");
            osw.append("\n");


            while (result.next()) {
                String name = result.getString("username");
                int age = result.getInt("age");

                osw.append(name);
                osw.append(",");
                osw.append(String.valueOf(age));
                osw.append("\n");
            }

            osw.flush();

            System.out.println("CSV文件已創建：" + csvFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
