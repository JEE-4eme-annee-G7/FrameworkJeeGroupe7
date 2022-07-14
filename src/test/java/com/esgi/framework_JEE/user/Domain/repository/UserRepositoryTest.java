package com.esgi.framework_JEE.user.Domain.repository;

import com.esgi.framework_JEE.user.Domain.entities.User;
import com.esgi.framework_JEE.user.Domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    UserRepository userRepository;

    public User user1 = new User();
    public User user2 = new User();
    public User user3 = new User();

    @BeforeEach
    public void setup(){
        user1.setEmail("ljehanno@myges.fr");
        user1.setFirstname("lucas");
        user1.setLastname("jehanno");
        user1.setPassword("ceci est un  mot de passe tres securise !(c fo met pas ca)");

        user2.setEmail("kbervin@myges.fr");
        user2.setFirstname("kelyan");
        user2.setLastname("bervin");
        user2.setPassword("j aime les pates");

        user3.setEmail("hbah@myges.fr");
        user3.setFirstname("halimatou");
        user3.setLastname("bah");
        user3.setPassword("bahbibel");
    }
    @Test
    public void should_find_not_empty() {
        var users = userRepository.findAll();
        assertThat(users).isNotEmpty();
    }


    @Test
    public void should_find_2_user() {
        var users = userRepository.findAll();
        assertThat(users).size().isEqualTo(2);
    }

    @Test
    public void should_find_all_user() {

        entityManager.persist(user1);

        entityManager.persist(user2);

        entityManager.persist(user3);

        var users = userRepository.findAll();
        assertThat(users).hasSize(5).contains(user1, user2, user3);
    }


    @Test
    public void should_find_User_by_id() {
        entityManager.persist(user1);
        var user2Created = entityManager.persist(user2);
        var dbUser = userRepository.findById(user2Created.getId());
        assertThat(dbUser).isEqualTo(user2Created);
    }

    @Test
    public void should_delete_User_by_id() {

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        userRepository.deleteById(user2.getId());
        var users = userRepository.findAll();
        assertThat(users).hasSize(4).contains(user1, user3);
    }


    @Test
    public void should_delete_all_user() {
        entityManager.persist(user1);
        entityManager.persist(user2);

        userRepository.deleteAll();
        assertThat(userRepository.findAll()).isEmpty();
    }
}