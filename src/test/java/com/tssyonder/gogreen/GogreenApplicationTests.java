package com.tssyonder.gogreen;

import com.tssyonder.gogreen.entities.Citizen;
import com.tssyonder.gogreen.entities.Request;
import com.tssyonder.gogreen.repositories.CitizenRepository;
import com.tssyonder.gogreen.services.CitizenService;
import com.tssyonder.gogreen.services.RequestService;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = GogreenApplication.class)
public class GogreenApplicationTests {

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private CitizenRepository citizenRepository;

    //@Test
    public void citizenHasNoRequests() {
        Citizen citizen = new Citizen();
        citizen.setCitizenAddress("Zorilor");
        citizen.setCitizenPhoneNumber("0751452855");
        citizen.setFirstName("Cosmin");
        citizen.setLastName("Cosma");
        citizen.setLatitude("22.752");
        citizen.setLongitude("33.712");

        citizenService.saveCitizen(citizen);

        List<Request> listRequest = requestService.getAllByUserId(1L);

        assertThat(listRequest).hasSize(0);
    }


    @After
    public void tearDown() {
        citizenRepository.deleteAll();
    }

}
