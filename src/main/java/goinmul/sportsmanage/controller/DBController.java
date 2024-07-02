/*
package goinmul.sportsmanage.controller;
import java.sql.*;
public class DBController {
    public static void main(String[] args) {
        Connection connection = null;
        try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            // 드라이버 연결

            // 데이터베이스 연결부
            String url = "";
            String username = "";
            String password = "";

            //환경 변수 테스트
//            String url = System.getenv("DB_URL");
//            String username = System.getenv("DB_USERNAME");
//            String password = System.getenv("DB_PASSWORD");

            connection = DriverManager.getConnection(url, username, password);

            System.out.println(connection);

            // 3. SQL 쿼리 실행
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM user";
            ResultSet resultSet = statement.executeQuery(query);



            while (resultSet.next()) {
                // 가져온 각 행의 열 데이터 얻기
                String id = resultSet.getString("user_id");
                String name = resultSet.getString("name");
                // 결과 출력 또는 다른 작업 수행
                System.out.println("ID: " + id + ", Name: " + name);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

 */