package org.open.gateway.portal.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.HibernateValidator;
import org.open.gateway.common.utils.JSON;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Created by miko on 9/28/20.
 *
 * @author MIKO
 */
@Configuration
public class MvcConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JSON.getJsonMapper();
    }

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
//                .failFast(true)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

}
