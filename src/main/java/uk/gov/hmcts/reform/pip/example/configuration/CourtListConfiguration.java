package uk.gov.hmcts.reform.pip.example.configuration;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourtListConfiguration {

    private static final String drlFile = "COURT_LIST_RULE.drl";
    private static final String xlsFile = "SPREADSHEET_DECISION_TABLE_LIST.xls";
    private static final String timerBasedRule = "TIMER_BASED_RULE.drl";

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
//        kieFileSystem.write(ResourceFactory.newClassPathResource(drlFile));
        kieFileSystem.write(ResourceFactory.newClassPathResource(xlsFile));
        kieFileSystem.write(ResourceFactory.newClassPathResource(timerBasedRule));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();

        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

    @Bean
    public KieSession kieSession(@Autowired KieContainer kieContainer) {
        KieSession kieSession = kieContainer.newKieSession();
        Runnable thread = kieSession::fireUntilHalt;
        new Thread(thread).start();
        return kieSession;
    }



}
