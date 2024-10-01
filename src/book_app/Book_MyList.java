package book_app;

import java.util.Scanner;

public class Book_MyList {

	Book_Manager manager = null;
	Book user = null;
	boolean flag = false;
	String user_id = "";
	public Book_MyList() {
		manager = new Book_Manager();
		manager.initDBConnect();
	}

	public void Book_MyList_info() {
		Scanner sc = new Scanner(System.in);
		System.out.println("===================================================================");
		System.out.println("현재 나의 서고 목록");
		System.out.println("===================================================================");
		System.out.println("찾으시려는 유저 명과, 반납여부를 체크해주세요");

//		System.out.print("유저: ");
//		String userid = sc.nextLine();
		System.out.print("반납여부: ");
		String yn = sc.nextLine();
		if (yn.toUpperCase().equals("N") || yn.toUpperCase().equals("Y")) {
//			manager.getAllBook(userid, yn);
		} else {
			System.out.println("잘못입력하였습니다");
		}
	}

}
