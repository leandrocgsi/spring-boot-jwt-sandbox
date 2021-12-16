package br.com.erudio.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("_embedded")
	private EmbeddedVO embedded;

	public WrapperVO() {}

	public EmbeddedVO getEmbedded() {
		return embedded;
	}

	public void setEmbedded(EmbeddedVO embedded) {
		this.embedded = embedded;
	}
}
