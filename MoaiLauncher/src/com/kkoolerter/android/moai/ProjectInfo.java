package com.kkoolerter.android.moai;

public class ProjectInfo {
	private String mVersion;
	private String mAuthor;
	private String mName;
	private String mIcon;
	private String mStartDir;
	private String mRootDir;

	public void setRootDir(String rootDir) {
		mRootDir = rootDir;
	}

	public String getRootDir() {
		return mRootDir;
	}

	public void setVersion(String version) {
		mVersion = version;
	}

	public String getVersion() {
		return mVersion;
	}

	public void setAuthor(String author) {
		mAuthor = author;
	}

	public String getAuthor() {
		return mAuthor;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	public void setIcon(String icon) {
		mIcon = icon;
	}

	public String getIcon() {
		return mIcon;
	}

	public void setStartDir(String dir) {
		mStartDir = dir;
	}

	public String getStartDir() {
		return mStartDir;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ProjectInfo = [name = ").append(mName).append(",author = ")
				.append(mAuthor).append(",icon = ").append(mIcon)
				.append(",version = ").append(mVersion).append(",startDir = ")
				.append(mStartDir).append(",rootDir = ").append(mRootDir)
				.append("]");
		return sb.toString();
	}

	public static final String KEY_AUTHOR = "app.author";
	public static final String KEY_VERSION = "app.version";
	public static final String KEY_NAME = "app.name";
	public static final String KEY_ICON = "app.icon";
	public static final String KEY_PRO_DIR = "app.start.dir";
}
