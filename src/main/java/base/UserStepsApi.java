package base;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.User;
import static io.restassured.RestAssured.given;
public class UserStepsApi extends RestClient{
    private static final String Register_URL = "https://stellarburgers.nomoreparties.site/api/auth/register";
    private static final String Auth_URL = "https://stellarburgers.nomoreparties.site/api/auth/login";

    @Step("Создание пользователя")
    public ValidatableResponse userRegister(User user){
        return given()
                .spec(getReqSpec())
                .body(user)
                .when()
                .post(Register_URL)
                .then();
    }
    @Step("Авторизация по логину и паролю")
    public ValidatableResponse userBasicAuth(User user){
        return given()
                .spec(getReqSpec())
                .body(new User(user.getEmail(),user.getPassword()))
                .when()
                .post(Auth_URL)
                .then();
    }

}
