package com.fastcampus.crash.config;

import com.fastcampus.crash.model.sessionspeaker.SessionSpeaker;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.fastcampus.crash.model.user.UserSignUpRequestBody;
import com.fastcampus.crash.service.SessionSpeakerService;
import com.fastcampus.crash.service.UserService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.IntStream;

@Configuration
public class ApplicationConfiguration {

    private static final Faker faker = new Faker();

    @Autowired private UserService userService;

    @Autowired private SessionSpeakerService sessionSpeakerService;

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
            // TODO user // speaker data mock data create
            createTestUsers();
            createTestSessionSpeakers(10);
            }
        };
    }

    private void createTestUsers() {
        userService.signUp(new UserSignUpRequestBody(faker.name().name(), "1234", faker.name().fullName() , "kim@crash.com"));
        userService.signUp(new UserSignUpRequestBody(faker.name().name(), "1234", faker.name().fullName() , "lee@crash.com"));
        userService.signUp(new UserSignUpRequestBody(faker.name().name(), "1234", faker.name().fullName() , faker.internet().emailAddress()));
        userService.signUp(new UserSignUpRequestBody(faker.name().name(), "1234", faker.name().fullName() , "park@crash.com"));

    }

    private void createTestSessionSpeakers(int numberOfSpeakers) {
        var sessionSpeakers =
                IntStream.range(0, numberOfSpeakers).mapToObj(i -> createTestSessionSpeaker());

//        sessionSpeakers.forEach(
//
//        );
    }

    private SessionSpeaker createTestSessionSpeaker() {
        var name = faker.name().fullName();
        var company = faker.company().name();
        var description = faker.shakespeare().romeoAndJulietQuote();

        return sessionSpeakerService.createSessionSpeaker(
                new SessionSpeakerPostRequestBody(company, name, description));

    }
}
