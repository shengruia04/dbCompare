/*
 * Copyright 2012-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.db.mongo.domain;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Supplier;

public class MongoEntityScanPackages {

	private static final String BEAN = MongoEntityScanPackages.class.getName();

	private static final MongoEntityScanPackages NONE = new MongoEntityScanPackages();

	private final List<String> packageNames;

	MongoEntityScanPackages(String... packageNames) {
		List<String> packages = new ArrayList<>();
		for (String name : packageNames) {
			if (StringUtils.hasText(name)) {
				packages.add(name);
			}
		}
		this.packageNames = Collections.unmodifiableList(packages);
	}

	public List<String> getPackageNames() {
		return this.packageNames;
	}

	public static MongoEntityScanPackages get(BeanFactory beanFactory) {
		// Currently we only store a single base package, but we return a list to
		// allow this to change in the future if needed
		try {
			return beanFactory.getBean(BEAN, MongoEntityScanPackages.class);
		}
		catch (NoSuchBeanDefinitionException ex) {
			return NONE;
		}
	}

	public static void register(BeanDefinitionRegistry registry, String... packageNames) {
		Assert.notNull(registry, "Registry must not be null");
		Assert.notNull(packageNames, "PackageNames must not be null");
		register(registry, Arrays.asList(packageNames));
	}

	public static void register(BeanDefinitionRegistry registry, Collection<String> packageNames) {
		Assert.notNull(registry, "Registry must not be null");
		Assert.notNull(packageNames, "PackageNames must not be null");
		if (registry.containsBeanDefinition(BEAN)) {
			EntityScanPackagesBeanDefinition beanDefinition = (EntityScanPackagesBeanDefinition) registry
					.getBeanDefinition(BEAN);
			beanDefinition.addPackageNames(packageNames);
		}
		else {
			registry.registerBeanDefinition(BEAN, new EntityScanPackagesBeanDefinition(packageNames));
		}
	}

	/**
	 * {@link ImportBeanDefinitionRegistrar} to store the base package from the importing
	 * configuration.
	 */
	static class Registrar implements ImportBeanDefinitionRegistrar {

		private final Environment environment;

		Registrar(Environment environment) {
			this.environment = environment;
		}

		@Override
		public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
			register(registry, getPackagesToScan(metadata));
		}

		private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
			AnnotationAttributes attributes = AnnotationAttributes
					.fromMap(metadata.getAnnotationAttributes(MongoEntityScan.class.getName()));
			Set<String> packagesToScan = new LinkedHashSet<>();
			for (String basePackage : attributes.getStringArray("basePackages")) {
				addResolvedPackage(basePackage, packagesToScan);
			}
			for (Class<?> basePackageClass : attributes.getClassArray("basePackageClasses")) {
				addResolvedPackage(ClassUtils.getPackageName(basePackageClass), packagesToScan);
			}
			if (packagesToScan.isEmpty()) {
				String packageName = ClassUtils.getPackageName(metadata.getClassName());
				Assert.state(StringUtils.hasLength(packageName), "@EntityScan cannot be used with the default package");
				return Collections.singleton(packageName);
			}
			return packagesToScan;
		}

		private void addResolvedPackage(String packageName, Set<String> packagesToScan) {
			packagesToScan.add(this.environment.resolvePlaceholders(packageName));
		}

	}

	static class EntityScanPackagesBeanDefinition extends GenericBeanDefinition {

		private final Set<String> packageNames = new LinkedHashSet<>();

		EntityScanPackagesBeanDefinition(Collection<String> packageNames) {
			setBeanClass(MongoEntityScanPackages.class);
			setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			addPackageNames(packageNames);
		}

		@Override
		public Supplier<?> getInstanceSupplier() {
			return () -> new MongoEntityScanPackages(StringUtils.toStringArray(this.packageNames));
		}

		private void addPackageNames(Collection<String> additionalPackageNames) {
			this.packageNames.addAll(additionalPackageNames);
		}

	}

}
