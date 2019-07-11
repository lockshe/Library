package ynu.entity;

import java.sql.Timestamp;

public class Category {
	
	private int id;

    /**
     * ·ÖÀàÃû³Æ
     */
    private String name;

    private Timestamp createTime;

    public Category(String name) {
        this.name = name;
    }

    public Category(int id, String name, Timestamp createTime) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
    }
    
    @Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", createTime=" + createTime + "]";
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
