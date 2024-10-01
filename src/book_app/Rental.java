package book_app;

import java.time.LocalDateTime;

public class Rental {

	private String userid;
	private int bookno;
	private LocalDateTime rental_date;
	private String return_yn;

	public Rental() {
		// TODO Auto-generated constructor stub
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getBookno() {
		return bookno;
	}

	public void setBookno(int bookno) {
		this.bookno = bookno;
	}

	public LocalDateTime getRental_date() {
		return rental_date;
	}

	public void setRental_date(LocalDateTime rental_date) {
		this.rental_date = rental_date;
	}

	public String getReturn_yn() {
		return return_yn;
	}

	public void setReturn_yn(String return_yn) {
		this.return_yn = return_yn;
	}

}
