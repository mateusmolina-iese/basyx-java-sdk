/*******************************************************************************
 * Copyright (C) 2021 the Eclipse BaSyx Authors
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.basyx.extensions.aas.registration.mqtt;

import org.eclipse.basyx.aas.registration.observing.IAASRegistryServiceObserver;
import org.eclipse.basyx.extensions.shared.mqtt.MqttEventService;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation variant for the AASRegistryServiceObserver that triggers MQTT events for
 * different operations on the registry. 
 * 
 * @author haque
 *
 */
public class MqttAASRegistryServiceObserver extends MqttEventService implements IAASRegistryServiceObserver {
	private static Logger logger = LoggerFactory.getLogger(MqttAASRegistryServiceObserver.class);

	// List of topics
	public static final String TOPIC_REGISTERAAS = "BaSyxRegistry_registeredAAS";
	public static final String TOPIC_REGISTERSUBMODEL = "BaSyxRegistry_registeredSubmodel";
	public static final String TOPIC_DELETEAAS = "BaSyxRegistry_deletedAAS";
	public static final String TOPIC_DELETESUBMODEL = "BaSyxRegistry_deletedSubmodel";

	/**
	 * Constructor for adding this MQTT extension as an AAS Registry Observer
	 *  
	 * @param serverEndpoint endpoint of mqtt broker
	 * @param clientId unique client identifier
	 * @throws MqttException
	 */
	public MqttAASRegistryServiceObserver(String serverEndpoint, String clientId) throws MqttException {
		super(serverEndpoint, clientId);
		logger.info("Create new MQTT AAS Registry Service Observer for endpoint " + serverEndpoint);
	}

	/**
	 * Constructor for adding this MQTT extension as an AAS Registry Observer with a
	 * custom mqtt client persistence
	 * 
	 * @param serverEndpoint endpoint of mqtt broker
	 * @param clientId unique client identifier
	 * @param clientId unique client identifier
	 * @param mqttPersistence custom mqtt persistence strategy
	 * @throws MqttException
	 */
	public MqttAASRegistryServiceObserver(String serverEndpoint, String clientId, MqttClientPersistence mqttPersistence) throws MqttException {
		super(serverEndpoint, clientId, mqttPersistence);
		logger.info("Create new MQTT AAS Registry Service Observer for endpoint " + serverEndpoint);
	}

	/**
	 * Constructor for adding this MQTT extension as an AAS Registry Observer
	 *  
	 * @param serverEndpoint endpoint of mqtt broker
	 * @param clientId unique client identifier
	 * @param user username for authentication with broker
	 * @param pw password for authentication with broker
	 * @throws MqttException
	 */
	public MqttAASRegistryServiceObserver(String serverEndpoint, String clientId, String user, char[] pw)
			throws MqttException {
		super(serverEndpoint, clientId, user, pw);
		logger.info("Create new MQTT AAS Registry Service Observer for endpoint " + serverEndpoint);
	}
	
	/**
	 * Constructor for adding this MQTT extension as an AAS Registry Observer
	 *  
	 * @param client already configured client
	 * @throws MqttException
	 */
	public MqttAASRegistryServiceObserver(MqttClient client) throws MqttException {
		super(client);
		logger.info("Create new MQTT AAS Registry Service Observer for endpoint " + client.getServerURI());
	}

	@Override
	public void aasRegistered(String aasId) {
		sendMqttMessage(TOPIC_REGISTERAAS, aasId);
	}

	@Override
	public void submodelRegistered(IIdentifier aasId, IIdentifier smId) {
		sendMqttMessage(TOPIC_REGISTERSUBMODEL, concatAasSmId(aasId, smId));
	}

	@Override
	public void aasDeleted(String aasId) {
		sendMqttMessage(TOPIC_DELETEAAS, aasId);
	}

	@Override
	public void submodelDeleted(IIdentifier aasId, IIdentifier smId) {
		sendMqttMessage(TOPIC_DELETESUBMODEL, concatAasSmId(aasId, smId));
	}
	
	public static String concatAasSmId(IIdentifier aasId, IIdentifier smId) {
		return "(" + aasId.getId() + "," + smId.getId() + ")";
	}
}