package com.ciscospark;

import com.ciscospark.WebhookEvent;

public interface WebhookEventListener {
	public void onEvent(WebhookEvent event);
}
