package userTestsPack;

import base.UserStepsApi;
import base.util.GeneratorData;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class UserTests {
    private User testUser;
    private UserStepsApi userStepsApi;
    private String accessToken;

    @Before
    public void setUp(){
        userStepsApi = new UserStepsApi();
        testUser = GeneratorData.createUser();
    }
    @After
    public void tearDown(){
        ValidatableResponse response = userStepsApi.userBasicAuth(testUser);
        accessToken = response.extract().path("accessToken").toString();
        userStepsApi.delite(accessToken);
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

    @Tag("Работает")
    @Test
    @DisplayName("логин с неверным логином и паролем")
    public void invalidLogin(){
        userStepsApi.userRegister(testUser);
        userStepsApi.userBasicAuth(new User(testUser.getEmail(),null))
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message",equalTo("email or password are incorrect"));
    }
    @Tag("Работает")
    @Test
    @DisplayName("Изменение данных пользователя без авторизацией")
    public void editUserWithAuth(){
        userStepsApi.userRegister(testUser);
        userStepsApi.editWithAuth("",testUser)
                .assertThat()
                .statusCode(401)
                .and()
                .body("message",equalTo("You should be authorised"));
}
}
