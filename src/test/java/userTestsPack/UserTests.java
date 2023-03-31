package userTestsPack;

import base.UserStepsApi;
import base.util.GeneratorData;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class UserTests {
    private User testUser;
    private UserStepsApi userStepsApi;

    @Before
    public void setUp(){
        userStepsApi = new UserStepsApi();
        testUser = GeneratorData.createUser();
    }

    @Tag("Работает")
    @Test
    @DisplayName("создать уникального пользователя")
    public void createUnicUser(){
        userStepsApi.userRegister(testUser)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("accessToken",notNullValue());

    }

    @Tag("Работает")//Возможна доработка
    @Test
    @DisplayName("создать пользователя, который уже зарегистрирован")
    public void createSimilarUser(){
        userStepsApi.userRegister(testUser);
        User secondUser = testUser;
        userStepsApi.userRegister(secondUser)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("message",equalTo("User already exists"));
    }
    @Tag("Работает")
    @Test
    @DisplayName("логин под существующим пользователем")
    public void userLogin(){
        userStepsApi.userRegister(testUser);
        userStepsApi.userBasicAuth(testUser)
                .assertThat()
                .statusCode(200)
                .and()
                .body("user.email",equalTo(testUser.getEmail()));
    }
}
