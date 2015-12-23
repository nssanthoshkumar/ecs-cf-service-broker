package com.emc.ecs.serviceBroker.config;

import java.net.URL;

import org.cloudfoundry.community.servicebroker.model.BrokerApiVersion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.emc.ecs.managementClient.Connection;
import com.emc.ecs.serviceBroker.repository.EcsRepositoryCredentials;

@Configuration
@ComponentScan(basePackages = "com.emc.ecs.serviceBroker")
public class BrokerConfig {
	
	// TODO support multiple Cloud Foundry instances per http://docs.cloudfoundry.org/services/supporting-multiple-cf-instances.html
	// TODO support syslog drain URL
	
	@Value("${broker-config.managementEndpoint}")
	private String managementEndpoint;
	
	@Value("${broker-config.namespace}")
	private String namespace;
	
	@Value("${broker-config.replicationGroup}")
	private String replicationGroup;
	
	@Value("${broker-config.repositoryUser:user}")
	private String repositoryUser;
	
	@Value("${broker-config.username:root}")
	private String username;
	
	@Value("${broker-config.password:ChangeMe}")
	private String password;
	
	@Value("${broker-config.repositoryBucket:repository}")
	private String repositoryBucket;

	@Value("${broker-config.repositoryEndpoint}")
	private String repositoryEndpoint;
	
	@Value("${broker-config.prefix:ecs-cf-broker-}")
	private String prefix;
	
	@Value("${broker-config.apiVersion:2.8}")
	private String brokerApiVersion;

	@Bean
	public Connection ecsConnection() {
		URL certificate = getClass().getClassLoader().getResource("localhost.pem");
		return new Connection(managementEndpoint, username, password, certificate);
	}
	
	@Bean
	public BrokerApiVersion brokerApiVersion() {
		return new BrokerApiVersion(brokerApiVersion);
	}
	
	@Bean
	public EcsRepositoryCredentials getRepositoryCredentials() {
		EcsRepositoryCredentials creds = new EcsRepositoryCredentials(
				repositoryBucket, repositoryUser, namespace, replicationGroup,
				prefix);
		if (repositoryEndpoint != null) creds.setEndpoint(repositoryEndpoint);
		return creds;
	}
}