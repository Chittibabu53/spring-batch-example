package com.example.spring.batch.config;

import com.example.spring.batch.entity.Customers;
import com.example.spring.batch.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringConfig {

//    @Autowired
//    private JobBuilder jobBuilder;
//
//    @Autowired
//    private StepBuilder stepBuilder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;


    @Bean
    public FlatFileItemReader<Customers> reader(){

        FlatFileItemReader<Customers>  itemReader=new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("C:\\Users\\chitt\\Downloads\\spring batch\\spring-batch\\src\\main\\resources\\customers.csv"));
        itemReader.setName("csvreader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    public LineMapper<Customers> lineMapper(){
        DefaultLineMapper<Customers> lineMapper=new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer=new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");

        BeanWrapperFieldSetMapper<Customers> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customers.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public CustomerProcessor customerProcessor(){
        return new CustomerProcessor();
    }

    @Bean
    public RepositoryItemWriter<Customers> writer(){
        RepositoryItemWriter<Customers> writer=new RepositoryItemWriter<>();
        writer.setRepository(productRepository);
        writer.setMethodName("save");
        return writer;
    }

    public Step step(){
        return new StepBuilder("csv-step",jobRepository).
                <Customers,Customers>chunk(10,transactionManager ).reader(reader()).
                processor(customerProcessor()).writer(writer()).build();
    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importCustomers", jobRepository)
                .start(step())
                .build();
    }



}
