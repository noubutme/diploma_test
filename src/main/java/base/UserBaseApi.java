package base;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import pojo.User;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;

public class UserBaseApi extends RestClient {
    private static final String Register_URI = RestClient.BASE_URI + "auth/register";
    private static final String Auth_URI = RestClient.BASE_URI + "auth/login";
    private static final String Delite_URI = RestClient.BASE_URI + "auth/user";

    private Response response;
    private String accessToken;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken() {
        accessToken = response.then().extract().path("accessToken");
    }

    @Step("Регистарция пользователя")
    public ValidatableResponse registrateUser(User user){
        return given()
                .spec(getReqSpec())
                .body(user)
                .post(Register_URI)

                .then()
                .assertThat()
                .statusCode(SC_OK);
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
    @Step("Получение информации о пользователе")
    public void getUserInfo() {
        response = given()
                .spec(getReqSpec())
                .header("Authorization",accessToken)
                .when()
                .get(Delite_URI);
    }
    @Step("Удаление пользователя")
    public void delete() {
        if (getAccessToken() == null) return;
        given()
                .spec(getReqSpec())
                .headers("Authorization", accessToken)
                .when()
                .delete("auth/user")
                .then()
                .statusCode(SC_ACCEPTED);
        System.out.println(getAccessToken());
    }
    @Step("Изменение данных пользователя")
    public ValidatableResponse edit(User user){
     return    given()
                .spec(getReqSpec())
                .header("Authorization",accessToken)
                .body(user)
                .when()
                .patch("https://stellarburgers.nomoreparties.site/api/auth/user")
                .then();
    }
}
