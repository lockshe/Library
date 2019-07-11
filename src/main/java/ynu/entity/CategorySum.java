package ynu.entity;

public class CategorySum {

	private int id;
	
	private String name;
	
	private long sum;
	
	public CategorySum(int id,String name,long sum) {
		this.id=id;
		this.name=name;
		this.sum=sum;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getSum() {
		return sum;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	@Override
	public String toString() {
		return "CategorySum [id=" + id + ", name=" + name + ", sum=" + sum + "]";
	}

}
