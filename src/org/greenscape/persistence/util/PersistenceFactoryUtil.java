package org.greenscape.persistence.util;

import org.greenscape.persistence.DocumentModel;
import org.greenscape.persistence.DocumentModelBase;
import org.greenscape.persistence.PersistedModel;
import org.greenscape.persistence.PersistedModelBase;
import org.greenscape.persistence.PersistenceProvider;
import org.greenscape.persistence.PersistenceType;
import org.greenscape.persistence.impl.PersistenceProviderImpl;

public class PersistenceFactoryUtil {

	public static PersistenceProvider createPersistenceProvider(String providerName, PersistenceType type) {
		PersistenceProvider provider = new PersistenceProviderImpl(providerName, type);
		return provider;
	}

	public static DocumentModel createDocumentModel() {
		return new DocumentModelBase();
	}

	public static PersistedModel createPersistedModel() {
		return new PersistedModelBase();
	}
}
