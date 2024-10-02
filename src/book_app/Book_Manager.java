package book_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.SQLException;

import java.sql.PreparedStatement; // 동적sql

public class Book_Manager {

	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String url = "jdbc:mysql://127.0.0.1:3306/bookdb?severTimeZone=UTC";
	private static String id = "root";
	private static String pw = "1234";

	public static Connection conn = null;
	public static Statement stmt = null;

	public Book_Manager() {
		// TODO Auto-generated constructor stub
	}

	public static void initDBConnect() { // db 연동
		try {
			Class.forName(driver); // driver을 메모리에 로드한다. (driver: 클래스이다.)
			Book_Manager.conn = DriverManager.getConnection(url, id, pw); // getConnection: 커넥션 객체를 만들어줌
			Book_Manager.stmt = conn.createStatement(); // 연결객체를 통해서 명령객체가 만들어져서 stmt에 넣는다.

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static boolean authenticateUser(String id, String password) {
        String sql = "SELECT * FROM user WHERE userid = ? AND userpw = ?"; // 테이블 이름과 컬럼 이름 변경 필요
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // 결과가 존재하면 true 반환
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	public void releaseDB() {
		try {
			this.conn.close();
			this.stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void getAllBook(String user_id) {

		// 문자열 변수에 쿼리를 저장
		String sql = "select * from Book b inner join rental r inner join user u on b.bookno = r.bookno and r.userid=u.userid where r.userid = ?";

		try {
			PreparedStatement pstmt = this.conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				String username = rs.getString("username");
				String bookno = rs.getString("bookno");
				String bookname = rs.getString("b.bookname");
				Timestamp rental_date = rs.getTimestamp("rental_date");
				String publisher = rs.getString("publisher");
				String return_yn = rs.getString("return_yn");

				System.out.println("===================================================================");
				System.out.println("유저 이름: " + username);
				System.out.println("책 번호: " + bookno);
				System.out.println("책 제목: " + bookname);
				System.out.println("대여기한: " + rental_date);
				System.out.println("출판사: " + publisher);
				System.out.println("반납 여부: " + return_yn);
				System.out.println("===================================================================");
			}
			// 다 사용하면 닫아준다.
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
