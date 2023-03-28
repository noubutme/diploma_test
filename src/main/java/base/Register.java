package base;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.User;

import static io.restassured.RestAssured.given;

public class Register extends RestClient {
    private static final String Register_URI = RestClient.BASE_URI + "auth/register";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user){
        return given()
                .spec(getReqSpec())
                .body(user)
                .post(Register_URI)
                .then();
    }

    @Step("Авторизация")
    public ValidatableResponse userAuth(User name){
        return given()
                .spec()
                .body()
    }

}
