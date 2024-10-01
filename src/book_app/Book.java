package book_app;

public class Book {

	private int bookno;
	private String bookname;
	private String remark;
	private String publisher;
	private int bookcount;
	private String category;

	public Book(int bookno, String bookname, String remark, String publisher, int bookcount, String category) {
		this.bookno = bookno;
		this.bookname = bookname;
		this.remark = remark;
		this.publisher = publisher;
		this.bookcount = bookcount;
		this.category = category;
	}

	public int getBookno() {
		return bookno;
	}

	public void setBookno(int bookno) {
		this.bookno = bookno;
	}

	public String getBookname() {
		return bookname;
	}

	public void setBookname(String bookname) {
		this.bookname = bookname;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getBookcount() {
		return bookcount;
	}

	public void setBookcount(int bookcount) {
		this.bookcount = bookcount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
