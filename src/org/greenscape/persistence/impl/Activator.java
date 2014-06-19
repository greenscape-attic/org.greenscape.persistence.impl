package org.greenscape.persistence.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private ModelBundleTracker modelTracker;

	@Override
	public void start(BundleContext context) throws Exception {
		modelTracker = new ModelBundleTracker(context);
		modelTracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		modelTracker.close();
	}

}
