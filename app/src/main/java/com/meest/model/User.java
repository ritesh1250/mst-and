package com.meest.model;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("displayPicture")
	private String displayPicture;

	@SerializedName("thumbnail")
	private String thumbnail;

	@SerializedName("id")
	private String id;

	@SerializedName("username")
	private String username;

	public String getDisplayPicture(){
		return displayPicture;
	}

	public String getThumbnail(){
		return thumbnail;
	}

	public String getId(){
		return id;
	}

	public String getUsername(){
		return username;
	}
}