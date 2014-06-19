package org.greenscape.persistence.impl;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.greenscape.persistence.annotations.Model;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.BundleTracker;

public class ModelBundleTracker extends BundleTracker<String> {
	private static final String GS_MODEL = "GS-Model";

	public ModelBundleTracker(BundleContext context) {
		super(context, Bundle.ACTIVE, null);
	}

	@Override
	public String addingBundle(Bundle bundle, BundleEvent event) {
		Dictionary<String, String> headers = bundle.getHeaders();
		String headerValue = headers.get(GS_MODEL);
		if (headerValue != null) {
			registerModel(bundle, headerValue);
		}
		return null;
	}

	@Override
	public void removedBundle(Bundle bundle, BundleEvent event, String object) {
		Dictionary<String, String> headers = bundle.getHeaders();
		String modelList = headers.get(GS_MODEL);
		if (modelList != null) {
			unregisterModel(bundle.getBundleId());
		}
	}

	private void registerModel(Bundle bundle, String modelList) {
		ServiceReference<ConfigurationAdmin> configAdminRef = context.getServiceReference(ConfigurationAdmin.class);

		String[] modelArray = modelList.split(",");
		if (configAdminRef != null) {
			ConfigurationAdmin confAdmin = context.getService(configAdminRef);
			Configuration[] configurations;
			try {
				configurations = confAdmin.listConfigurations("(service.factoryPid="
						+ ModelRegistryEntryImpl.FACTORY_DS + ")");
				for (String model : modelArray) {
					boolean found = false;
					model = model.trim();
					if (configurations != null) {
						for (Configuration config : configurations) {
							Dictionary<String, Object> properties = config.getProperties();
							String modelClass = (String) properties.get("modelClass");
							if (model.equals(modelClass)) {
								found = true;
								break;
							}
						}
					}
					if (!found) {
						Configuration config = confAdmin.createFactoryConfiguration(ModelRegistryEntryImpl.FACTORY_DS);
						Dictionary<String, Object> properties = new Hashtable<>();
						properties.put("modelClass", model);
						Class<?> cls = bundle.loadClass(model);
						properties.put("modelName", cls.getAnnotation(Model.class).name());
						properties.put("bundleId", bundle.getBundleId());
						config.update(properties);
					}
				}
			} catch (IOException | InvalidSyntaxException | ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void unregisterModel(long bundleId) {
		ServiceReference<ConfigurationAdmin> configAdminRef = context.getServiceReference(ConfigurationAdmin.class);
		Configuration[] configurations;
		if (configAdminRef != null) {
			ConfigurationAdmin confAdmin = context.getService(configAdminRef);
			try {
				configurations = confAdmin.listConfigurations("(factoryPid=" + ModelRegistryEntryImpl.FACTORY_DS + ")");
				for (Configuration config : configurations) {
					Dictionary<String, Object> properties = config.getProperties();
					long bid = (long) properties.get("bundleId");
					if (bundleId == bid) {
						config.delete();
					}
				}
			} catch (IOException | InvalidSyntaxException e1) {
				e1.printStackTrace();
			}
		}
	}
}
