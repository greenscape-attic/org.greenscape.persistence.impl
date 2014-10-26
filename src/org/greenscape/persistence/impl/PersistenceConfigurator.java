package org.greenscape.persistence.impl;

import java.util.Map;

import org.greenscape.core.ResourceEvent;
import org.greenscape.core.ResourceRegistry;
import org.greenscape.core.ResourceType;
import org.greenscape.persistence.PersistenceService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

@Component(property = { EventConstants.EVENT_TOPIC + "=" + ResourceRegistry.TOPIC_RESOURCE_REGISTERED,
		EventConstants.EVENT_TOPIC + "=" + ResourceRegistry.TOPIC_RESOURCE_UNREGISTERED })
public class PersistenceConfigurator implements EventHandler {
	private PersistenceService persistenceService;

	@Override
	public void handleEvent(Event event) {
		String name = (String) event.getProperty(ResourceEvent.RESOURCE_NAME);
		String type = (String) event.getProperty(ResourceEvent.RESOURCE_TYPE);
		if (type.equals(ResourceType.Model.name())) {
			switch (event.getTopic()) {
			case ResourceRegistry.TOPIC_RESOURCE_REGISTERED:
				persistenceService.addModel(name);
				break;
			case ResourceRegistry.TOPIC_RESOURCE_UNREGISTERED:
				break;
			}
		}
	}

	@Activate
	private void activate(Map<String, Object> properties) {
	}

	@Reference(policy = ReferencePolicy.DYNAMIC)
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void unsetPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = null;
	}
}
