package kr.seok;

import kr.seok.hospital.domain.enums.HospitalCategory;
import kr.seok.hospital.domain.enums.factory.EnumMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
