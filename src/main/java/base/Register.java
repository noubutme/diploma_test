package base;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import pojo.User;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class Register extends RestClient {
    private static final String Register_URI = RestClient.BASE_URI + "auth/register";
    private static final String Auth_URI = RestClient.BASE_URI + "auth/login";
    private static final String Delite_URI = RestClient.BASE_URI + "auth/user";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user){
        return given()
                .spec(getReqSpec())
                .body(user)
                .post(Register_URI)
                .then();
    }

    @Step("Авторизация")
    public ValidatableResponse userAuth(User user){
        return given()
                .spec(getReqSpec())
                .body(user)
                .when()
                .post(Auth_URI)
                .then();
    }
    @Step("Токен")
    public ValidatableResponse token(User user){
        return  userAuth(user)
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");
    }
    @Step("Ужаление")
    public ValidatableResponse delete(User user){
        return  given()
                .auth()
                .oauth2(token(user))
                .when()
                .delete(Delite_URI)
                .then()
                .assertThat()
                .statusCode(SC_OK);
    }
}
