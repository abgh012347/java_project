package phone_app;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Phone_Call {
	private int history_no;
	private int user_id;
	private String phone_number;
	private LocalDateTime call_date;
	
	public void Phone_Call_History(String phone_number, LocalDateTime call_date) {
		this.phone_number=phone_number;
		this.call_date=call_date;
	}

	public void Phone_Call_History(String number) {
		this.phone_number=number;
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

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public LocalDateTime getCall_date() {
		return call_date;
	}

	public void setCall_date(LocalDateTime call_date) {
		this.call_date = call_date;
	}
	public Phone_Call() {
		// TODO Auto-generated constructor stub
	}
//	public static void Phone_call(String number) {
//		Phone_DBManager manager = new Phone_DBManager();
//		Phone_DBManager[] oneUser=manager.selectOneCallHistory(number); // 폰번호로 조회한 유저정보를 객체배열에 넣는다.
//		LocalDateTime nowDateTime = LocalDateTime.now();
//		
//		
//		if(!manager.selectNumber(number)) {
//			System.out.println("연결 성공");
//			manager.inputCallUser(new Phone_DBManager(oneUser[0].getPhone_number(), nowDateTime));
//		} else {
//			System.out.println("차단된 번호입니다.");
//		}
//		
//	}
	
	
	
	
}
