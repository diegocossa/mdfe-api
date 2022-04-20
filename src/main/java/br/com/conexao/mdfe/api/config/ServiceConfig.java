package br.com.conexao.mdfe.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ComponentScan(value = {"br.com.conexao.mdfe.api.service"})
@EnableAsync
public class ServiceConfig {

}
