package org.greenscape.persistence.impl;

import java.util.Map;

import org.greenscape.persistence.ModelRegistryEntry;
import org.greenscape.persistence.PersistenceConstants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

@Component(name = PersistenceConstants.ModelRegistryEntry_FACTORY_DS, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class ModelRegistryEntryImpl implements ModelRegistryEntry {

	private String modelClass;
	private String modelName;
	private long bundleId;

	private LogService logService;

	@Override
	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	@Override
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	@Override
	public long getBundleId() {
		return bundleId;
	}

	public void setBundleId(long bundleId) {
		this.bundleId = bundleId;
	}

	@Activate
	public void activate(ComponentContext ctx, Map<String, Object> properties) {
		bundleId = (Long) properties.get("bundleId");
		modelClass = (String) properties.get("modelClass");
		modelName = (String) properties.get("modelName");
		if (logService != null) {
			logService.log(LogService.LOG_DEBUG, "Component created: " + modelClass);
		}
	}

	@Modified
	public void modified(ComponentContext ctx, Map<String, Object> properties) throws ConfigurationException {
		bundleId = (Long) properties.get("bundleId");
		modelClass = (String) properties.get("modelClass");
		modelName = (String) properties.get("modelName");
		if (logService != null) {
			logService.log(LogService.LOG_DEBUG, "Component updated: " + modelClass);
		}
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public void unsetLogService(LogService logService) {
		this.logService = null;
	}

	@Override
	public String toString() {
		return modelClass;
	}
}
