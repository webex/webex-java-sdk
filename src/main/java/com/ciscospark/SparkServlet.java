package com.ciscospark;
import javax.servlet.*;
import javax.servlet.http.*;
import com.ciscospark.WebhookEvent;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.util.List;
import java.util.ArrayList;
import com.ciscospark.WebhookEventListener;

public class SparkServlet extends HttpServlet {

	List<WebhookEventListener> listeners = new ArrayList<WebhookEventListener>();

	public void doPost(HttpServletRequest incoming,
				HttpServletResponse outgoing)
				throws ServletException, IOException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			WebhookEvent whe = null;

			whe = mapper.readValue(incoming.getInputStream(), WebhookEvent.class);

			for (WebhookEventListener whel : listeners) {
				whel.onEvent(whe);
			}

	}

	public void addListener(WebhookEventListener whel) {
		listeners.add(whel);
	}
}
