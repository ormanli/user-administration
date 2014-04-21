package com.serdarormanli.useradministration.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/***
 * Captcha class for captcha indormation
 * @author Serdar ORMANLI
 *
 */
@Document(collection = "captchas")
public class Captcha implements Serializable {

	private static final long serialVersionUID = 3108422268414059857L;

	@Id
	private String id;
	private String value;
	private String image;
	private Date createDate;

	public Captcha(String id, String value, String image, Date createDate) {
		this.id = id;
		this.value = value;
		this.image = image;
		this.createDate = createDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
