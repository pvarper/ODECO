package micrium.user.mail;

import org.apache.commons.lang.StringUtils;

public class Attachment {

	private String path;
	private String name;
	private String description;

	public Attachment() {
		path = StringUtils.EMPTY;
		name = StringUtils.EMPTY;
		description = StringUtils.EMPTY;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "{path:" + path + ", name:" + ", description:" + description + "}";
	}
}
