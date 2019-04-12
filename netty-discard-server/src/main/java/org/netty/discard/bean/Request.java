package org.netty.discard.bean;

import java.io.Serializable;

public class Request implements Serializable{
	private static final long serialVersionUID = -5016598832288672843L;
	private String fileName;
	private String fileSuffix;
	private Integer id;
	private String name;
	private String reqeustMessag;
	private byte[] Attachment;

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReqeustMessag() {
		return reqeustMessag;
	}

	public void setReqeustMessag(String reqeustMessag) {
		this.reqeustMessag = reqeustMessag;
	}

	public byte[] getAttachment() {
		return Attachment;
	}

	public void setAttachment(byte[] attachment) {
		Attachment = attachment;
	}
	

}
