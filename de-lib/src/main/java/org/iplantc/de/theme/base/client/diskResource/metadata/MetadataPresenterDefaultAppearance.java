package org.iplantc.de.theme.base.client.diskResource.metadata;

import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;

public class MetadataPresenterDefaultAppearance implements MetadataView.Presenter.Appearance {

	private final MetadataDisplayStrings displayStrings;
	private final IplantResources iplantResources;
	private IplantDisplayStrings iplantDisplayStrings;

	public MetadataPresenterDefaultAppearance() {
		this(GWT.<MetadataDisplayStrings> create(MetadataDisplayStrings.class),
			 GWT.<IplantResources>create(IplantResources.class),
			 GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class));
	}

	public MetadataPresenterDefaultAppearance(MetadataDisplayStrings displayStrings,
											  IplantResources iplantResources,
											  IplantDisplayStrings iplantDisplayStrings) {

		this.displayStrings = displayStrings;
		this.iplantResources = iplantResources;
		this.iplantDisplayStrings = iplantDisplayStrings;
	}

	@Override
	public String templateListingError() {
		return displayStrings.templateListingError();
	}

	@Override
	public String loadMetadataError() {
		return displayStrings.loadMetadataError();
	}

	@Override
	public String saveMetadataError() {
		return displayStrings.saveMetadataError();
	}

	@Override
	public String templateinfoError() {
		return displayStrings.templateinfoError();
	}

	@Override
	public String selectTemplate() {
		return displayStrings.selectTemplate();
	}

	@Override
	public String templates() {
		return displayStrings.templates();
	}

	@Override
	public String error() {
		return displayStrings.error();
	}

	@Override
	public String incomplete() {
		return displayStrings.incomplete();
	}

	@Override
	public ImageResource info() {
		return iplantResources.info();
	}

	public SafeHtml importMdMsg() {
		return displayStrings.importMdMsg();
	}

	@Override
	public SafeHtml importMd() {
		return displayStrings.importMd();
	}

	@Override
	public String loadingMask() {
		return iplantDisplayStrings.loadingMask();
	}
}
