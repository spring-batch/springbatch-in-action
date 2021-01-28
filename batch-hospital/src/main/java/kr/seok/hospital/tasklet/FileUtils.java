package kr.seok.hospital.tasklet;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class FileUtils {

    private File file;

    /* 파일 가져오기 */
    public void getFile(ClassPathResource classPathResource) {
        try {
            file = new File(classPathResource.getURI());

            if(!file.exists()) throw new FileNotFoundException("파일이 존재하지 않습니다.");
            if(!file.canRead()) throw new Exception("읽을 수 없는 파일 입니다.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 파일 읽기 */
    public List<String> readFileLine() throws IOException {
        List<String> tmp = new ArrayList<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), UTF_8));
        String line = "";
        int i = 0;

        while((line = br.readLine()) != null) {
            if(!(i == 0)) {
                tmp.add(line);
            }
            i++;
        }
        return tmp;
    }
}
