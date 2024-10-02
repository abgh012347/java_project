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
		boolean end_flag=false;
		
		while(true) {
			System.out.println("1. 로그인, 2. 회원가입 3. 도서앱 종료");
			System.out.println("★ 메뉴 번호를 선택해 주세요");

			Scanner in = new Scanner(System.in);
			String number = in.nextLine();

			if (!checkInputOnlyNumberAndAlphabet(number) || number.length() != 1) {
				System.out.println("숫자를 입력해주세요. (1글자)");
				continue;
			}

			int num = Integer.parseInt(number);
			
			switch(num) {
			case 1:
				AfterLoginMenu();
				break;
			case 2:
				User_List.userMenu();
				break;
			case 3:
				System.out.println("도서앱을 종료합니다.");
				end_flag=true;
				break;
			}
			
			if(end_flag) {
				break;
			}
		}
		
	}
	
	public static void AfterLoginMenu() {
		Scanner input = new Scanner(System.in);
		
		while (true) {
			System.out.print("아이디를 입력하세요:");
			String idInput = input.nextLine();
			System.out.print("패스워드를 입력하세요:");
			String pwInput = input.nextLine();

			if (manager.authenticateUser(idInput, pwInput)) {
				nowUser = idInput;
				System.out.println("로그인 성공!!!");

				break; // 로그인 성공 시 반복문 종료
			} else {
				System.out.println("아이디 또는 패스워드가 잘못되었습니다.");
				continue;
			}
		}
		
		boolean flag = false;

		while (true) {
			System.out.println("1. 도서보기, 2. 내책장보기, 3. 로그아웃");
			System.out.println("★ 메뉴 번호를 선택해 주세요");

			Scanner in = new Scanner(System.in);
			String number = in.nextLine();

			if (!checkInputOnlyNumberAndAlphabet(number) || number.length() != 1) {
				System.out.println("숫자를 입력해주세요. (1글자)");
				continue;
			}

			int num = Integer.parseInt(number);

			if (num < 1 || num >= 4) {
				System.out.println("메뉴번호가 틀렸습니다.");
				continue;
			} else {
				switch (num) {
				case 1:
					System.out.println("도서보기");
//					showAllBook();	

					while (true) {
						System.out.println("검색 기능을 선택해주세요.\n1.전체보기 2.카테고리 검색 3.도서명 검색");
						String select_no = in.nextLine();

						if (!checkInputOnlyNumberAndAlphabet(select_no) || select_no.length() != 1) {
							System.out.println("숫자를 입력해주세요. (1글자)");
							continue;
						}

						int selectNo = Integer.parseInt(select_no);

						switch (selectNo) {
						case 1:
							showAllBook();
							break;
						case 2:
							countCategory();
							break;
						case 3:
							BeforeSearchBook();
							break;
						}
						break;
					}
					pickBook();
					break;

				case 2:
					System.out.println("내책장보기");
					System.out.println("===================================================================");
					System.out.println("현재 나의 서고 목록");
					System.out.println("===================================================================");
					manager.getAllBook(nowUser);
					return;
				case 3:
					System.out.println("로그아웃되었습니다.");
					nowUser = "";
					flag = true;
					return;
				}
			}

			if (flag) {
				break;
			}
		}
	}

