package phone_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Phone_DBManager {
	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String url = "jdbc:mysql://127.0.0.1:3306/phoneDB?severTimeZone=UTC";
	private static String id = "root";
	private static String pw = "1234";
	private static String p_phone_number;

	public static Connection conn = null;
	public static Statement stmt = null;
	static Phone_DBManager[] userList=new Phone_DBManager[5];
	public Phone_DBManager[] blockedList = null;

	static int callCount=0;
	static Phone_Manager manager = new Phone_Manager();
	private int history_no;
	private int user_id;
	private String user_name;
	private String phone_number;
	private LocalDateTime call_date;
	boolean insert_flag = true;
	boolean delete_flag = true;
	//private BlockedPerson[] blockedList=null;
	Phone_DBManager[] getBlockedList() {
		return blockedList;
	}
	public Phone_DBManager(String phone_number, LocalDateTime call_date) {
		this.phone_number=phone_number;
		this.call_date=call_date;
	}

	public Phone_DBManager(String number) {
		this.phone_number=number;
	}
	public Phone_DBManager(String number,String name) {
		this.phone_number=number;
		this.user_name = name;
	}

	public int getHistory_no() {
		return history_no;
	}

	public void setHistory_no(int history_no) {
		this.history_no = history_no;
	}

	public String getPhone_number() {
		return phone_number;
	}
	public String getUser_name() {
		return user_name;
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
	public static String getPhonenumber() {
		return p_phone_number;
	}
	
	public static void initDBConnect() { // db 연동
		try {
			Class.forName(driver); // driver을 메모리에 로드한다. (driver: 클래스이다.)
			conn = DriverManager.getConnection(url, id, pw); // getConnection: 커넥션 객체를 만들어줌
			stmt = conn.createStatement(); // 연결객체를 통해서 명령객체가 만들어져서 stmt에 넣는다.

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
		public Phone_DBManager() {
			
		}
		public static boolean selectNumber(String number) { // 작성된 번호가 Phone_Block에 있다면 true를 리턴하는 메서드
			 String sql="select * from Phone_Block where p_phone_number=?";
			 try {	 
				 PreparedStatement pstmt=conn.prepareStatement(sql);
				 pstmt.setString(1, number);
				 ResultSet rs=pstmt.executeQuery();
				 
				 if(rs.next()) {
					 return true;

				 }
				 rs.close();
				 
			 }catch(SQLException e) {
				 e.printStackTrace();
			 }
			 return false;
		 }
	

//		public static void main(String[] args) {
//			
//			initDBConnect();
//			
//			Scanner input=new Scanner(System.in);
//			
//			System.out.println("1\t2\t3");
//			System.out.println();
//			System.out.println("4\t5\t6");
//			System.out.println();
//			System.out.println("7\t8\t9");
//			System.out.println();
//			System.out.println("*\t0\t#");
//			
//			System.out.print("번호: ");
//			String number=input.nextLine();
//			
//			call(number);
//			
//			showCallHistory();
//		}
//		
		public static void call(String number) {
			Phone_DBManager[] oneUser=selectOneCallHistory(number); // 폰번호로 조회한 유저정보를 객체배열에 넣는다.
			LocalDateTime nowDateTime = LocalDateTime.now();
			
			if(!selectNumber(number)) {
				System.out.println("연결 성공");
				inputCallUser(new Phone_DBManager(number, nowDateTime));
			} else {
				System.out.println("차단된 번호입니다..");
			}
			
		}
		
		
		
		public static void inputCallUser(Phone_DBManager user) { // 통화기록 테이블에 통화정보 넣기
			String sql = "insert into Phone_CallHistory values(null, ?, ?)";

			try {
//				Phone_Manager manager = new Phone_Manager();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, user.getPhone_number());
				pstmt.setTimestamp(2, Timestamp.valueOf(user.getCall_date()));
				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		public static Phone_DBManager[] selectOneCallHistory(String phone_number) { // 번호에 대한 유저정보를 가져오는 메서드
			String sql = "select * from Phone_UserList where p_phone_number=?";
			try {
//				Phone_Manager manager = new Phone_Manager();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, phone_number);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					String number = rs.getString("p_phone_number");
					
					userList[callCount++]=new Phone_DBManager(number);
				}
				rs.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return userList;
		}
		
		public static void showCallHistory() {
			String sql = "select c.p_history_no as id, u.p_user_name as name, c.p_phone_number as number, c.p_call_date as date from Phone_CallHistory c inner join phone_userlist u where c.p_phone_number=u.p_phone_number order by id desc;";
			
			try {
//				Phone_Manager manager = new Phone_Manager();
				ResultSet rs = stmt.executeQuery(sql);

				System.out.println("no\t이름\t전화번호\t발신날짜");
				
				while (rs.next()) { // rs.next(): rs 레코드 하나를 본인이 가지고 있다.
					String id = rs.getString("id");
					String name = rs.getString("name");
					String number = rs.getString("number");
					Timestamp callDate = rs.getTimestamp("date");

					System.out.println(id+"\t"+name+"\t"+number+"\t"+callDate);
				}
				rs.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		public void getAllUser() {
			initDBConnect();
			

			// 문자열 변수에 쿼리를 저장
			String sql = "select * from phone_userlist";

			try {
				
				// 명령 객체의 쿼리실행 메소드를 이용하고, 매개변수는 위에서 문자열 변수에 저장한 쿼리이다.
				// 그렇게 가져온 테이블 객체를 rs 변수로 이어준다.
				ResultSet rs = stmt.executeQuery(sql);

				// rs 변수에 테이블 객체가 있으면 true 반환
				while (rs.next()) {

					// 가져온 테이블 객체의 레코드 1개에서 get---으로 값을 하나씩 가져온다
					// 매개변수는 해당 테이블의 필드(컬럼)이다.
					String user_name = rs.getString("p_user_name");
					String phone_number = rs.getString("p_phone_number");
					System.out.println("이름: " + user_name + " 전화번호: " + phone_number);
				}
				// 다 사용하면 닫아준다.
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 현재 테이블에 유저 정보를 insert 하는 메소드이다.
		public void inputUser(String phone,String user,String block) {
			String sql = "insert into phone_userlist values(?,?,?)";
			try {
				Phone_Manager manager = new Phone_Manager();
				// 동적쿼리랑 비슷하다
				PreparedStatement pstmt = this.conn.prepareStatement(sql);
				pstmt.setString(1, phone);
				pstmt.setString(2, user);
				pstmt.setString(3, block);
				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 테이블에 있는 유저 정보를 삭제할 메소드이다.
		public void deleteUser(String phone_number) {

			// where에서 이름과 전화번호를 조건으로 확실히 필터링 후에 삭제하는 쿼리이다.
			String sql = "delete from phone_userlist where p_phone_number =?";
			try {

				// 동적쿼리랑 비슷하다. 우선 쿼리를 가져온다.
				PreparedStatement pstmt = this.conn.prepareStatement(sql);
				// 해당 쿼리 ?를 순서에 맞게 채워준다.
				pstmt.setString(1, phone_number);
				pstmt.execute();
//				ResultSet rs = pstmt.executeQuery();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		// db를 다 사용하면 연결객체와 명령객체를 닫아준다.
		public void releaseDB() {
			try {
				this.conn.close();
				this.stmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 현재 테이블의 행 개수를 알려주는 메소드이다.
		public int recordCount() {
			String sql = "select count(*) as cnt from phone_userlist";
			int recount = 0;
			try {
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					recount = rs.getInt("cnt");
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return recount;
		}
		public void insert_user_list() {
			Scanner sc = new Scanner(System.in);
			while (insert_flag) {

				System.out.println("이름과 전화번호를 추가해주세요");
				System.out.print("이름: ");
				String user_name = sc.nextLine();
				System.out.print("전화번호: ");
				String user_phone = sc.nextLine();
				if(user_phone.length() == 11 || user_phone.length() == 10) {
				this.inputUser(user_phone, user_name, "N");
				}
				else {
					System.out.println("잘못된 전화번호입니다.");
					continue;
				}

				try {
					System.out.println("계속 추가하시겠습니까?? Y|N");
					String yn = sc.nextLine();
					if (yn.toLowerCase().equals("n") || !yn.toLowerCase().equals("y")) {
						System.out.println("주소록 저장을 종료합니다.");
						insert_flag = false;
						break;
					}
				} catch (Exception e) {
					e.getMessage();
				}

			}
		}

		public void delete_user_list() {
			Scanner sc = new Scanner(System.in);
			while (delete_flag) {
				System.out.println("삭제할 전화번호를 추가해주세요");
				System.out.println("전화번호: ");
				String user_phone = sc.nextLine();
				if(user_phone.length() == 11 || user_phone.length() == 10) {
				this.deleteUser(user_phone);
				
				System.out.println("계속 삭제하시겠습니까?? Y|N");
				String yn = sc.nextLine();
				if (yn.toLowerCase().equals("n") || !yn.toLowerCase().equals("y")) {
					System.out.println("주소록 삭제를 종료합니다");
					delete_flag = false;
					break;
				}
				}
				else {
					System.out.println("잘못된 전화번호입니다.");
					continue;
				}
			}
		}
		public int block_recordCount() {
			 String sql = "select count(*) as cnt from Phone_Block";
			 int recount=0;
			 try {
				 ResultSet rs=stmt.executeQuery(sql);
				 if(rs.next()) {
					 recount=rs.getInt("cnt");
				 }
				 rs.close();
			 }catch(SQLException e){
				 e.printStackTrace();
			 }
			 return recount;
		 }
		
		public void allFetch() {
			 int recount=this.block_recordCount();
			 blockedList=new Phone_DBManager[recount];
			 int numberCount=0;
			 String sql="select a.p_phone_number,ifnull(b.p_user_name,'알 수 없는 번호') as p_user_name from Phone_Block a left join Phone_userlist b on a.p_phone_number = b.p_phone_number;";
			 try {
				 ResultSet rs=stmt.executeQuery(sql);
				 while(rs.next()) {
					 String phonenumber=rs.getString("p_phone_number");
					 String pusername = rs.getString("p_user_name");
					 blockedList[numberCount++]=new Phone_DBManager(phonenumber,pusername);
					 //blockedList[numberCount++]=new Phone_DBManager(phonenumber);
				 }
				 rs.close();
			 }catch(SQLException e){
				 e.printStackTrace();
			 }		 
		 }
		
		
		
		
		
		 public void inputNumber(String number) {
			 String sql="insert into phone_block values(?)";
			 try {
				 PreparedStatement pstmt=this.conn.prepareStatement(sql);
				 pstmt.setString(1,number);
				 pstmt.executeUpdate();
				 
			 }catch(SQLException e) {
				 e.printStackTrace();
			 }
			 String sql2 = "update phone_userlist set p_block_yn = 'Y' where p_phone_number = ?";
			 try {
				 PreparedStatement pstmt=this.conn.prepareStatement(sql2);
				 pstmt.setString(1,number);
				 pstmt.executeUpdate();
				 
			 }catch(SQLException e) {
				 e.printStackTrace();
			 }
		 }
		
		 
		 public void outputNumber(String number) {
			 String sql="delete from phone_block where p_phone_number = ?";
			 try {
				 PreparedStatement pstmt=this.conn.prepareStatement(sql);
				 pstmt.setString(1,number);
				 pstmt.executeUpdate();
				 
			 }catch(SQLException e) {
				 e.printStackTrace();
			 }
			 String sql2 = "update phone_userlist set p_block_yn = 'N' where p_phone_number = ?";
			 try {
				 PreparedStatement pstmt=this.conn.prepareStatement(sql2);
				 pstmt.setString(1,number);
				 pstmt.executeUpdate();
				 
			 }catch(SQLException e) {
				 e.printStackTrace();
			 }
		 }
		 
		
		

		// 테이블에 있는 유저 정보를 삭제할 메소드이다.
		
}
