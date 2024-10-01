package book_app;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Book_List {

	static Book_Manager manager = new Book_Manager();
	static Book oneBook = null;
	static String nowUser = "";

	public static void bookMenu() {
		manager.initDBConnect();
		Scanner input = new Scanner(System.in);
		Book_Manager manager = new Book_Manager();
		while (true) {
            System.out.println("아이디를 입력하세요:");
            String idInput = input.nextLine();
            System.out.println("패스워드를 입력하세요:");
            String pwInput = input.nextLine();


            if (manager.authenticateUser(idInput, pwInput)) {
            	nowUser = idInput;
                System.out.println("로그인 성공!!!");
                
                break;  // 로그인 성공 시 반복문 종료
            } else {
                System.out.println("아이디 또는 패스워드가 잘못되었습니다.");
            }
        }
		
		boolean flag=false;
		
		while (true) {
			System.out.println("1. 도서보기, 2. 내책장보기, 3. 로그아웃");
			System.out.println("★ 메뉴 번호를 선택해 주세요");

			Scanner in = new Scanner(System.in);
			int number = in.nextInt();
			if (number < 1 || number >= 4) {
				System.out.println("메뉴번호가 틀렸습니다.");
				continue;
			} else {
				switch (number) {
				case 1:
					System.out.println("도서보기");
					showAllBook();	
					System.out.println("검색 기능을 선택해주세요.\n 1.전체보기 2.카테고리 검색 3.도서명 검색");
					int select_no = in.nextInt();
					in.nextLine();
					switch(select_no) {
					case 1:showAllBook();
					break;
					case 2:
						System.out.print("카테고리 명 : ");
						String category = in.nextLine();
						selectCategory(category);
						break;
					case 3:
						System.out.print("도서명 : ");
						String bookname = in.nextLine();
						searchBook(bookname);
						break;
					}
					pickBook();
					break;
				case 2:
					System.out.println("내책장보기");
					Book_MyList_info();
					return;
				case 3:
					System.out.println("로그아웃.");
					nowUser = "";
					flag=true;
					return;
				}
			}
			
			if(flag) {
				break;
			}
		}
	}
	
	public static void Book_MyList_info() {
		Scanner sc = new Scanner(System.in);
		System.out.println("===================================================================");
		System.out.println("현재 나의 서고 목록");
		System.out.println("===================================================================");
		System.out.println("찾으시려는 유저 명과, 반납여부를 체크해주세요");

		System.out.print("유저: ");
		String userid = sc.nextLine();
		System.out.print("반납여부: ");
		String yn = sc.nextLine();
		if (yn.toUpperCase().equals("N") || yn.toUpperCase().equals("Y")) {
			manager.getAllBook(userid, yn);
		} else {
			System.out.println("잘못입력하였습니다");
		}
	}
	public static void showAllBook() { // 모든 도서보기
		String sql = "select * from book order by bookno";
		try {
			ResultSet rs = manager.stmt.executeQuery(sql);

			while (rs.next()) {
				System.out.println("도서번호: " + rs.getInt("bookno"));
				System.out.println("도서명: " + rs.getString("bookname"));
				System.out.println("출판사: " + rs.getString("publisher"));
				System.out.println("대여가능 권 수: " + rs.getInt("bookcount"));
				System.out.println("도서종류: " + rs.getString("category"));

				System.out.println("=====================================");
			}
			rs.close();
			
			return;

		} catch (SQLException e) {
			System.out.println("showAllBook() 오류 발생");
			e.printStackTrace();
		}

	}

	public static void selectCategory(String category) { // 카테고리명에 대한 도서리스트를 가져오는 메서드
		String sql = "select * from book where category=? order by bookno";
		try {
			PreparedStatement pstmt = manager.conn.prepareStatement(sql);
			pstmt.setString(1, category);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				System.out.println("도서번호: " + rs.getInt("bookno"));
				System.out.println("도서명: " + rs.getString("bookname"));
				System.out.println("출판사: " + rs.getString("publisher"));
				System.out.println("대여가능 권 수: " + rs.getInt("bookcount"));
				System.out.println("도서종류: " + rs.getString("category"));

				System.out.println("=====================================");
			}
			rs.close();
			
			return;

		} catch (SQLException e) {
			System.out.println("selectCategory() 오류 발생");
			e.printStackTrace();
		}
	}

	public static void searchBook(String bookname) { // 도서명으로 도서정보 검색하기
		String sql = "select * from book where bookname=?";

		try {
			PreparedStatement pstmt = manager.conn.prepareStatement(sql);
			pstmt.setString(1, bookname);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) { // rs.next(): rs 레코드 하나를 본인이 가지고 있다.
				System.out.println("도서번호: " + rs.getInt("bookno"));
				System.out.println("도서명: " + rs.getString("bookname"));
				System.out.println("출판사: " + rs.getString("publisher"));
				System.out.println("대여가능 권 수: " + rs.getInt("bookcount"));
				System.out.println("도서종류: " + rs.getString("category"));
			}
			rs.close();
			
			return;

		} catch (SQLException e) {
			System.out.println("searchBook() 오류 발생");
			e.printStackTrace();
		}
	}

	public static int countBook() { // 책 권수 세기

		String sql = "select count(*) as cnt from book";
		int cnt = 0;

		try {
			ResultSet rs = manager.stmt.executeQuery(sql);

			if (rs.next()) { // rs.next(): rs 레코드 하나를 본인이 가지고 있다.
				cnt = rs.getInt("cnt");
			}
			rs.close();

		} catch (SQLException e) {
			System.out.println("countBook() 오류 발생");
			e.printStackTrace();
		}

		return cnt;
	}

	public static void pickBook() { // 도서 선택시 해당 도서 전체정보 보여주기
		String sql = "select * from book where bookno=?";

		Scanner input = new Scanner(System.in);

		while (true) {
//			System.out.print("도서번호를 입력해주세요. : ");
			int bookno = input.nextInt();
			input.nextLine();

			if (0 > bookno || bookno > countBook()) {
				System.out.println("해당 번호는 없는 도서번호입니다. 다시 입력해주세요.");
				continue;
			} else {
				try {
					PreparedStatement pstmt = manager.conn.prepareStatement(sql);
					pstmt.setInt(1, bookno);
					ResultSet rs = pstmt.executeQuery();

					if (rs.next()) {
						int id = rs.getInt("bookno");
						String name = rs.getString("bookname");
						String remark = rs.getString("remark");
						String publisher = rs.getString("publisher");
						int cnt = rs.getInt("bookcount");
						String category = rs.getString("category");

						System.out.println("도서번호: " + id);
						System.out.println("도서명: " + name);
						System.out.println("비고: " + remark);
						System.out.println("출판사: " + publisher);
						System.out.println("대여가능 권 수: " + cnt);
						System.out.println("도서종류: " + category);

						// 대여관련
						oneBook = new Book(id, name, remark, publisher, cnt, category);

						System.out.print("대여하시겠습니까? y/n :");
						String yn = input.nextLine();

						if (yn.toLowerCase().equals("y")) {
							rentalBook();
//							if (!selectRental(nowUser.getUserid(), id)) {
//								rentalBook();
//							} else {
//								System.out.println("이미 대여된 도서입니다.");
//							}
						}
						break;
					}
					rs.close();

				} catch (SQLException e) {
					System.out.println("pickBook() 오류 발생");
					e.printStackTrace();
				}
			}
		}
	}

	public static void rentalBook() { // 대여테이블에 책정보 저장

		String sql = "insert into rental values(?, ?, ?, 'n')";
		LocalDateTime date = LocalDateTime.now().plusDays(7);

		try {
			PreparedStatement pstmt = manager.conn.prepareStatement(sql);
			pstmt.setString(1, nowUser); // (change)
			pstmt.setInt(2, oneBook.getBookno());
			pstmt.setTimestamp(3, Timestamp.valueOf(date));
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("rentalBook() 오류 발생");
			e.printStackTrace();
		}
	}

//	public static boolean selectRental(String userid, int bookno) { // 대여테이블에 책이 대여되어있는지 확인
//
//		String sql = "select * from rental where userid=? and bookno=?";
//
//		try {
//			PreparedStatement pstmt = manager.conn.prepareStatement(sql);
//			pstmt.setString(1, userid);
//			pstmt.setInt(2, bookno);
//			ResultSet rs = pstmt.executeQuery();
//
//			if (rs.next()) { // rs.next(): rs 레코드 하나를 본인이 가지고 있다.
//				return true;
//			}
//			rs.close();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return false;
//	}
}
