package com.contactmanagement.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.contactmanagement.backend.entity.User;
import com.contactmanagement.backend.repository.ContactRepository;
import com.contactmanagement.backend.repository.UserRepository;
import com.contactmanagement.backend.service.ContactService;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:rollbacktest;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "jwt.secret=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY="
})
class ContactImportTransactionTest {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void importRollsBackWhenLaterContactFails() {

        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User(
                "Test User",
                "test@example.com",
                null,
                "test-password-hash"
        );

        User savedUser = userRepository.save(user);

        String longFirstName = "A".repeat(51);

        String csv =
                "First Name,Last Name,Title\n"
                + "Ali,Khan,Student\n"
                + longFirstName + ",Ahmed,Developer\n";

        assertThrows(
                RuntimeException.class,
                () -> contactService.importContactsFromCsv(csv, savedUser)
        );

        assertEquals(0, contactRepository.count());
    }
}
