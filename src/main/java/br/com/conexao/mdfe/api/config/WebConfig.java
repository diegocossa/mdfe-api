package br.com.conexao.mdfe.api.config;

import br.com.conexao.mdfe.api.tenant.TenancyInterceptor;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsViewResolver;

@Configuration
@EnableSpringDataWebSupport
@EnableCaching
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    HandlerInterceptor tenantInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new TenancyInterceptor()); //Interceptor do tenant da session

        registry.addInterceptor(tenantInterceptor); //Interceptor do schema para consultas e gravacoes
    }

    @Bean
    public ViewResolver jasperReportsviewResolver() {
        JasperReportsViewResolver resolver = new JasperReportsViewResolver();
        resolver.setPrefix("classpath:/documentos/");
        resolver.setSuffix(".jasper");
        resolver.setViewNames("documento_*");
        resolver.setViewClass(JasperReportsMultiFormatView.class);
        resolver.setOrder(0);
        return resolver;
    }

    @Primary
    @Bean
    public FreeMarkerConfigurationFactoryBean factoryBean() {
        FreeMarkerConfigurationFactoryBean bean=new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("classpath:/emails");
        return bean;
    }

    @Bean
    public AmazonSimpleEmailService amazonSES() {

        //credenciais do amazon SES
        AWSCredentials credenciais
                = new BasicAWSCredentials("AKIAJ6QUTQVHWWLFMYFA", "hPEw7GxVHwL98Rl4NPgnKEm2uJewNnxzCnQ7IhJi");
        AWSCredentialsProvider awsCredentialsProvider
                = new AWSStaticCredentialsProvider(credenciais);

        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(awsCredentialsProvider).withRegion(Regions.US_WEST_2).build();
    }

}
