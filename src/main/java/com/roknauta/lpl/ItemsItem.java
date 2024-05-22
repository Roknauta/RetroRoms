package com.roknauta.lpl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemsItem{

	private String path;
	@JsonProperty("db_name")
	private String dbName;
	private String label;
	private String crc32;
	@JsonProperty("core_name")
	private String coreName;
	@JsonProperty("core_path")
	private String corePath;
}
