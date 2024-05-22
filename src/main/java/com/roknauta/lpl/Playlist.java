package com.roknauta.lpl;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Playlist{

	@JsonProperty("left_thumbnail_mode")
	private int leftThumbnailMode;

	@JsonProperty("sort_mode")
	private int sortMode;

	@JsonProperty("default_core_name")
	private String defaultCoreName;

	@JsonProperty("default_core_path")
	private String defaultCorePath;

	@JsonProperty("thumbnail_match_mode")
	private int thumbnailMatchMode;

	@JsonProperty("version")
	private String version;

	private List<ItemsItem> items;

	@JsonProperty("label_display_mode")
	private int labelDisplayMode;

	@JsonProperty("right_thumbnail_mode")
	private int rightThumbnailMode;
}
