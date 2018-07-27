package org.iplantc.de.admin.desktop.client.metadata.presenter.callbacks;

import jsinterop.annotations.JsFunction;

@JsFunction
public interface SaveTemplateError {
    void reject(String reason);
}