//	public static void Book_MyList_info() {
//		Scanner sc = new Scanner(System.in);
//		System.out.println("===================================================================");
//		System.out.println("현재 나의 서고 목록");
//		System.out.println("===================================================================");
//
//		
//		manager.getAllBook(userid);
//	}

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

	public static void countCategory() {

		String sql = "select count(*) as cnt from book where category=? order by bookno";
//		int cnt = 0;
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.print("카테고리명 : ");
			String category = sc.nextLine();
			int cnt = 0;

			try {
				PreparedStatement pstmt = manager.conn.prepareStatement(sql);
				pstmt.setString(1, category);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) { // rs.next(): rs 레코드 하나를 본인이 가지고 있다.
					cnt = rs.getInt("cnt");
				}

				if (cnt == 0) {
					System.out.println("맞는 카테고리명이 없습니다. 다시 입력해주세요.");
					continue;
				} else {
					selectCategory(category);
				}
				rs.close();

			} catch (SQLException e) {
				System.out.println("countCategory() 오류 발생");
				e.printStackTrace();
			}

			break;
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

	public static void BeforeSearchBook() {

		String sql = "select count(*) as cnt from book where bookname=? order by bookno";
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.print("도서명 : ");
			String bookname = sc.nextLine();

			int cnt = 0;

			try {
				PreparedStatement pstmt = manager.conn.prepareStatement(sql);
				pstmt.setString(1, bookname);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) { // rs.next(): rs 레코드 하나를 본인이 가지고 있다.
					cnt = rs.getInt("cnt");
				}

				if (cnt == 0) {
					System.out.println("맞는 도서명이 없습니다. 다시 입력해주세요.");
					continue;
				} else {
					searchBook(bookname);
				}
				rs.close();

			} catch (SQLException e) {
				System.out.println("BeforeSearchBook() 오류 발생");
				e.printStackTrace();
			}

			break;
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

	public static int selectBookCount(String bookname) { // 도서명별 대여가능 권 수 반환
		String sql = "select bookcount as cnt from book where bookname=?";
		int count=0;
		
		try {
			PreparedStatement pstmt = manager.conn.prepareStatement(sql);
			pstmt.setString(1, bookname);
			ResultSet rs = pstmt.executeQuery();

			if(rs.next()) {
				count=rs.getInt("cnt")-1;
			}
			rs.close();

		} catch (SQLException e) {
			System.out.println("selectBookCount() 오류 발생");
			e.printStackTrace();
		}
		
		return count;
	}
	
	public static void minusBookCount(String b_name) { // 도서별 대여가능 권 수 업데이트
		String sql = "update book set bookcount=? where bookname=?";
		int cnt=selectBookCount(b_name);
		
		if(cnt==-1) {
			System.out.println("해당 도서는 대여가 불가능합니다.");
			return;
		} else {
			System.out.println("'" + oneBook.getBookname() + "'" + " 도서 대여가 완료되었습니다.");
		}
		
		try {
			PreparedStatement pstmt = manager.conn.prepareStatement(sql);
			pstmt.setInt(1, cnt);
			pstmt.setString(2, b_name);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("minusBookCount() 오류 발생");
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
			System.out.print("대여할 도서의 번호를 입력해주세요: ");
			String bookno = input.nextLine();

			if (!checkInputOnlyNumberAndAlphabet(bookno) || bookno.length() != 1) {
				System.out.println("해당 번호는 없는 도서번호입니다. 다시 입력해주세요.");
				continue;
			}

			int bookNo = Integer.parseInt(bookno);

			if (0 > bookNo || bookNo > countBook()) {
				System.out.println("해당 번호는 없는 도서번호입니다. 다시 입력해주세요.");
				continue;
			} else {
				try {
					PreparedStatement pstmt = manager.conn.prepareStatement(sql);
					pstmt.setInt(1, bookNo);
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

						while (true) {
							System.out.print("대여하시겠습니까? y/n :");
							String yn = input.nextLine();

							if (yn.toLowerCase().equals("y") || yn.toLowerCase().equals("n")) {
								if (yn.toLowerCase().equals("y")) {
									rentalBook();
								}

								break;
							} else {
								System.out.println("y 또는 n만 입력 가능합니다. 다시 입력해주세요.");
								continue;
							}
						}
					}
					rs.close();

				} catch (SQLException e) {
					System.out.println("pickBook() 오류 발생");
					e.printStackTrace();
				}
				break;
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

			minusBookCount(oneBook.getBookname());
//			System.out.println("'" + oneBook.getBookname() + "'" + " 도서 대여가 완료되었습니다.");

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

	public static boolean checkInputOnlyNumberAndAlphabet(String textInput) {
		char chrInput;

		for (int i = 0; i < textInput.length(); i++) {

			chrInput = textInput.charAt(i);

			if (chrInput >= 0x30 && chrInput <= 0x39) {
				return true;
			}
		}
		return false;
	}
}
