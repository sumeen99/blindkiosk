package com.blindkiosk.server;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;


import javax.sql.DataSource;

//@Profile(value="mongo")
@Configuration
@Import(EmbeddedMongoAutoConfiguration.class)
public class Config {


}
