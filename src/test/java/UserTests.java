import base.Register;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.SC_OK;

public class UserTests {
    private User user;
    private Register register;

    @Before
    public void setUp(){
        register = new Register();
        user = new User("hf@mail.ru","123456","Nikita");
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void checkNewUserregister(){
        register.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success",equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void checkCreateRepeatedUser(){
        register.createUser(user)
                .assertThat()
                .statusCode(SC_OK);
        User secondUser = new User(user.getEmail(), "123456","Повтор");
        register.createUser(secondUser)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("message",equalTo("User already exists"));
    }
    @Test
    @DisplayName("Логин под существующим пользователем")
    public void checkUserAuth(){
        register.createUser(user)
                .assertThat()
                .statusCode(SC_OK);
        register.userAuth(new User(user.getEmail(), user.getPassword()))
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success",equalTo(true));
    }
    @Test
    public void deleteUser(){
        register.userAuth(user)
                        .extract()
                        .path("accessToken");
        register.delete(user);
    }
}
