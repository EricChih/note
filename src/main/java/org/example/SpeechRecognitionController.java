package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/csv")
public class SpeechRecognitionController {
    @Autowired
    private CsvServies csvServies;

    @Autowired
    private RedisMysqlService redisMysqlService;

    @Autowired
    private SqlClass sqlClass;
    @GetMapping("/export")
    public String exportCSV() {
        csvServies.exportCSV();
        return null;
    }

    @GetMapping("/import")
    public String importCSV() {
        String csvFile = "C:\\Users\\Eric\\Desktop\\test_data.csv";
            csvServies.readCsvFile(csvFile);
        return null;
    }

    @GetMapping("/upload")
    public String redis() {
        redisMysqlService.RedisMysql();
        return "OK";
    }

    @GetMapping("/creat")
    public String creat() {
        sqlClass.createsql();
        return "OK";
    }
}