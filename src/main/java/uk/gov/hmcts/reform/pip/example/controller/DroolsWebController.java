package uk.gov.hmcts.reform.pip.example.controller;

import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.pip.example.model.CourtList;
import uk.gov.hmcts.reform.pip.example.model.PublicationRequest;
import uk.gov.hmcts.reform.pip.example.service.CourtListService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;

@RestController
public class DroolsWebController {

    private static final String xlsFile = "SPREADSHEET_DECISION_TABLE_LIST.xls";

    @Autowired
    CourtListService courtListService;

    @GetMapping("/publication/{publicationId}")
    public ResponseEntity<CourtList> getRules(@PathVariable  Integer publicationId) {
        PublicationRequest publicationRequest = new PublicationRequest();
        publicationRequest.setPublicationId(publicationId);

        System.out.println(publicationId);

        CourtList courtList =
                courtListService.generateCourtList(publicationRequest, new CourtList());

        return ResponseEntity.ok(courtList);

    }

    @GetMapping("/generateDrl")
    public ResponseEntity<String> getDrl() throws IOException, URISyntaxException {
        InputStream inputStream = new FileInputStream(
                Paths.get(ClassLoader.getSystemResource(xlsFile).toURI()).toAbsolutePath().toString());

        SpreadsheetCompiler sc = new SpreadsheetCompiler();

        String drl = sc.compile(inputStream, InputType.XLS);

        return ResponseEntity.ok(drl);

    }





}
