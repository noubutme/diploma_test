import base.UserBaseApi;
import base.util.GeneratorData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserTests {
    private User user;
    private UserBaseApi userBaseApi;
    private String accessToken;


    @Before
    public void setUp(){
        userBaseApi = new UserBaseApi();
        user = GeneratorData.createUser();
    }
    @After
    public void deleteUser() {
        userBaseApi.delete();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void checkNewUserRegister(){
        userBaseApi.registrateUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success",equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void checkCreateRepeatedUser(){
        userBaseApi.registrateUser(user)
                .assertThat()
                .statusCode(SC_OK);
        User secondUser = new User(user.getEmail(), user.getPassword(), user.getName());
        userBaseApi.registrateUser(secondUser)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("message",equalTo("User already exists"));
    }
    @Test
    @DisplayName("Логин под существующим пользователем")
    public void checkUserAuth(){
        userBaseApi.registrateUser(user)
                .assertThat()
                .statusCode(SC_OK);
        userBaseApi.userAuth(new User(user.getEmail(), user.getPassword()))
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success",equalTo(true));
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем.")
    public void checkUserAuthWithIncorrectData(){
        userBaseApi.registrateUser(user)
                .assertThat()
                .statusCode(SC_OK);
        userBaseApi.userAuth(new User(user.getEmail(), null))
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message",equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией: изменение пароля")
    public void editUserDateWithAuth(){
        ValidatableResponse response = userBaseApi.registrateUser(user);
        accessToken = response.extract().path("accessToken").toString();
        userBaseApi.setAccessToken(accessToken);
        user.setPassword(GeneratorData.generatePassword());
        userBaseApi.edit(user);
        userBaseApi.getUserInfo();
        userBaseApi.getResponse()
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизацией")
    public void editUserDateWithoutAuth(){
        user.setPassword(GeneratorData.generatePassword());
        userBaseApi.edit(user)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("You should be authorised"));
    }
}
