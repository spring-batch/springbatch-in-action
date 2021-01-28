package kr.seok.hospital.tasklet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Tasklet_H_FileReaderTest {


    Tasklet_H_FileReader tasklet_h_fileReader;
    FileUtils fileUtils;

    @BeforeEach
    public void setUp() {
        tasklet_h_fileReader = new Tasklet_H_FileReader(fileUtils);
    }

    @Test
    void testCase1() throws Exception {

    }
}
