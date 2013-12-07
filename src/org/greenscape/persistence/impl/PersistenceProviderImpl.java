package org.greenscape.persistence.impl;

import org.greenscape.persistence.PersistenceProvider;
import org.greenscape.persistence.PersistenceType;

public class PersistenceProviderImpl implements PersistenceProvider {

	private String providerName;
	private PersistenceType type;

	public PersistenceProviderImpl() {
	}

	public PersistenceProviderImpl(String providerName, PersistenceType type) {
		this.providerName = providerName;
		this.type = type;
	}

	@Override
	public String getName() {
		return providerName;
	}

	public PersistenceProviderImpl setName(String providerName) {
		this.providerName = providerName;
		return this;
	}

	@Override
	public PersistenceType getType() {
		return type;
	}

	public PersistenceProviderImpl setType(PersistenceType type) {
		this.type = type;
		return this;
	}

}
