package org.spring.openapi.schema.generator.plugin;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.spring.openapi.schema.generator.OpenAPIGenerator;

import io.swagger.v3.oas.models.OpenAPI;

import static java.util.Arrays.asList;

@Mojo(name = "generateOpenApi", defaultPhase = LifecyclePhase.INSTALL)
public class GenerateOpenApiSchemaMojo extends AbstractMojo {

	@Parameter
	private String[] modelPackages;

	@Parameter
	private String[] controllerBasePackages;

	@Parameter
	private String outputDirectory;

	public void execute() {
		OpenAPIGenerator openApiGenerator = new OpenAPIGenerator(asList(modelPackages), asList(controllerBasePackages));
		OpenAPI openAPI = openApiGenerator.generate();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		try {
			if (!new File(outputDirectory).mkdirs()) {
				getLog().error(String.format("Error creating directories for path [%s]", outputDirectory));
				return;
			}
			objectMapper.writeValue(new File(outputDirectory + "/swagger.json"), openAPI);
		} catch (IOException e) {
			getLog().error("Cannot serialize generated OpenAPI spec", e);
		}
	}

}
