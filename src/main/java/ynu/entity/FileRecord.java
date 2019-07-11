package ynu.entity;

import java.sql.Timestamp;

public class FileRecord {

	private long id;

	private int userId;

	private String username;

	private String avatar;

	private String fileName;

	private long size;

	private String categoryName;

	private String description;

	private String tag;

	private int checkTimes;

	private int downloadTimes;

	private String visitUrl;

	private int isUploadable;

	private int isDeletable;

	private int isUpdatable;

	private int isDownloadable;

	private int isVisible;
	
	private Timestamp createTime;
	
    public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}



    public FileRecord(long id, int userId, String username, String avatar, String fileName, long size, Timestamp
            createTime, String categoryName, String description, String tag, int checkTimes, int downloadTimes,
                      String visitUrl, int isUploadable, int isDeletable, int isUpdatable, int isDownloadable, int
                              isVisible) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.avatar = avatar;
        this.fileName = fileName;
        this.size = size;
        this.createTime = createTime;
        this.categoryName = categoryName;
        this.description = description;
        this.tag = tag;
        this.checkTimes = checkTimes;
        this.downloadTimes = downloadTimes;
        this.visitUrl = visitUrl;
        this.isUploadable = isUploadable;
        this.isDeletable = isDeletable;
        this.isUpdatable = isUpdatable;
        this.isDownloadable = isDownloadable;
        this.isVisible = isVisible;
    }

	@Override
	public String toString() {
		return "FileRecord [id=" + id + ", userId=" + userId + ", username=" + username + ", avatar=" + avatar
				+ ", fileName=" + fileName + ", size=" + size + ", categoryName=" + categoryName + ", description="
				+ description + ", tag=" + tag + ", checkTimes=" + checkTimes + ", downloadTimes=" + downloadTimes
				+ ", visitUrl=" + visitUrl + ", isUploadable=" + isUploadable + ", isDeletable=" + isDeletable
				+ ", isUpdatable=" + isUpdatable + ", isDownloadable=" + isDownloadable + ", isVisible=" + isVisible
				+ "]";
	}

	public long getId() {
		return id;
	}

	public int getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public String getAvatar() {
		return avatar;
	}

	public String getFileName() {
		return fileName;
	}

	public long getSize() {
		return size;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getDescription() {
		return description;
	}

	public String getTag() {
		return tag;
	}

	public int getCheckTimes() {
		return checkTimes;
	}

	public int getDownloadTimes() {
		return downloadTimes;
	}

	public String getVisitUrl() {
		return visitUrl;
	}

	public int getIsUploadable() {
		return isUploadable;
	}

	public int getIsDeletable() {
		return isDeletable;
	}

	public int getIsUpdatable() {
		return isUpdatable;
	}

	public int getIsDownloadable() {
		return isDownloadable;
	}

	public int getIsVisible() {
		return isVisible;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setCheckTimes(int checkTimes) {
		this.checkTimes = checkTimes;
	}

	public void setDownloadTimes(int downloadTimes) {
		this.downloadTimes = downloadTimes;
	}

	public void setVisitUrl(String visitUrl) {
		this.visitUrl = visitUrl;
	}

	public void setIsUploadable(int isUploadable) {
		this.isUploadable = isUploadable;
	}

	public void setIsDeletable(int isDeletable) {
		this.isDeletable = isDeletable;
	}

	public void setIsUpdatable(int isUpdatable) {
		this.isUpdatable = isUpdatable;
	}

	public void setIsDownloadable(int isDownloadable) {
		this.isDownloadable = isDownloadable;
	}

	public void setIsVisible(int isVisible) {
		this.isVisible = isVisible;
	}

}
