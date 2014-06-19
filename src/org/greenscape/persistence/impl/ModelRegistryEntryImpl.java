package org.greenscape.persistence.impl;

import java.util.Dictionary;
import java.util.Map;

import org.greenscape.persistence.ModelRegistryEntry;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

@Component(name = ModelRegistryEntryImpl.FACTORY_DS, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class ModelRegistryEntryImpl implements ModelRegistryEntry, ManagedService {
	static final String FACTORY_DS = "org.greenscape.persistence.ModelRegistryEntry.factory";

	private String modelClass;
	private String modelName;
	private long bundleId;

	private LogService logService;

	@Override
	public String getModelClass() {
		return modelClass;
	}

	@Override
	public String getModelName() {
		return modelName;
	}

	@Override
	public long getBundleId() {
		return bundleId;
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		bundleId = (Long) properties.get("bundleId");
		modelClass = (String) properties.get("modelClass");
		modelName = (String) properties.get("modelName");
		if (logService != null) {
			logService.log(LogService.LOG_DEBUG, "Component updated: " + modelClass);
		}
	}

	@SuppressWarnings("unchecked")
	@Activate
	public void activate(ComponentContext ctx, Map<String, Object> config) {
		ctx.getProperties().put("modelClass", modelClass);
		if (logService != null) {
			logService.log(LogService.LOG_DEBUG, "Component created: " + modelClass);
		}
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public void unsetLogService(LogService logService) {
		this.logService = null;
	}
}
