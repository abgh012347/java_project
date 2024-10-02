package book_app;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;

import book_app.Book_Manager.*;

public class User_List {

	static User saveUser=null;

	public User_List() {
		// TODO Auto-generated constructor stub
	}

	public static void userMenu() {
		Book_Manager.initDBConnect();
		Scanner input = new Scanner(System.in);
		while (true) {
			System.out.print("아이디를 입력하세요:");
			String idInput = input.nextLine();

			if (!duplicationUser(idInput)) {
				System.out.print("이름을 입력하세요:");
				String nameInput = input.nextLine();
				System.out.print("패스워드를 입력하세요:");
				String pwInput = input.nextLine();

				saveUser = new User(idInput, nameInput, pwInput);
				insertUser(saveUser);

				break;

			} else {
				System.out.println("중복사용 불가능한 아이디입니다.");
				continue;
			}
		}	
	}

	public static boolean duplicationUser(String idInput) {
		String sql = "SELECT * FROM user WHERE userid = ?";
        try (PreparedStatement statement = Book_Manager.conn.prepareStatement(sql)) {
            statement.setString(1, idInput);
            ResultSet rs = statement.executeQuery();
            return rs.next(); // 결과가 존재하면 true 반환
        } catch (Exception e) {
            e.printStackTrace();
        }

		return false;
	}

	public static void insertUser(User user) {
		String sql = "insert into user values(?, ?, ?)";

		try {
			PreparedStatement pstmt = Book_Manager.conn.prepareStatement(sql);
			pstmt.setString(1, user.getUserid());
			pstmt.setString(2, user.getUsername());
			pstmt.setString(3, user.getUserpw());
			pstmt.executeUpdate();

			System.out.println("회원가입이 완료되었습니다.");

		} catch (SQLException e) {
			System.out.println("rentalBook() 오류 발생");
			e.printStackTrace();
		}
	}


}