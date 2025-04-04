package com.fastcampus.crash.config;

import com.fastcampus.crash.model.crashsession.CrashSessionCategory;
import com.fastcampus.crash.model.crashsession.CrashSessionPostRequestBody;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeaker;
import com.fastcampus.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.fastcampus.crash.model.user.UserSignUpRequestBody;
import com.fastcampus.crash.service.CrashSessionService;
import com.fastcampus.crash.service.SessionSpeakerService;
import com.fastcampus.crash.service.UserService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.stream.IntStream;

@Configuration
public class ApplicationConfiguration {

    private static final Faker faker = new Faker();

    @Autowired private UserService userService;

    @Autowired private SessionSpeakerService sessionSpeakerService;

    @Autowired private CrashSessionService crashSessionService;

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
                IntStream.range(0, numberOfSpeakers).mapToObj(i -> createTestSessionSpeaker()).toList();

        sessionSpeakers.forEach(
        sessionSpeaker -> {
            int numberOfSessions = new Random().nextInt(4) + 1;
            IntStream.range(0, numberOfSessions).forEach(i -> createTestCrashSession(sessionSpeaker));
        });
    }

    private SessionSpeaker createTestSessionSpeaker() {
        var name = faker.name().fullName();
        var company = faker.company().name();
        var description = faker.shakespeare().romeoAndJulietQuote();

        return sessionSpeakerService.createSessionSpeaker(
                new SessionSpeakerPostRequestBody(company, name, description));

    }

    private void createTestCrashSession(SessionSpeaker sessionSpeaker) {
        var title = faker.book().title();
        var body =
                faker.shakespeare().asYouLikeItQuote()
                + faker.shakespeare().hamletQuote()
                + faker.shakespeare().kingRichardIIIQuote()
                + faker.shakespeare().romeoAndJulietQuote();

        crashSessionService.createCrashSession(new CrashSessionPostRequestBody(
                title,
                body,
                getRandomCategory(),
                ZonedDateTime.now().plusDays(new Random().nextInt(2) + 1),
                sessionSpeaker.speakerId()
        ));
    }

    private CrashSessionCategory getRandomCategory() {
        var categories = CrashSessionCategory.values();
        int randomIndex = new Random().nextInt(categories.length);
        return categories[randomIndex];
    }

}
