package uk.gov.hmcts.reform.pip.example.service;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.pip.example.model.CourtList;
import uk.gov.hmcts.reform.pip.example.model.PublicationRequest;

@Service
public class CourtListService {

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    private boolean online = true;

    @Autowired
    private KieContainer kieContainer;

    @Autowired
    private KieSession kieSession;

    public CourtList generateCourtList
            (PublicationRequest publicationRequest, CourtList courtList) {

        kieSession.setGlobal("courtList", courtList);
        kieSession.insert(publicationRequest);
        kieSession.insert(this);
        return courtList;
    }

    public String getCurrentStatus() {
        return "We are online";
    }
}
