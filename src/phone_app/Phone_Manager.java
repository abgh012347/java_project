package phone_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Scanner;

import book_app.Book_List;

public class Phone_Manager {
	public String driver = "com.mysql.cj.jdbc.Driver";
	public String url = "jdbc:mysql://127.0.0.1:3306/phoneDB?severTimeZone=UTC";
	public String id = "root";
	public String pw = "1234";
	public static String p_phone_number;
	public String user_id;
	public String user_name;
	public String phone_number;
	public String block_yn = "n";
	public Connection conn = null;
	public Statement stmt = null;
	public Phone_DBManager[] blockedList = null;
	
	private int history_no;
//	private int user_id;
//	private String phone_number;
	private LocalDateTime call_date;

	


	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	

	

	public String getBlock_yn() {
		return block_yn;
	}

	public void setBlock_yn(String block_yn) {
		this.block_yn = block_yn;
	}


	
//	public Phone_Call_History(String phone_number, LocalDateTime call_date) {
//		this.phone_number=phone_number;
//		this.call_date=call_date;
//	}

//	public Phone_Call_History(String number) {
//		this.phone_number=number;
//	}

	public int getHistory_no() {
		return history_no;
	}

	public void setHistory_no(int history_no) {
		this.history_no = history_no;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public LocalDateTime getCall_date() {
		return call_date;
	}

	public void setCall_date(LocalDateTime call_date) {
		this.call_date = call_date;
	}

	public Phone_Manager() {
		// TODO Auto-generated constructor stub
		
	}
	
	public void Main_Menu_Open() {
		 while(true) {
			 	System.out.println("===========================================================================================");
		    	System.out.println("███████╗███╗   ███╗ █████╗ ██████╗ ████████╗    ██████╗ ██╗  ██╗ ██████╗ ███╗   ██╗███████╗");
		    	System.out.println("██╔════╝████╗ ████║██╔══██╗██╔══██╗╚══██╔══╝    ██╔══██╗██║  ██║██╔═══██╗████╗  ██║██╔════╝");
		    	System.out.println("███████╗██╔████╔██║███████║██████╔╝   ██║       ██████╔╝███████║██║   ██║██╔██╗ ██║█████╗  ");
		    	System.out.println("╚════██║██║╚██╔╝██║██╔══██║██╔══██╗   ██║       ██╔═══╝ ██╔══██║██║   ██║██║╚██╗██║██╔══╝  ");
		    	System.out.println("███████║██║ ╚═╝ ██║██║  ██║██║  ██║   ██║       ██║     ██║  ██║╚██████╔╝██║ ╚████║███████╗");
		    	System.out.println("╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝       ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝");
		    	System.out.println("===========================================================================================");
	            System.out.println("1. 전화, 2. 도서, 3. 종료" );
	            System.out.println("★ 메뉴 번호를 선택해 주세요");

	            Scanner in = new Scanner(System.in);
	            int number = in.nextInt();
	            if(number < 1 || number >= 4) {
	                System.out.println("메뉴번호가 틀렸습니다.");
	                continue;
	            } else {
	                switch(number) {
	                    case 1: 
	                        //System.out.println("전화");
	                        Phone_Menu_Open();
	                        break;
	                    case 2: 
	                        //System.out.println("도서");
	                        Book_List blist = new Book_List();
	                        blist.bookMenu();
	                        break;
	                    case 3:
	                        System.out.println("종료합니다.");
	                        return; 
	                }
	            }
	        }
	}
	public void Phone_Menu() {
			System.out.println("===============================================================================");
			System.out.println("██████╗ ██╗  ██╗ ██████╗ ███╗   ██╗███████╗     ██████╗ █████╗ ██╗     ██╗     ");
			System.out.println("██╔══██╗██║  ██║██╔═══██╗████╗  ██║██╔════╝    ██╔════╝██╔══██╗██║     ██║     ");
			System.out.println("██████╔╝███████║██║   ██║██╔██╗ ██║█████╗      ██║     ███████║██║     ██║     ");
			System.out.println("██╔═══╝ ██╔══██║██║   ██║██║╚██╗██║██╔══╝      ██║     ██╔══██║██║     ██║     ");
			System.out.println("██║     ██║  ██║╚██████╔╝██║ ╚████║███████╗    ╚██████╗██║  ██║███████╗███████╗");
			System.out.println("╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝     ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝");
			System.out.println("===============================================================================");
			System.out.println("1.전화걸기");
			System.out.println("2.연락처");
			System.out.println("3.통화기록");
			System.out.println("4.차단목록");
			System.out.println("5.전화 앱 종료");
		}
	
		public void Phone_Menu_Open() {
		Scanner input = new Scanner(System.in);

		boolean end_flag = false;

		while (true) {
			Phone_Menu();
			System.out.print("메뉴번호를 선택해 주세요 : ");
			int number = input.nextInt();

			if (number < 1 || number > 5) {
				System.out.println("메뉴번호가 틀렸습니다.다시 선택해 주세요.");
				continue;
			}
			switch (number) {
			case 1:
				call();
				break;
			case 2:
				contacts();
				break;
			case 3:
				callList();
				break;
			case 4:
				blockList();
			case 5:
				end_flag = true;
				break;       // 전화 앱 종료
			default:
			}
			if (end_flag) {
				break;
			}
			
    
     
    
     }
	}
	public void call() {
		Phone_DBManager manager = new Phone_DBManager();
		Phone_Call phone_call= new Phone_Call();
		manager.initDBConnect();
		
		Scanner input=new Scanner(System.in);
		
		System.out.println("1\t2\t3");
		System.out.println();
		System.out.println("4\t5\t6");
		System.out.println();
		System.out.println("7\t8\t9");
		System.out.println();
		System.out.println("*\t0\t#");
		
		System.out.print("번호: ");
		String number=input.nextLine();
		if(number.length() == 11 || number.length() == 10) {
			manager.call(number);	
		}
		else {
			System.out.println("전화번호를 잘못 입력했습니다.");
		}
		
		// manager.showCallHistory();
		
//		Phone_DBManager manager = new Phone_DBManager();
//		Phone_Call phone_call= new Phone_Call();
//		manager.initDBConnect();
//		
//		Scanner input=new Scanner(System.in);
//		
//		System.out.println("1\t2\t3");
//		System.out.println();
//		System.out.println("4\t5\t6");
//		System.out.println();
//		System.out.println("7\t8\t9");
//		System.out.println();
//		System.out.println("*\t0\t#");
//		
//		System.out.print("번호: ");
//		String number=input.nextLine();
//		
//		phone_call.Phone_call(number);
//		
//		//showCallHistory();
	}
	
	public void contacts() {
		System.out.println("주소록");
		Phone_DBManager manager = new Phone_DBManager();
		manager.getAllUser();
		System.out.println("어떤 작업을 선택하시겠습니까?");
		System.out.println("1.연락처 추가 2.연락처 삭제 3.뒤로 가기");
		Scanner input = new Scanner(System.in);
		int select_no = input.nextInt();
		switch(select_no) {
		case 1: 
			manager.insert_user_list();
		case 2:
			manager.delete_user_list();
		default :
			break;
		}
		
		
		
	}
	
	public void callList() {
		Phone_DBManager manager = new Phone_DBManager();
		manager.initDBConnect();
		manager.showCallHistory();
		

	}
	
	public void blockList() {
			Scanner input = new Scanner(System.in);

			System.out.println("============================");
			System.out.println("차단목록리스트");
			System.out.println("");

			Phone_DBManager manager = new Phone_DBManager();
			manager.initDBConnect();
			manager.allFetch();
			Phone_DBManager[] blockedList = manager.getBlockedList();
			for (int i = 0; i < blockedList.length; i++) {
				System.out.println(blockedList[i].getPhone_number() + "\t" +blockedList[i].getUser_name());
			} // 차단리스트 불러오기

			System.out.println();
			System.out.println("============================");
			System.out.println();
			System.out.println("1.추가하기");
			System.out.println("2.해제하기");
			System.out.println("3.뒤로가기");

			boolean end_flag = false;

			while (true) {
				System.out.print("메뉴번호를 선택해 주세요 : ");
				int number = input.nextInt();

				if (number < 1 || number > 3) {
					System.out.println("메뉴번호가 틀렸습니다.다시 선택해 주세요.");
					continue;
				}
				switch (number) {
				case 1:
					addBlock();
					break;
				case 2:
					unblock(blockedList);
					break;
				case 3:
					end_flag = true;
					break;
				}
				if (end_flag) {
					break;
				}
			}
		}
		

		public static void addBlock() {
			Phone_DBManager manager = new Phone_DBManager();
			System.out.println("차단하실 전화번호를 입력해주세요.");
			Scanner input = new Scanner(System.in);
			String phnumber = input.nextLine();

			if (manager.selectNumber(phnumber)) {
				System.out.println("이미 차단된 전화번호입니다.");
			} else {
				manager.inputNumber(phnumber);
				System.out.println("해당 번호가 차단되었습니다.");
			}

		}

		public static void unblock(Phone_DBManager[] blockedList) {
			Phone_DBManager manager = new Phone_DBManager();
			//Phone_DBManager[] blockedList = manager.getBlockedList();

			System.out.println("어느 번호를 차단해제하시겠습니까? 차단해제하실 번호를 입력하세요.");

			while (true) {
				Scanner input = new Scanner(System.in);
				String phnumber = input.nextLine();
				boolean end_flag = false;

				if (manager.selectNumber(phnumber)) {
					for (int i = 0; i < blockedList.length; i++) {
						if (blockedList[i].getPhonenumber() == phnumber) {
							for (int j = i; j < blockedList.length - 1; j++) {
								blockedList[j] = blockedList[j + 1];
							}
							blockedList[blockedList.length - 1] = null;
							break;
						}
					}
					end_flag = true;
					manager.outputNumber(phnumber);
					System.out.println("해당 번호가 차단해제 되었습니다.");
				} else {
					System.out.println("없는 번호입니다. 다시입력해주세요.");
					continue;
				}

				if (end_flag) {
					break;
				}
			}

		}
	

	
	
	public void callAppEnd() {
		System.out.println("통화앱을 종료합니다");
	}

	

	

	public static String getPhonenumber() {
		return p_phone_number;
	}

	public void setPhonenumber(String phonenumber) {
		this.p_phone_number = phonenumber;
	}


}



