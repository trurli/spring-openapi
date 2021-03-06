package com.github.jrcodeza.schema.generator.interceptors;

import io.swagger.v3.oas.models.media.Schema;

public interface SchemaInterceptor {

	void intercept(Class<?> clazz, Schema<?> transformedSchema);

}
