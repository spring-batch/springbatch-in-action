package kr.seok;

import kr.seok.hospital.domain.enums.HospitalCategory;
import kr.seok.hospital.domain.enums.factory.EnumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class HospitalApplication {
    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("HospitalCate", HospitalCategory.class);
        return enumMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args).close();
    }
}
